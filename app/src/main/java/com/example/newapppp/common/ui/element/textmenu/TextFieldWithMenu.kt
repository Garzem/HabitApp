package com.example.newapppp.common.ui.element.textmenu

import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.example.newapppp.R
import com.example.newapppp.common.ui.element.textfield.BaseTextField
import com.example.newapppp.common.ui.theme.HabitTheme
import com.example.newapppp.presentation.abstracts.Suggestion

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TextFieldWithMenu(
    modifier: Modifier = Modifier,
    expanded: Boolean,
    onExpandedChange: (Boolean) -> Unit,
    value: String = "",
    @StringRes labelStringId: Int = R.string.empty_string,
    suggestionsList: List<Suggestion> = emptyList(),
    onSuggestionClick: (suggestion: Suggestion) -> Unit,
    leadingIcon: @Composable (() -> Unit)? = null,
    color: Color = HabitTheme.colors.colorTextInField
) {
    Column(modifier = modifier) {
        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = {
                onExpandedChange(it)
            }
        ) {
            BaseTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .menuAnchor(),
                value = value,
                labelStringId = labelStringId,
                onValueChange = {},
                leadingIcon = leadingIcon
            )

            if (suggestionsList.isNotEmpty()) {
                ExposedDropdownMenu(
                    modifier = Modifier
                        .background(HabitTheme.colors.background)
                        .exposedDropdownSize(),
                    expanded = expanded,
                    onDismissRequest = {
                        onExpandedChange(false)
                    }
                ) {
                    suggestionsList.forEach { suggestion ->
                        DropdownMenuItem(
                            text = {
                                Text(
                                    text = suggestion.value,
                                    color = color,
                                    style = HabitTheme.typography.bodyMedium
                                )
                            },
                            onClick = {
                                onSuggestionClick(suggestion)
                            }
                        )
                    }
                }
            }
        }
    }
}