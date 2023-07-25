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
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.newapppp.R
import com.example.newapppp.data.Habit
import com.example.newapppp.data.HabitColor
import com.example.newapppp.databinding.RedactorFragmentBinding
import com.example.newapppp.data.Type


class RedactorFragment : Fragment(R.layout.redactor_fragment) {

    private val binding by viewBinding(RedactorFragmentBinding::bind)
    private val args: RedactorFragmentArgs by navArgs()
    //инициализация объекта будет выполнена только при первом обращении к нему
    //т.е будет использоваться, когда дейсвительно понадобится
    private val redactorViewModel: RedactorFragmentViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        findNavController().currentBackStackEntry?.let { entry ->
            entry.savedStateHandle.getLiveData<HabitColor>("color").observe(viewLifecycleOwner)
            { color ->
                redactorViewModel.getColor(color)
                binding.chooseColorButton.background.let { drawable ->
                    //проверяет, является ли полученный Drawable экземпляром класса GradientDrawable
                    if (drawable is GradientDrawable) {
                        val colorInt  = ContextCompat.getColor(requireContext(), color.getColorRedId())
                        drawable.setColor(colorInt)
                    }
                    entry.savedStateHandle.remove<HabitColor>("color")
                }
            }
        }
        redactorViewModel.setHabit(args.habit)
        //??почему не могу this
        redactorViewModel.uiState.observe(viewLifecycleOwner) { uiState ->
            uiState.let { changedState ->
            binding.spinnerPriority.setSelection(changedState.priorityPosition) }
        }
        sendToViewModel()
        val saveButton = binding.saveHabit
        saveButton.setOnClickListener {
            saveNewHabit()
        }
    }

    private fun sendToViewModel() {
        val radioGood = binding.radioGood
        val radioBad = binding.radioBad

        //считаывает информацию с текстовых полей
        binding.editTitle.addTextChangedListener{
            redactorViewModel.getTitle(it.toString())
        }

        binding.editDescription.addTextChangedListener{
            redactorViewModel.getDescription(it.toString())
        }

        binding.editQuantity.addTextChangedListener{
            redactorViewModel.getQuantity(it.toString())
        }

        binding.editPeriod.addTextChangedListener{
            redactorViewModel.getPeriod(it.toString())
        }

        //??подробнее про __
        radioGood.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                redactorViewModel.getType(Type.GOOD)
            }
        }
        radioBad.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                redactorViewModel.getType(Type.BAD)
            }
        }
        //создаём адаптер для списка элементов
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
                    redactorViewModel.getPriority(position)
                }

                override fun onNothingSelected(parent: AdapterView<*>) {}
            }

        val colorChoose = binding.chooseColorButton
        colorChoose.setOnClickListener {
            colorDialog()
        }
    }

    private fun colorDialog() {
        val action = RedactorFragmentDirections.actionHabitRedactorFragmentToColorChooseDialog()
        findNavController().navigate(action)
    }

    private fun saveNewHabit() {
        if (!redactorViewModel.validation()) {
            Toast.makeText(
                requireContext(),
                R.string.fill_the_line,
                Toast.LENGTH_SHORT
            ).show()
        } else {
            val habit = redactorViewModel.makeHabit()
            findNavController().apply {
                popBackStack()
                val entry = currentBackStackEntry ?: return
                entry.savedStateHandle.set("habit", habit)
            }
        }
    }
}

