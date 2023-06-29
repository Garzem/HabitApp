package com.example.newapppp.redactor


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
import com.example.newapppp.ui.data.HabitList
import com.example.newapppp.ui.data.Type

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
        binding?.apply {
            lifecycleOwner = this@RedactorFragment
        }
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
        val title = binding!!.editTitle
        val description = binding!!.editDescription
        val period = binding!!.editPeriod
        val quantity = binding!!.editQuantity

        //??подробнее
        title.doOnTextChanged { text, _, _, _ ->
            viewModel.title.value = text.toString()
        }

        description.doOnTextChanged { text, _, _, _ ->
            viewModel.description.value = text.toString()
        }

        period.doOnTextChanged { text, _, _, _ ->
            viewModel.period.value = text.toString()
        }

        quantity.doOnTextChanged { text, _, _, _ ->
            viewModel.quantity.value = text.toString()
        }

        val radioGood = binding!!.radioGood
        val radioBad = binding!!.radioBad
        //??подробнее про __
        radioGood.setOnCheckedChangeListener { _, isChecked ->
            viewModel.type.value = Type.GOOD
        }
        radioBad.setOnCheckedChangeListener { _, isChecked ->
            viewModel.type.value = Type.BAD
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
        val colorChooseDialog = ColorChooseDialog(this)
        colorChooseDialog.show(childFragmentManager, "colorChooseDialog")
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
            findNavController().navigate(R.id.action_redactorFragment_to_homeFragment)
        }
    }
}