package com.example.newapppp.presentation.filter

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import androidx.fragment.app.viewModels
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.newapppp.R
import com.example.newapppp.presentation.abstracts.Suggestion
import com.example.newapppp.common.ui.element.buttons.BaseButton
import com.example.newapppp.common.ui.element.buttons.BaseButtonDefaults
import com.example.newapppp.common.ui.element.textfield.BaseTextField
import com.example.newapppp.common.ui.element.textfield.TextFieldWithMenu
import com.example.newapppp.common.ui.theme.HabitTheme
import com.example.newapppp.common.ui.theme.color.HabitAppColors
import com.example.newapppp.databinding.LayoutBottomSheetComposeBinding
import com.example.newapppp.presentation.filter.state.FilterEvent
import com.example.newapppp.presentation.filter.state.FilterState
import com.example.newapppp.presentation.habit_list.mapper.HabitPriorityMapper
import com.example.newapppp.presentation.util.collectWithLifecycle
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class BottomFilterFragment : BottomSheetDialogFragment(R.layout.layout_bottom_sheet_compose) {

    private val binding by viewBinding(LayoutBottomSheetComposeBinding::bind)
    private val bottomViewModel: BottomFilterViewModel by viewModels()


    @Inject
    lateinit var habitPriorityMapper: HabitPriorityMapper

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.root.setContent {
            val state by bottomViewModel.state.collectAsStateWithLifecycle()
            FilterScreen(
                state = state,
                onAction = bottomViewModel::onAction
            )
        }
        collectWithLifecycle(bottomViewModel.event) { event ->
            observeEvents(event)
            bottomViewModel.consumeEvents()
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

    @Composable
    private fun FilterScreen(
        state: FilterState,
        onAction: (FilterAction) -> Unit
    ) {
        Column {
            CreateFilterTitle()
            FindHabitByTitle(
                title = state.selectedTitle,
                onValueChanged = {
                    onAction(FilterAction.OnTitleFilterChanged(it))
                }
            )
            FieldWithMenu(
                selected = state.selectedPriority,
                onSuggestionClick = {
                    onAction(FilterAction.OnPriorityFilterChanged(it.id))
                }
            )
            FilterButtons(
                onFilterButtonClick = {
                    onAction(FilterAction.OnFilterButtonClick)
                },
                onCancelClicked = {
                    onAction(FilterAction.OnCancelClick)
                }
            )
        }
    }

    @Preview
    @Composable
    fun FilterScreenPreview() {
        FilterScreen(
            state = FilterState("", 0),
            onAction = {}
        )
    }

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
    fun FindHabitByTitle(
        title: String,
        onValueChanged: (String) -> Unit
    ) {
        BaseTextField(
            value = title,
            onValueChange = {
                onValueChanged(title)
            },
            labelStringId = R.string.find_habit_by_name_string,
            leadingIcon = {
                IconButton(onClick = { TextFieldValue("") }) {
                    Icon(
                        painter = painterResource(id = R.drawable.find_by_name),
                        contentDescription = stringResource(id = R.string.find_habit_by_name_icon)
                    )
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = HabitTheme.dimensions.paddingNormal)
                .padding(top = HabitTheme.dimensions.paddingLarge),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = HabitAppColors.PurpleDeep,
                unfocusedBorderColor = HabitAppColors.GreyLight
            )
        )
    }

    @Preview
    @Composable
    fun FindHabitByTitlePreview() {
        FindHabitByTitle("", {})
    }

    @Composable
    fun FieldWithMenu(
        selected: Int ,
        onSuggestionClick: (Suggestion) -> Unit
    ) {
        var expanded by remember { mutableStateOf(false) }

        TextFieldWithMenu(
            modifier = Modifier
                .padding(horizontal = HabitTheme.dimensions.paddingNormal)
                .padding(top = HabitTheme.dimensions.paddingLarge),
            expanded = expanded,
            onExpandedChange = { value ->
                expanded = value
            },
            value = bottomViewModel.priorityIntToString(selected),
            labelStringId = R.string.find_habit_by_priority_string,
            suggestionsList = stringArrayResource(id = R.array.priority_array_string)
                .mapIndexed { id, value ->
                    Suggestion(id, value)
                },
            onSuggestionClick = { suggestion ->
                onSuggestionClick(suggestion)
            },
            leadingIcon = {
                IconButton(onClick = { TextFieldValue("") }) {
                    Icon(
                        painter = painterResource(id = R.drawable.find_by_priority),
                        contentDescription = stringResource(id = R.string.find_habit_by_priority_icon)
                    )
                }
            }
        )
    }

    @Preview
    @Composable
    fun FieldWithMenuPreview() {
        FieldWithMenu(0, {})
    }

    @Composable
    fun FilterButtons(
        onFilterButtonClick: () -> Unit,
        onCancelClicked: () -> Unit
    ) {
        val isFilterApplied by remember { mutableStateOf(bottomViewModel.state.value.isFilterApplied) }

        Column {
            BaseButton(
                text = stringResource(id = R.string.filter_button_string),
                onClick = onFilterButtonClick,
                rounded = false
            )
            BaseButton(
                text = stringResource(id = R.string.cancel_filter_button),
                onClick = onCancelClicked,
                colors = BaseButtonDefaults.secondaryButtonColors,
                rounded = false,
                isEnabled = isFilterApplied
            )
        }
    }

    @Preview
    @Composable
    fun FilterButtonsPreview() {
        FilterButtons({}, {})
    }
}

