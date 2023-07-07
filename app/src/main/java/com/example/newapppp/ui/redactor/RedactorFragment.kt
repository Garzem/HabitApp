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
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.habit_create.ColorChooseDialog
import com.example.newapppp.R
import com.example.newapppp.databinding.RedactorFragmentBinding
import com.example.newapppp.data.HabitList
import com.example.newapppp.data.Type
import com.example.newapppp.ui.home.HomeViewModel

class RedactorFragment: Fragment(), ColorChooseDialog.OnInputListener {

    private val homeViewModel: HomeViewModel by viewModels()

    private var binding: RedactorFragmentBinding? = null
    //инициализация объекта будет выполнена только при первом обращении к нему
    //т.е будет использоваться, когда дейсвительно понадобится
    private val redactorViewModel: RedactorFragmentViewModel by lazy {
        ViewModelProvider(this)[RedactorFragmentViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        //инициализирует переменную viewModel экземпляром RedactorFragmentViewModel,
        //который связан с текущим фрагментом RedactorFragment
        binding = DataBindingUtil.inflate(inflater, R.layout.redactor_fragment, container, false)
        //связываем lifecycle с текущем фрагментом для избежания ошибок и утечек памяти
        binding?.lifecycleOwner = this
        //??значения заданные в макете передаются в RedactorFragmentViewModel или наоборот
        binding?.viewModel = redactorViewModel
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        sendToViewModel()
        val saveButton = binding?.saveHabit
        saveButton?.setOnClickListener {
            saveNewHabit()
        }
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

        binding?.spinnerPriority?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
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
        findNavController().navigate(R.id.action_habit_redactor_fragment_to_color_choose_dialog)
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
            HabitList.addHabit(habit)
            //передаём хобби в HomeViewModel
            homeViewModel.setHabit(habit)
            val action = RedactorFragmentDirections.actionRedactorFragmentToViewPagerFilter(habit)
            findNavController().navigate(action)
        }
    }
}