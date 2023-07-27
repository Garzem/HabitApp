package com.example.newapppp.ui.redactor

import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.LiveData
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.newapppp.R
import com.example.newapppp.data.HabitColor
import com.example.newapppp.databinding.RedactorFragmentBinding
import com.example.newapppp.data.Type
import com.example.newapppp.data.UiState


class RedactorFragment : Fragment(R.layout.redactor_fragment) {

    private val binding by viewBinding(RedactorFragmentBinding::bind)
    private val args: RedactorFragmentArgs by navArgs()

    //инициализация объекта будет выполнена только при первом обращении к нему
    //т.е будет использоваться, когда дейсвительно понадобится
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
        colorFormNavController()

        redactorViewModel.setHabit(args.habit)

        onChangedHabit(redactorViewModel.uiState.value ?: return)

        val saveButton = binding.saveHabit
        saveButton.setOnClickListener {
            redactorViewModel.saveHabit()
        }

        setupResultOrError()
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
            //определяем макет для отдельного элемента выпадающего списка
            android.R.layout.simple_spinner_item,
            redactorViewModel.getList()
        )

        binding.spinnerPriority.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>,
                    view: View,
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

    private fun colorFormNavController() {
        findNavController().currentBackStackEntry?.let { entry ->
            entry.savedStateHandle.getLiveData<HabitColor>("color").observe(viewLifecycleOwner)
            { color ->
                redactorViewModel.getColor(color)
                binding.chooseColorButton.background.let { drawable ->
                    //проверяет, является ли полученный Drawable экземпляром класса GradientDrawable
                    if (drawable is GradientDrawable) {
                        val colorInt =
                            ContextCompat.getColor(requireContext(), color.getColorRedId())
                        drawable.setColor(colorInt)
                    }
                    entry.savedStateHandle.remove<HabitColor>("color")
                }
            }
        }
    }

    private fun colorDialog() {
        val action = RedactorFragmentDirections.actionHabitRedactorFragmentToColorChooseDialog()
        findNavController().navigate(action)
    }

    private fun setupResultOrError() {
        redactorViewModel.showErrorToast.observe(viewLifecycleOwner) {
            Toast.makeText(
                requireContext(),
                R.string.fill_the_line,
                Toast.LENGTH_SHORT
            ).show()
        }
        redactorViewModel.goBackWithResult.observe(viewLifecycleOwner) { habit ->
            findNavController().apply {
                popBackStack()
                val entry = currentBackStackEntry ?: return@observe
                entry.savedStateHandle["habit"] = habit
            }
        }
    }

    private fun onChangedHabit(uiState: UiState) {
        binding.editTitle.setText(uiState.title)
        binding.editDescription.setText(uiState.description)
        binding.editPeriod.setText(uiState.period)
        //???можно ли сделать запись без let? Если нет, то почему
        binding.chooseColorButton.background.let { drawable ->
            if (drawable is GradientDrawable) {
                val colorEnum = redactorViewModel.getChosenColor(uiState.color)
                drawable.setColor(ContextCompat.getColor(requireContext(), colorEnum.getColorRedId()))
            }
        }
        binding.spinnerPriority.setSelection(uiState.priorityPosition)

        if (uiState.type == 0) {
            binding.radioGood.isChecked = true
        } else {
            binding.radioBad.isChecked = true
        }
        binding.editQuantity.setText(uiState.quantity)
    }
}


