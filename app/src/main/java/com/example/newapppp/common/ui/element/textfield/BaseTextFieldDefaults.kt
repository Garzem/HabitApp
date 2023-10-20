package com.example.newapppp.common.ui.element.textfield

import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import com.example.newapppp.common.ui.theme.HabitTheme

object BaseTextFieldDefaults {

    val textFieldColors: TextFieldColors
        @Composable get() = TextFieldDefaults.colors(
            focusedTextColor = HabitTheme.colors.primary,
            unfocusedTextColor = HabitTheme.colors.primary
        )
}