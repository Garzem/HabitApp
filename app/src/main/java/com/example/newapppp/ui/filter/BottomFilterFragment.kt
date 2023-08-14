package com.example.newapppp.ui.filter

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.viewModels
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.newapppp.R
import com.example.newapppp.databinding.FilterBottomSheetBinding
import com.example.newapppp.ui.habit_list.HabitListViewModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class BottomFilterFragment : BottomSheetDialogFragment(R.layout.filter_bottom_sheet) {
    private val binding by viewBinding(FilterBottomSheetBinding::bind)
    private val viewModel: HabitListViewModel by viewModels(ownerProducer = { requireParentFragment() })

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupFindHabitText()
        setupFilterSpinner()
        setupFilterButton()
        setupCancelFilterButton()
        observeEvents()
    }

    private fun setupFindHabitText() {
        binding.findHabitByName.addTextChangedListener(
            onTextChanged = { text, _, _, _ ->
                viewModel.onTitleChanged(text.toString())
            }
        )
    }

    private fun setupFilterSpinner() {
        binding.findHabitByPriority.adapter = ArrayAdapter(
            requireContext(),
            R.layout.filter_spinner_item,
            viewModel.getList()
        )

        binding.findHabitByPriority.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    viewModel.onPriorityChanged(position)
                }

                override fun onNothingSelected(parent: AdapterView<*>) {}
            }
    }

    private fun setupFilterButton() {
        binding.startFilterButton.setOnClickListener {
            viewModel.getFilteredHabit()
        }
    }

    private fun setupCancelFilterButton() {
        val cancelButton = binding.cancelFilterButton
        cancelButton.isVisible = viewModel.isFilterApplied
        cancelButton.setOnClickListener {
            viewModel.apply {
                cancelFilter()
                habitType?.let {
                    setHabitType(it)
                }
                dismiss()
            }
        }
    }

    private fun observeEvents() {
        viewModel.apply {
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