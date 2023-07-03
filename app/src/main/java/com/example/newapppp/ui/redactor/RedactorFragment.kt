package com.example.newapppp.ui.redactor


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.Toast
import androidx.core.widget.doOnTextChanged
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.habit_create.ColorChooseDialog
import com.example.newapppp.R
import com.example.newapppp.databinding.RedactorFragmentBinding
import com.example.newapppp.data.HabitList
import com.example.newapppp.data.Type

class RedactorFragment: Fragment(), ColorChooseDialog.OnInputListener {

    private var binding: RedactorFragmentBinding? = null
    private lateinit var viewModel: RedactorFragmentViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        //инициализирует переменную viewModel экземпляром RedactorFragmentViewModel,
        //который связан с текущим фрагментом RedactorFragment
        binding = DataBindingUtil.inflate(inflater, R.layout.redactor_fragment, container, false)
        //обновление в соот. с жиз. циклами RedactorFragment
//        binding?.apply {
//            lifecycleOwner = this@RedactorFragment
//        }
        viewModel = ViewModelProvider(this)[RedactorFragmentViewModel::class.java]
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
        val radioGood = binding!!.radioGood
        val radioBad = binding!!.radioBad
        //??подробнее про __
        radioGood.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
            viewModel.type.value = Type.GOOD
            }
        }
        radioBad.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                viewModel.type.value = Type.BAD
            }
        }

        val spinner = binding!!.spinnerPriority
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                viewModel.getChosenPriority(position)
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

        val colorChoose = binding!!.chooseColorButton
        colorChoose.setOnClickListener {
            colorDialog()
        }
    }

    private fun colorDialog() {
        findNavController().navigate(R.id.action_habit_redactor_fragment_to_color_choose_dialog)
    }

    override fun sendColor(colorChoose: Int) {
        binding?.chooseColorButton?.setBackgroundColor(colorChoose)
        viewModel.color.value = colorChoose
    }

    private fun saveNewHabit() {
        if (!viewModel.validation()) {
            Toast.makeText(
                requireContext(),
                R.string.fill_the_line,
                Toast.LENGTH_SHORT
            ).show()
        } else {
            val habit = viewModel.makeHabit()
            HabitList.addHabit(habit)
            findNavController().navigate(R.id.action_redactor_fragment_to_view_pager_filter)
        }
    }
}