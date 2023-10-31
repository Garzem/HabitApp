package com.example.newapppp.presentation.habit_list.mapper

import androidx.compose.ui.graphics.Color
import com.example.newapppp.common.ui.theme.color.HabitAppColors
import com.example.newapppp.domain.model.HabitColor
import javax.inject.Inject

class HabitColorMapper @Inject constructor() {

    fun getColor(habitColor: HabitColor): Color {
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