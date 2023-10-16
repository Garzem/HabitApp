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
import com.example.newapppp.domain.model.HabitPriority
import com.example.newapppp.presentation.filter.state.FilterEvent
import com.example.newapppp.presentation.habit_list.mapper.HabitPriorityMapper
import com.example.newapppp.presentation.util.collectWithLifecycle
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class BottomFilterFragment : BottomSheetDialogFragment(R.layout.filter_bottom_sheet) {

    private val binding by viewBinding(FilterBottomSheetBinding::bind)
    private val bottomViewModel: BottomFilterViewModel by viewModels()

    @Inject
    lateinit var habitPriorityMapper: HabitPriorityMapper

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        clearTextInFindHabit()
        setupFilterSpinner()
        setupFilterButton()
        collectWithLifecycle(bottomViewModel.state) { state ->
            setupCancelFilterButton(state.filter?.isFilterApplied)
            binding.apply {
                findHabitByTitle.editText?.setText(state.filter?.filterByTitle)
                val autoCompleteTextView = findHabitByPriority.editText as? AutoCompleteTextView
                autoCompleteTextView?.setText(
                    habitPriorityMapper.getPriorityName(
                        state.filter?.filterByPriority ?: HabitPriority.CHOOSE
                    ), false
                )
            }
        }
        collectWithLifecycle(bottomViewModel.event) { event ->
            observeEvents(event)
            bottomViewModel.consumeEvents()
        }

    }

    private fun clearTextInFindHabit() {
        val habitByName = binding.findHabitByTitle
        habitByName.setEndIconOnClickListener {
            habitByName.editText?.text?.clear()
        }
    }

    private fun setupFilterSpinner() {
        val priorityAdapter = ArrayAdapter(
            requireContext(),
            R.layout.filter_spinner_item,
            bottomViewModel.getList()
        )
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

    private fun setupCancelFilterButton(isFilterApplied: Boolean?) {
        val cancelButton = binding.cancelFilterButton
        cancelButton.isVisible = isFilterApplied ?: false
        cancelButton.setOnClickListener {
            bottomViewModel.apply {
                cancelFilter()
                dismiss()
            }
        }
    }

    private fun observeEvents(event: FilterEvent?) {
        when (event) {
            is FilterEvent.ShowErrorToast -> {
                Toast.makeText(
                    requireContext(),
                    R.string.fill_the_filter_line,
                    Toast.LENGTH_SHORT
                ).show()
            }
            is FilterEvent.GoBack -> {
                dismiss()
            }
            null -> {}
        }
    }
}