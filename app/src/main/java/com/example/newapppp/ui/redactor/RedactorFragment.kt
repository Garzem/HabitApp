package com.example.newapppp.ui.redactor

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewModelScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.newapppp.R
import com.example.newapppp.data.AppHabitDataBase
import com.example.newapppp.data.Constants.COLOR_KEY
import com.example.newapppp.data.Constants.HABIT_KEY
import com.example.newapppp.data.Constants.HABIT_ID_FROM_REDACTOR_KEY
import com.example.newapppp.data.Habit
import com.example.newapppp.data.HabitColor
import com.example.newapppp.data.HabitDao
import com.example.newapppp.data.Type
import com.example.newapppp.data.UiState
import com.example.newapppp.databinding.RedactorFragmentBinding
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class RedactorFragment : Fragment(R.layout.redactor_fragment) {

    private val binding by viewBinding(RedactorFragmentBinding::bind)
    private val args: RedactorFragmentArgs by navArgs()
    private val redactorViewModel: RedactorFragmentViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupTitleText()
        setupDescriptionText()
        setupQuantityText()
        setupPeriodText()
        setupPrioritySpinner()
        setupRadioButtons()
        setupColorButton()
        observeColorResult()

        redactorViewModel.setHabit(args.habit)

        FlowExtension().apply {
            collectWithLifecycle(redactorViewModel.uiState) {uiState ->
                onChangedHabit(uiState)
            }
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

    private fun setupQuantityText() {
        binding.editQuantity.addTextChangedListener(
            onTextChanged = { text, start, _, count ->
                redactorViewModel.onQuantityChanged(text.toString(), start + count)
            }
        )
    }

    private fun setupPeriodText() {
        binding.editPeriod.addTextChangedListener(
            onTextChanged = { text, start, _, count ->
                redactorViewModel.onPeriodChanged(text.toString(), start + count)
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
                redactorViewModel.setupType(Type.GOOD)
            }
        }
        radioBad.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                redactorViewModel.setupType(Type.BAD)
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
            redactorViewModel.saveHabit()
        }
        binding.deleteHabit.apply {
            setOnClickListener{
//                deleteHabitInViewPager(args.habit ?: return@setOnClickListener)
                if (args.habit != null) {
                    redactorViewModel.viewModelScope.launch {
                        redactorViewModel.habitDao.deleteHabit(args.habit!!)
                    }
                }
            }
            isVisible = args.habit != null
        }
    }
    private fun observeColorResult() {
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

//    private fun deleteHabitInViewPager(habit: Habit) {
//        val habitId = habit.id
//        findNavController().apply {
//            popBackStack()
//            val entry = currentBackStackEntry ?: return
//            entry.savedStateHandle[HABIT_ID_FROM_REDACTOR_KEY] = habitId
//        }
//    }

    private fun observeEvents() {
        redactorViewModel.showErrorToast.observe(viewLifecycleOwner) {
            Toast.makeText(
                requireContext(),
                R.string.fill_the_line,
                Toast.LENGTH_SHORT
            ).show()
        }
//        redactorViewModel.goBackWithResult.observe(viewLifecycleOwner) { habit ->
//            findNavController().apply {
//                popBackStack()
//                val entry = currentBackStackEntry ?: return@observe
//                entry.savedStateHandle[HABIT_KEY] = habit
//            }
//        }
    }

    private fun onChangedHabit(uiState: UiState) {
        binding.editTitle.setText(uiState.title)
        binding.editTitle.setSelection(uiState.titleCursorPosition)
        binding.editDescription.setText(uiState.description)
        binding.editDescription.setSelection(uiState.descriptionCursorPosition)
        binding.editPeriod.setText(uiState.period)
        binding.editPeriod.setSelection(uiState.periodCursorPosition)

        binding.chooseColorButton.setBackgroundResource(uiState.color.getBackGroundResId())
        binding.spinnerPriority.setSelection(uiState.priorityPosition)

        if (uiState.type == 0) {
            binding.radioGood.isChecked = true
        } else {
            binding.radioBad.isChecked = true
        }
        binding.editQuantity.setText(uiState.quantity)
        binding.editQuantity.setSelection(uiState.quantityCursorPosition)
    }
}


