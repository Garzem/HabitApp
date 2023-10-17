package com.example.newapppp.presentation.filter

import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Toast
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.newapppp.R
import com.example.newapppp.common.ui.theme.color.HabitAppColors
import com.example.newapppp.databinding.FilterBottomSheetBinding
import com.example.newapppp.databinding.LayoutComposeBinding
import com.example.newapppp.domain.model.HabitPriority
import com.example.newapppp.presentation.filter.state.FilterEvent
import com.example.newapppp.presentation.habit_list.mapper.HabitPriorityMapper
import com.example.newapppp.presentation.util.collectWithLifecycle
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class BottomFilterFragment : BottomSheetDialogFragment(R.layout.layout_compose) {

    private val viewBinding by viewBinding(LayoutComposeBinding::bind)
    private val bottomViewModel: BottomFilterViewModel by viewModels()


    @Inject
    lateinit var habitPriorityMapper: HabitPriorityMapper

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewBinding.root.setContent {
            CreateFilterTitle()
            FindHabitByTitle { newFilterByTitle ->
                bottomViewModel.updateFilterTitle(newFilterByTitle)
            }
        }

//        clearTextInFindHabit()
//        setupFilterSpinner()
//        setupFilterButton()
//        collectWithLifecycle(bottomViewModel.state) { state ->
//            setupCancelFilterButton(state.filter?.isFilterApplied)
//            binding.apply {
//                findHabitByTitle.editText?.setText(state.filter?.filterByTitle)
//                val autoCompleteTextView = findHabitByPriority.editText as? AutoCompleteTextView
//                autoCompleteTextView?.setText(
//                    habitPriorityMapper.getPriorityName(
//                        state.filter?.filterByPriority ?: HabitPriority.CHOOSE
//                    ), false
//                )
//            }
//        }
        collectWithLifecycle(bottomViewModel.event) { event ->
            observeEvents(event)
            bottomViewModel.consumeEvents()
        }

    }

//    private fun clearTextInFindHabit() {
//        val habitByName = binding.findHabitByTitle
//        habitByName.setEndIconOnClickListener {
//            habitByName.editText?.text?.clear()
//        }
//    }
//
//    private fun setupFilterSpinner() {
//        val priorityAdapter = ArrayAdapter(
//            requireContext(),
//            R.layout.filter_spinner_item,
//            bottomViewModel.getList()
//        )
//        (binding.findHabitByPriority.editText as? AutoCompleteTextView)?.apply {
//            setAdapter(priorityAdapter)
//            setOnItemClickListener { _, _, position: Int, _ ->
//                if (position > 0) {
//                    bottomViewModel.onPriorityChanged(position)
//                }
//            }
//        }
//    }
//
//    private fun setupFilterButton() {
//        binding.startFilterButton.setOnClickListener {
//            bottomViewModel.onFilterClicked(binding.findHabitByTitle.editText?.text.toString())
//        }
//    }
//
//    private fun setupCancelFilterButton(isFilterApplied: Boolean?) {
//        val cancelButton = binding.cancelFilterButton
//        cancelButton.isVisible = isFilterApplied ?: false
//        cancelButton.setOnClickListener {
//            bottomViewModel.apply {
//                cancelFilter()
//                dismiss()
//            }
//        }
//    }

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

    @Preview
    @Composable
    fun CreateFilterTitle() {
        Text(
            text = stringResource(id = R.string.filters_string),
            style = MaterialTheme.typography.bodyLarge,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = HabitAppColors.Black1
        )
    }

    @Composable
    fun FindHabitByTitle(onValueChanged: (String) -> Unit) {
        var text by remember { mutableStateOf(TextFieldValue()) }

        OutlinedTextField(
            value = text,
            onValueChange = {
                text = it
                onValueChanged(it.text)
            },
            label = { Text(text = stringResource(id = R.string.find_habit_by_name_string)) },
            singleLine = true,
            trailingIcon = {
                IconButton(onClick = { text = TextFieldValue("") }) {
                    Icon(
                        painter = painterResource(id = R.drawable.find_by_name),
                        contentDescription = stringResource(id = R.string.find_habit_by_name_icon))
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = dimensionResource(id = R.dimen.small_margin))
                .padding(top = dimensionResource(id = R.dimen.large_margin)),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = HabitAppColors.Purple,
                unfocusedBorderColor = HabitAppColors.GreyLight
            )
        )
    }

    @Composable
    fun FindHabitByPriority(priorities: List<>) {
        var selectedPriority by remember { mutableIntStateOf(priorities[0]) }

        OutlinedTextField(
            value = selectedPriority,
            onValueChange = {
                selectedPriority = it
            },
            label = { Text(text = stringResource(id = R.string.find_habit_by_name_string)) },
            singleLine = true,
            trailingIcon = {
                IconButton(onClick = { text = TextFieldValue("") }) {
                    Icon(
                        painter = painterResource(id = R.drawable.find_by_name),
                        contentDescription = stringResource(id = R.string.find_habit_by_name_icon))
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = dimensionResource(id = R.dimen.small_margin))
                .padding(top = dimensionResource(id = R.dimen.large_margin)),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = HabitAppColors.Purple,
                unfocusedBorderColor = HabitAppColors.GreyLight
            )
        )
    }
}