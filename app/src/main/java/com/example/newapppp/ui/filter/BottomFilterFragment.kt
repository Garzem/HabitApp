package com.example.newapppp.ui.filter

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.newapppp.R
import com.example.newapppp.data.Constants.HABIT_LIST_INT_KEY
import com.example.newapppp.data.Habit
import com.example.newapppp.databinding.FilterBottomSheetBinding
import com.example.newapppp.ui.habit_list.HabitListViewModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class BottomFilterFragment : BottomSheetDialogFragment(R.layout.filter_bottom_sheet) {
    private val binding by viewBinding(FilterBottomSheetBinding::bind)
    private val HLViewModel: HabitListViewModel by viewModels(ownerProducer = { requireParentFragment() })

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
                HLViewModel.onTitleChanged(text.toString())
            }
        )
    }

    private fun setupFilterSpinner() {
        binding.priorityFilter.adapter = ArrayAdapter(
            requireContext(),
            R.layout.filter_spinner_item,
            HLViewModel.getList()
        )

        binding.priorityFilter.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    HLViewModel.onPriorityChanged(position)
                }

                override fun onNothingSelected(parent: AdapterView<*>) {}
            }
    }

    private fun setupFilterButton() {
        binding.startFilterButton.setOnClickListener {
            HLViewModel.getFilteredHabit()
        }
    }

    private fun observeEvents() {
        HLViewModel.showErrorToast.observe(viewLifecycleOwner) {
            Toast.makeText(
                requireContext(),
                R.string.fill_the_filter_line,
                Toast.LENGTH_SHORT
            ).show()
        }
        HLViewModel.apply {
            goBack.observe(viewLifecycleOwner) {
                findNavController().popBackStack()
            }
        }
    }
}