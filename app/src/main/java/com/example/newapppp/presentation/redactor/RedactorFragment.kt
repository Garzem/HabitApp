package com.example.newapppp.presentation.redactor

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.modifier.modifierLocalConsumer
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.fragment.app.viewModels
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.newapppp.R
import com.example.newapppp.common.ui.element.button.BaseButton
import com.example.newapppp.common.ui.element.button.BaseButtonDefaults
import com.example.newapppp.common.ui.element.text.BaseText
import com.example.newapppp.common.ui.element.textfield.BaseTextField
import com.example.newapppp.common.ui.element.textmenu.TextFieldWithMenu
import com.example.newapppp.common.ui.theme.HabitTheme
import com.example.newapppp.common.ui.topbar.BaseTopBar
import com.example.newapppp.databinding.LayoutComposeBinding
import com.example.newapppp.domain.Constants.COLOR_KEY
import com.example.newapppp.domain.model.HabitColor
import com.example.newapppp.domain.model.HabitCount
import com.example.newapppp.domain.model.HabitPriority
import com.example.newapppp.domain.model.HabitType
import com.example.newapppp.presentation.abstracts.BaseFragment
import com.example.newapppp.presentation.abstracts.Suggestion
import com.example.newapppp.presentation.redactor.state.RedactorEvents
import com.example.newapppp.presentation.redactor.state.UiState
import com.example.newapppp.presentation.util.collectWithLifecycle
import com.example.newapppp.presentation.util.setContentWithTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RedactorFragment : BaseFragment<UiState, RedactorEvents>(R.layout.layout_compose) {

    private val binding by viewBinding(LayoutComposeBinding::bind)
    private val args: RedactorFragmentArgs by navArgs()
    override val viewModel: RedactorFragmentViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        args.habitType?.let {
            viewModel.setType(it)
        }
        viewModel.setHabit(args.habitId)
        observeColorResult()
        binding.root.setContentWithTheme {
            val state by viewModel.state.collectAsStateWithLifecycle()
            RedactorScreen(
                state = state,
                onAction = viewModel::onAction
            )
        }
    }

    @Composable
    private fun RedactorScreen(
        state: UiState,
        onAction: (RedactorAction) -> Unit
    ) {
        Scaffold(
            topBar = {
                BaseTopBar(
                    titleId = R.string.redactor_title,
                    backActionClick = {
                        findNavController().popBackStack()
                    }
                )
            },
            containerColor = HabitTheme.colors.background
        ) { padding ->
            RedactorContent(
                state = state,
                padding = padding,
                onAction = onAction
            )
        }
    }

    @Composable
    private fun RedactorContent(
        state: UiState,
        padding: PaddingValues,
        onAction: (RedactorAction) -> Unit
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(HabitTheme.dimensions.paddingNormal)
        ) {
            Column(
                modifier = Modifier.fillMaxSize(),
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Bottom
                ) {
                    BaseTextField(
                        labelStringId = R.string.title_of_habit,
                        onValueChange = {
                            onAction(RedactorAction.OnTitleChanged(it))
                        },
                        modifier = Modifier
                            .fillMaxWidth(0.75f)
                            .padding(
                                end = HabitTheme.dimensions.paddingNormal
                            )
                    )
                    ColorField(
                        state = state
                    )
                }

                BaseTextField(
                    labelStringId = R.string.habit_description,
                    onValueChange = {
                        onAction(RedactorAction.OnDescriptionChanged(it))
                    },
                    modifier = Modifier.fillMaxWidth()
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Top
                ) {
                    BaseTextField(
                        labelStringId = R.string.frequency_doing_habit,
                        onValueChange = {
                            onAction(RedactorAction.OnFrequencyChanged(it))
                        },
                        modifier = Modifier.fillMaxWidth(0.50f),
                        keyboardType = KeyboardType.Number,
                    )
                    CountSpinner(
                        selectedCount = state.selectedCountLocalized,
                        onAction = onAction
                    )
                }

                PrioritySpinner(
                    selectedPriority = state.selectedPriorityLocalized,
                    onAction = onAction
                )

                Box(
                    modifier = Modifier.padding(
                        top = HabitTheme.dimensions.paddingNormal
                    )
                ) {
                    RadioGroupField(
                        state = state,
                        onAction = onAction
                    )
                }

                SetupButtons(
                    state = state,
                    onAction = onAction
                )
            }
        }
    }

    @Composable
    private fun ColorField(state: UiState) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .border(
                    width = HabitTheme.dimensions.stroke,
                    color = HabitTheme.colors.borderStroke,
                    shape = RoundedCornerShape(HabitTheme.dimensions.viewRoundedCorner)
                )
                .padding(HabitTheme.dimensions.paddingNormal)
        ) {
            BaseButton(
                colors = BaseButtonDefaults.chooseColorButton(state.color),
                onClick = {
                    colorDialog()
                },
                textStringId = R.string.color_string,
                contentPadding = PaddingValues(HabitTheme.dimensions.paddingNormal)
            )
        }
    }

    @Composable
    private fun CountSpinner(
        selectedCount: String,
        onAction: (RedactorAction) -> Unit
    ) {
        var expanded by remember { mutableStateOf(false) }

        TextFieldWithMenu(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    start = HabitTheme.dimensions.paddingNormal
                ),
            expanded = expanded,
            onExpandedChange = { value ->
                expanded = value
            },
            labelStringId = R.string.period_string,
            value = selectedCount,
            suggestionsList = stringArrayResource(id = R.array.count_array_string)
                .mapIndexed { id, value ->
                    Suggestion(id, value)
                },
            onSuggestionClick = { suggestion ->
                onAction(RedactorAction.OnPriorityChanged(suggestion.id))
            }
        )
    }

    @Composable
    private fun PrioritySpinner(
        selectedPriority: String,
        onAction: (RedactorAction) -> Unit
    ) {
        var expanded by remember { mutableStateOf(false) }

        TextFieldWithMenu(
            modifier = Modifier
                .fillMaxWidth(),
            expanded = expanded,
            onExpandedChange = { value ->
                expanded = value
            },
            labelStringId = R.string.priority_string,
            value = selectedPriority,
            suggestionsList = stringArrayResource(id = R.array.priority_array_string)
                .mapIndexed { id, value ->
                    Suggestion(id, value)
                },
            onSuggestionClick = { suggestion ->
                onAction(RedactorAction.OnPriorityChanged(suggestion.id))
            }
        )
    }

    @Composable
    private fun RadioGroupField(
        state: UiState,
        onAction: (RedactorAction) -> Unit
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .border(
                    width = HabitTheme.dimensions.stroke,
                    color = HabitTheme.colors.borderStroke,
                    shape = RoundedCornerShape(HabitTheme.dimensions.viewRoundedCorner)
                )
                .padding(HabitTheme.dimensions.paddingBig)
        ) {
            Column {
                BaseText(
                    textStringId = R.string.choose_the_type,
                    color = HabitTheme.colors.colorTextInField,
                    modifier = Modifier.fillMaxWidth()
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    RadioButton(
                        selected = state.type == HabitType.GOOD.ordinal,
                        onClick = {
                            onAction(RedactorAction.OnRadioButtonClick(HabitType.GOOD))
                        }
                    )
                    BaseText(
                        textStringId = R.string.good_habit,
                        color = HabitTheme.colors.colorTextInField
                    )

                    Spacer(modifier = Modifier.width(HabitTheme.dimensions.standardSpace))

                    RadioButton(
                        selected = state.type == HabitType.BAD.ordinal,
                        onClick = {
                            onAction(RedactorAction.OnRadioButtonClick(HabitType.BAD))
                        }
                    )
                    BaseText(
                        textStringId = R.string.bad_habit,
                        color = HabitTheme.colors.colorTextInField
                    )
                }
            }
        }
    }

    @Composable
    private fun SetupButtons(
        state: UiState,
        onAction: (RedactorAction) -> Unit
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Bottom,
            modifier = Modifier
                .fillMaxSize()
                .padding(HabitTheme.dimensions.paddingHuge)
        ) {
            BaseButton(
                textStringId = R.string.save_string,
                onClick = {
                    onAction(RedactorAction.OnSaveButtonClick)
                },
                modifier = Modifier
                    .fillMaxWidth(0.5f)
                    .padding(
                        end = HabitTheme.dimensions.paddingBig
                    )
            )
            BaseButton(
                textStringId = R.string.delete_string,
                isEnabled = state.id != null,
                onClick = {
                    onAction(RedactorAction.OnDeleteButtonClick)
                },
                modifier = Modifier
                    .padding(
                        start = HabitTheme.dimensions.paddingBig
                    )
            )
        }
    }

    override fun handleState(state: UiState) {

    }

    override fun handleEvent(event: RedactorEvents) {
        when (event) {
            is RedactorEvents.ShowValidationError -> {
                Toast.makeText(
                    requireContext(),
                    R.string.fill_the_line,
                    Toast.LENGTH_SHORT
                ).show()
            }

            is RedactorEvents.GoBack -> {
                findNavController().popBackStack()
            }
        }
    }

    private fun observeColorResult() {
        collectWithLifecycle(viewModel.state) { uiState ->
            viewModel.saveColor(uiState.color)
        }

        findNavController().currentBackStackEntry?.let { entry ->
            entry.savedStateHandle.getLiveData<HabitColor>(COLOR_KEY).observe(viewLifecycleOwner)
            { color ->
                viewModel.saveColor(color)
                entry.savedStateHandle.remove<HabitColor>(COLOR_KEY)
            }
        }
    }


    private fun colorDialog() {
        val action = RedactorFragmentDirections.actionHabitRedactorFragmentToColorChooseDialog()
        findNavController().navigate(action)
    }

    @Preview
    @Composable
    fun RedactorScreenPreviewWithoutId() {
        RedactorScreen(
            state = UiState(
                id = null,
                uid = null,
                title = "",
                description = "",
                color = HabitColor.ORANGE,
                priority = 3,
                selectedPriorityLocalized = stringResource(id = R.string.priority_choose),
                type = 0,
                frequency = "",
                count = 3,
                selectedCountLocalized = stringResource(id = R.string.count_choose_ui),
                doneDates = emptyList()
            ),
            onAction = {}
        )
    }

    @Preview
    @Composable
    fun RedactorScreenPreviewWithId() {
        RedactorScreen(
            state = UiState(
                id = "ggggg",
                uid = null,
                title = "",
                description = "",
                color = HabitColor.ORANGE,
                priority = 3,
                selectedPriorityLocalized = stringResource(id = R.string.priority_choose),
                type = 0,
                frequency = "",
                count = 3,
                selectedCountLocalized = stringResource(id = R.string.count_choose_ui),
                doneDates = emptyList()
            ),
            onAction = {}
        )
    }
}


