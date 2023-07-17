package com.example.newapppp.ui.redactor


import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.habit_create.ColorChooseDialog
import com.example.newapppp.R
import com.example.newapppp.databinding.RedactorFragmentBinding
import com.example.newapppp.data.Type

class RedactorFragment : Fragment(), ColorChooseDialog.OnInputListener {

    private var binding: RedactorFragmentBinding? = null
    private val args: RedactorFragmentArgs? by navArgs()
    //инициализация объекта будет выполнена только при первом обращении к нему
    //т.е будет использоваться, когда дейсвительно понадобится
    private val redactorViewModel: RedactorFragmentViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        //инициализирует переменную viewModel экземпляром RedactorFragmentViewModel,
        //который связан с текущим фрагментом RedactorFragment
        binding = DataBindingUtil.inflate(inflater, R.layout.redactor_fragment, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //связываем lifecycle с текущем фрагментом для избежания ошибок и утечек памяти
        binding?.lifecycleOwner = this
        //??значения заданные в макете передаются в RedactorFragmentViewModel или наоборот
        binding?.viewModel = redactorViewModel
        redactorViewModel.setHabit(args?.habit)
        //?почему я не могу указать this вместо viewLifecycleOwner
        redactorViewModel.priorityPosition.observe(viewLifecycleOwner) { priorityPosition ->
            binding?.spinnerPriority?.setSelection(priorityPosition)
        }
        sendToViewModel()
        val saveButton = binding?.saveHabit
        saveButton?.setOnClickListener {
            saveNewHabit()
        }
    }

    private fun newHabitFilter() {

    }

    private fun sendToViewModel() {
        val radioGood = binding?.radioGood
        val radioBad = binding?.radioBad
        //??подробнее про __
        radioGood?.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                redactorViewModel.type.value = Type.GOOD
            }
        }
        radioBad?.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                redactorViewModel.type.value = Type.BAD
            }
        }
        //создаём адаптер для списка элементов
        binding?.spinnerPriority?.adapter = ArrayAdapter(
            requireContext(),
            //определяем макет для отдельного элемента выпадающего списка
            android.R.layout.simple_spinner_item,
            redactorViewModel.getList()
        )

        binding?.spinnerPriority?.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    redactorViewModel.getChosenPriority(position)
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {}
            }

        val colorChoose = binding?.chooseColorButton
        colorChoose?.setOnClickListener {
            colorDialog()
        }
    }

    private fun colorDialog() {
        val action = RedactorFragmentDirections.actionHabitRedactorFragmentToColorChooseDialog()
        findNavController().navigate(action)
    }

    override fun sendColor(colorChoose: Int) {
        //drawable ссылается на полученный фоновый Drawable
        binding?.chooseColorButton?.background?.let { drawable ->
            //проверяет, является ли полученный Drawable экземпляром класса GradientDrawable
            if (drawable is GradientDrawable) {
                drawable.setColor(colorChoose)
            }
        }
        redactorViewModel.color.value = colorChoose
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
