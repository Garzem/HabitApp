package com.example.newapppp.common.ui.element.button

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ButtonElevation
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.dp
import com.example.newapppp.common.ui.theme.HabitTheme
import com.example.newapppp.common.ui.theme.color.HabitAppColors
import com.example.newapppp.domain.model.HabitColor

object BaseButtonDefaults {

    private val buttonElevation: ButtonElevation
        @Composable get() = ButtonDefaults.buttonElevation(2.dp)

    private val zeroButtonElevation: ButtonElevation
        @Composable get() = ButtonDefaults.buttonElevation(0.dp)

    val mainButtonColors: ButtonColors
        @Composable get() = ButtonDefaults.buttonColors(
            containerColor = HabitTheme.colors.primary,
            contentColor = HabitTheme.colors.onPrimary
        )

    val secondaryButtonColors: ButtonColors
        @Composable get() = ButtonDefaults.buttonColors(
            containerColor = HabitTheme.colors.secondary,
            contentColor = HabitTheme.colors.onSecondary
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

    @Composable
    fun chooseColorButton(color: HabitColor = HabitColor.ORANGE): ButtonColors =
        ButtonDefaults.buttonColors(
            containerColor = getColor(color),
            contentColor = HabitTheme.colors.secondary
        )

    private fun getColor(habitColor: HabitColor): Color {
        return when(habitColor) {
            HabitColor.PINK -> HabitAppColors.Pink
            HabitColor.RED -> HabitAppColors.Red
            HabitColor.DEEPORANGE -> HabitAppColors.OrangeRed
            HabitColor.ORANGE -> HabitAppColors.Orange
            HabitColor.AMBER -> HabitAppColors.Saffron
            HabitColor.YELLOW -> HabitAppColors.Amber
            HabitColor.LIME -> HabitAppColors.LawnGreen
            HabitColor.LIGHTGREEN -> HabitAppColors.Mantis
            HabitColor.GREEN -> HabitAppColors.Emerald
            HabitColor.TEAL -> HabitAppColors.Aqua
            HabitColor.CYAN -> HabitAppColors.BlueLight
            HabitColor.LIGHTBLUE -> HabitAppColors.Blue
            HabitColor.BLUE -> HabitAppColors.BlueDark1
            HabitColor.DARKBLUE -> HabitAppColors.BlueDark2
            HabitColor.PURPLE -> HabitAppColors.Purple
            HabitColor.DEEPPURPLE -> HabitAppColors.PurpleDeep
            HabitColor.DEEPPURPLEDARK -> HabitAppColors.PurpleDark
        }
    }
}