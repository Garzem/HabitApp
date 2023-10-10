package com.example.newapppp.presentation.redactor

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.newapppp.R
import com.example.newapppp.domain.Constants.COLOR_KEY
import com.example.newapppp.domain.model.HabitColor
import com.example.newapppp.domain.model.HabitType
import com.example.newapppp.databinding.RedactorFragmentBinding
import com.example.newapppp.domain.extension.collectWithLifecycle
import com.example.newapppp.presentation.habit_list.mapper.HabitColorMapper
import com.example.newapppp.presentation.redactor.state.UiState
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class RedactorFragment : Fragment(R.layout.redactor_fragment) {

    @Inject
    lateinit var habitColorMapper: HabitColorMapper

    private val binding by viewBinding(RedactorFragmentBinding::bind)
    private val args: RedactorFragmentArgs by navArgs()
    private val redactorViewModel: RedactorFragmentViewModel by viewModels()

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
            redactorViewModel.setType(it)
        }
        redactorViewModel.setHabit(args.habitId)

        collectWithLifecycle(redactorViewModel.uiState) { uiState ->
            onChangedHabit(uiState)
        }

        setupButtons()
        observeEvents()
    }

    private fun setupTitleText() {
        binding.editTitle.addTextChangedListener(
            onTextChanged = { text, start, _, count ->
                redactorViewModel.onTitleChanged(text.toString(), start + count)
            }
        )
    }

    private fun setupDescriptionText() {
        binding.editDescription.addTextChangedListener(
            onTextChanged = { text, start, _, count ->
                redactorViewModel.onDescriptionChanged(text.toString(), start + count)
            }
        )
    }

    private fun setupFrequencyText() {
        binding.editFrequency.addTextChangedListener(
            onTextChanged = { text, start, _, count ->
                    redactorViewModel.onFrequencyChanged(text.toString(), start + count)
            }
        )
    }

    private fun setupPrioritySpinner() {
        binding.spinnerPriority.adapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_item,
            redactorViewModel.getHabitPriorityList()
        )

        binding.spinnerPriority.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    redactorViewModel.onPrioritySelected(position)
                }

                override fun onNothingSelected(parent: AdapterView<*>) {}
            }
    }

    private fun setupCountSpinner() {
        binding.spinnerCount.adapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_item,
            redactorViewModel.getHabitCountList()
        )

        binding.spinnerCount.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    redactorViewModel.onCountSelected(position)
                }

                override fun onNothingSelected(parent: AdapterView<*>) {}
            }
    }

    private fun setupRadioButtons() {
        val radioGood = binding.radioGood
        val radioBad = binding.radioBad

        radioGood.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                redactorViewModel.setType(HabitType.GOOD)
            }
        }
        radioBad.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                redactorViewModel.setType(HabitType.BAD)
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
            redactorViewModel.saveOrUpdateHabitToServer()
        }
        binding.deleteHabit.apply {
            setOnClickListener {
                redactorViewModel.deleteHabit()
            }
            isVisible = args.habitId != null
        }
    }

    private fun observeColorResult() {
        collectWithLifecycle(redactorViewModel.uiState) { uiState ->
            redactorViewModel.saveColor(uiState.color)
        }

        findNavController().currentBackStackEntry?.let { entry ->
            entry.savedStateHandle.getLiveData<HabitColor>(COLOR_KEY).observe(viewLifecycleOwner)
            { color ->
                redactorViewModel.saveColor(color)
                entry.savedStateHandle.remove<HabitColor>(COLOR_KEY)
            }
        }
    }

    private fun colorDialog() {
        val action = RedactorFragmentDirections.actionHabitRedactorFragmentToColorChooseDialog()
        findNavController().navigate(action)
    }


    private fun observeEvents() {
        redactorViewModel.apply {
            showValidationError.observe(viewLifecycleOwner) {
                Toast.makeText(
                    requireContext(),
                    R.string.fill_the_line,
                    Toast.LENGTH_SHORT
                ).show()
            }

            goBack.observe(viewLifecycleOwner) {
                findNavController().popBackStack()
            }
        }
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


