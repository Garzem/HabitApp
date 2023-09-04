package com.example.newapppp.presentation.filter

import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.newapppp.R
import com.example.newapppp.databinding.FilterBottomSheetBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class BottomFilterFragment : BottomSheetDialogFragment(R.layout.filter_bottom_sheet) {
    private val binding by viewBinding(FilterBottomSheetBinding::bind)
    private val bottomViewModel: BottomFilterViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        clearTextInFindHabit()
        setupFilterSpinner()
        setupFilterButton()
        setupCancelFilterButton()
        binding.apply {
            with(bottomViewModel) {
                findHabitByTitle.editText?.setText(filterState.value.filterByTitle)
                val autoCompleteTextView = findHabitByPriority.editText as? AutoCompleteTextView
                autoCompleteTextView?.setText(filterState.value.filterByPriority.toString(), false)
            }
        }
        observeEvents()
    }

    private fun clearTextInFindHabit() {
        val habitByName = binding.findHabitByTitle
        habitByName.setEndIconOnClickListener{
            habitByName.editText?.text?.clear()
        }
    }

    private fun setupFilterSpinner() {
        val priorityAdapter = ArrayAdapter(
            requireContext(),
            R.layout.filter_spinner_item,
            bottomViewModel.getList())
        (binding.findHabitByPriority.editText as? AutoCompleteTextView)?.apply {
            setAdapter(priorityAdapter)
            setOnItemClickListener { _, _, position: Int, _ ->
                if (position > 0) {
                    bottomViewModel.onPriorityChanged(position)
                }
            }
        }
    }

    private fun setupFilterButton() {
        binding.startFilterButton.setOnClickListener {
            bottomViewModel.onFilterClicked(binding.findHabitByTitle.editText?.text.toString())
        }
    }

    private fun setupCancelFilterButton() {
        val cancelButton = binding.cancelFilterButton
        cancelButton.isVisible = bottomViewModel.filterState.value.isFilterApplied
        cancelButton.setOnClickListener {
            bottomViewModel.apply {
                cancelFilter()
                dismiss()
            }
        }
    }

    private fun observeEvents() {
        bottomViewModel.apply {
            showErrorToast.observe(viewLifecycleOwner) {
                Toast.makeText(
                    requireContext(),
                    R.string.fill_the_filter_line,
                    Toast.LENGTH_SHORT
                ).show()
            }
            goBack.observe(viewLifecycleOwner) {
                dismiss()
            }
        }
    }
}