package com.example.newapppp.presentation.redactor

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.newapppp.R
import com.example.newapppp.abstracts.BaseFragment
import com.example.newapppp.abstracts.RedactorEvents
import com.example.newapppp.domain.Constants.COLOR_KEY
import com.example.newapppp.domain.model.HabitColor
import com.example.newapppp.domain.model.HabitType
import com.example.newapppp.databinding.RedactorFragmentBinding
import com.example.newapppp.presentation.util.collectWithLifecycle
import com.example.newapppp.presentation.habit_list.mapper.HabitColorMapper
import com.example.newapppp.presentation.redactor.state.UiState
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class RedactorFragment : BaseFragment<UiState, RedactorEvents>(R.layout.redactor_fragment) {

    @Inject
    lateinit var habitColorMapper: HabitColorMapper

    private val binding by viewBinding(RedactorFragmentBinding::bind)
    private val args: RedactorFragmentArgs by navArgs()
    override val viewModel: RedactorFragmentViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupTitleText()
        setupDescriptionText()
        setupFrequencyText()
        setupCountSpinner()
        setupPrioritySpinner()
        setupRadioButtons()
        setupColorButton()
        observeColorResult()
        args.habitType?.let {
            viewModel.setType(it)
        }
        viewModel.setHabit(args.habitId)

        setupButtons()
    }

    override fun handleState(state: UiState) {
        onChangedHabit(state)
    }

    override fun handleEvent(event: RedactorEvents) {
        when (event) {
            is RedactorEvents.ShowValidationError -> {
                Toast.makeText(
                    requireContext(),
                    R.string.fill_the_line,
                    Toast.LENGTH_SHORT
                ).show()
            }
            is RedactorEvents.GoBack -> {
                findNavController().popBackStack()
            }
        }
    }

    private fun setupTitleText() {
        binding.editTitle.addTextChangedListener(
            onTextChanged = { text, start, _, count ->
                viewModel.onTitleChanged(text.toString(), start + count)
            }
        )
    }

    private fun setupDescriptionText() {
        binding.editDescription.addTextChangedListener(
            onTextChanged = { text, start, _, count ->
                viewModel.onDescriptionChanged(text.toString(), start + count)
            }
        )
    }

    private fun setupFrequencyText() {
        binding.editFrequency.addTextChangedListener(
            onTextChanged = { text, start, _, count ->
                viewModel.onFrequencyChanged(text.toString(), start + count)
            }
        )
    }

    private fun setupPrioritySpinner() {
        binding.spinnerPriority.adapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_item,
            viewModel.getHabitPriorityList()
        )

        binding.spinnerPriority.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    viewModel.onPrioritySelected(position)
                }

                override fun onNothingSelected(parent: AdapterView<*>) {}
            }
    }

    private fun setupCountSpinner() {
        binding.spinnerCount.adapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_item,
            viewModel.getHabitCountList()
        )

        binding.spinnerCount.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    viewModel.onCountSelected(position)
                }

                override fun onNothingSelected(parent: AdapterView<*>) {}
            }
    }

    private fun setupRadioButtons() {
        val radioGood = binding.radioGood
        val radioBad = binding.radioBad

        radioGood.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                viewModel.setType(HabitType.GOOD)
            }
        }
        radioBad.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                viewModel.setType(HabitType.BAD)
            }
        }
    }

    private fun setupColorButton() {
        val colorChoose = binding.chooseColorButton
        colorChoose.setOnClickListener {
            colorDialog()
        }
    }

    private fun setupButtons() {
        binding.saveHabit.setOnClickListener {
            viewModel.saveClick()
        }
        binding.deleteHabit.apply {
            setOnClickListener {
                viewModel.deleteHabit()
            }
            isVisible = args.habitId != null
        }
    }

    private fun observeColorResult() {
        collectWithLifecycle(viewModel.state) { uiState ->
            viewModel.saveColor(uiState.color)
        }

        findNavController().currentBackStackEntry?.let { entry ->
            entry.savedStateHandle.getLiveData<HabitColor>(COLOR_KEY).observe(viewLifecycleOwner)
            { color ->
                viewModel.saveColor(color)
                entry.savedStateHandle.remove<HabitColor>(COLOR_KEY)
            }
        }
    }


    private fun colorDialog() {
        val action = RedactorFragmentDirections.actionHabitRedactorFragmentToColorChooseDialog()
        findNavController().navigate(action)
    }

    private fun onChangedHabit(uiState: UiState) {
        binding.editTitle.setText(uiState.title)
        binding.editTitle.setSelection(uiState.titleCursorPosition)
        binding.editDescription.setText(uiState.description)
        binding.editDescription.setSelection(uiState.descriptionCursorPosition)

        binding.chooseColorButton.setBackgroundResource(habitColorMapper.getBackGroundResId(uiState.color))
        binding.spinnerPriority.setSelection(uiState.priority)
        binding.spinnerCount.setSelection(uiState.count)

        if (uiState.type == 0) {
            binding.radioGood.isChecked = true
        } else {
            binding.radioBad.isChecked = true
        }
        binding.editFrequency.setText(uiState.frequency)
        binding.editFrequency.setSelection(uiState.frequencyCursorPosition)
    }
}


