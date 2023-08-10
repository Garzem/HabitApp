package com.example.newapppp.ui.filter

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.newapppp.R
import com.example.newapppp.databinding.FilterBottomSheetBinding
import com.example.newapppp.extension.collectWithLifecycle
import com.example.newapppp.ui.redactor.RedactorFragmentViewModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class BottomFilterFragment: BottomSheetDialogFragment(R.layout.filter_bottom_sheet) {
    private val binding by viewBinding(FilterBottomSheetBinding::bind)
    private val BFViewModel: BottomFilterViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupFindHabitText()
        setupFilterSpinner()
        setupFilterButton()
        observeEvents()
    }

    private fun setupFindHabitText() {
        binding.findHabitByName.addTextChangedListener(
            onTextChanged = { text, _, _, _ ->
                BFViewModel.onTitleChanged(text.toString())
            }
        )
    }

    private fun setupFilterSpinner() {
        binding.priorityFilter.adapter = ArrayAdapter(
            requireContext(),
            R.layout.filter_spinner_item,
            BFViewModel.getList()
        )

        binding.priorityFilter.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    BFViewModel.onPriorityChanged(position)
                }

                override fun onNothingSelected(parent: AdapterView<*>) {}
            }
    }

    private fun setupFilterButton() {
        binding.startFilterButton.setOnClickListener {
            BFViewModel.filterHabit()
        }
    }

    private fun observeEvents() {
        BFViewModel.showErrorToast.observe(viewLifecycleOwner) {
            Toast.makeText(
                requireContext(),
                R.string.fill_the_filter_line,
                Toast.LENGTH_SHORT
            ).show()
        }
        BFViewModel.apply {
            goBack.observe(viewLifecycleOwner) {
                findNavController().popBackStack()
            }
        }
    }
}