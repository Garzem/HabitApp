package com.example.newapppp.common.ui.element.buttons

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ButtonElevation
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.dp
import com.example.newapppp.common.ui.theme.HabitTheme

object BaseButtonDefaults {

    private val buttonElevation: ButtonElevation
        @Composable get() = ButtonDefaults.buttonElevation(2.dp)

    private val zeroButtonElevation: ButtonElevation
        @Composable get() = ButtonDefaults.buttonElevation(0.dp)

    val mainButtonColors: ButtonColors
        @Composable get() = ButtonDefaults.buttonColors(
            containerColor = HabitTheme.colors.primary,
            contentColor = HabitTheme.colors.onPrimary,
//            disabledContainerColor = AdminTheme.colors.main.disabled,
//            disabledContentColor = AdminTheme.colors.main.onDisabled
        )

    val secondaryButtonColors: ButtonColors
        @Composable get() = ButtonDefaults.buttonColors(
            containerColor = HabitTheme.colors.secondary,
            contentColor = HabitTheme.colors.onSecondary,
//            disabledContainerColor = AdminTheme.colors.main.disabled,
//            disabledContentColor = AdminTheme.colors.main.onDisabled
        )
    private val buttonShape: RoundedCornerShape
        @Composable get() = RoundedCornerShape(HabitTheme.dimensions.buttonRadius)

    private val boxShape: RoundedCornerShape
        @Composable get() = RoundedCornerShape(HabitTheme.dimensions.boxRadius)

    @Composable
    fun getButtonShape(rounded: Boolean): Shape = if (rounded) {
        buttonShape
    } else {
        boxShape
    }

    @Composable
    fun getButtonElevation(elevated: Boolean): ButtonElevation = if (elevated) {
        buttonElevation
    } else {
        zeroButtonElevation
    }
}