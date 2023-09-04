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
import com.example.newapppp.domain.habit_local.HabitColor
import com.example.newapppp.domain.habit_local.HabitType
import com.example.newapppp.databinding.RedactorFragmentBinding
import com.example.newapppp.domain.extension.collectWithLifecycle

class RedactorFragment : Fragment(R.layout.redactor_fragment) {

    private val binding by viewBinding(RedactorFragmentBinding::bind)
    private val args: RedactorFragmentArgs by navArgs()
    private val redactorViewModel: RedactorFragmentViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupTitleText()
        setupDescriptionText()
        setupFrequencyText()
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
            redactorViewModel.getList()
        )

        binding.spinnerPriority.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    redactorViewModel.onNewPrioritySelected(position)
                }

                override fun onNothingSelected(parent: AdapterView<*>) {}
            }
    }

    private fun setupRadioButtons() {
        val radioGood = binding.radioGood
        val radioBad = binding.radioBad

        radioGood.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                redactorViewModel.setupType(HabitType.GOOD)
            }
        }
        radioBad.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                redactorViewModel.setupType(HabitType.BAD)
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
            showSendingError.observe(viewLifecycleOwner) {
                Toast.makeText(
                    requireContext(),
                    R.string.error_with_sending_habit_to_server,
                    Toast.LENGTH_SHORT
                ).show()
                findNavController().popBackStack()
            }

            showDeleteError.observe(viewLifecycleOwner) {
                Toast.makeText(
                    requireContext(),
                    R.string.delete_problem,
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

        binding.chooseColorButton.setBackgroundResource(uiState.color.getBackGroundResId())
        binding.spinnerPriority.setSelection(uiState.priority)

        if (uiState.type == 0) {
            binding.radioGood.isChecked = true
        } else {
            binding.radioBad.isChecked = true
        }
        binding.editFrequency.setText(uiState.frequency.toString())
        binding.editFrequency.setSelection(uiState.frequencyCursorPosition)
    }
}


