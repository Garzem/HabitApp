package com.example.newapppp.presentation.habit_list

import com.example.newapppp.R
import com.example.newapppp.domain.model.HabitColor
import javax.inject.Inject

class HabitColorMapper @Inject constructor() {

    fun getBackGroundResId(color: HabitColor): Int {
        return when(color) {
            HabitColor.PINK -> R.drawable.color_button_pink
            HabitColor.RED -> R.drawable.color_button_red
            HabitColor.DEEPORANGE -> R.drawable.color_button_deep_orange
            HabitColor.ORANGE -> R.drawable.color_button_orange
            HabitColor.AMBER -> R.drawable.color_button_amber
            HabitColor.YELLOW -> R.drawable.color_button_yellow
            HabitColor.LIME -> R.drawable.color_button_lime
            HabitColor.LIGHTGREEN -> R.drawable.color_button_light_green
            HabitColor.GREEN -> R.drawable.color_button_green
            HabitColor.TEAL -> R.drawable.color_button_teal
            HabitColor.CYAN -> R.drawable.color_button_cyan
            HabitColor.LIGHTBLUE -> R.drawable.color_button_light_blue
            HabitColor.BLUE -> R.drawable.color_button_blue
            HabitColor.DARKBLUE -> R.drawable.color_button_dark_blue
            HabitColor.PURPLE -> R.drawable.color_button_purple
            HabitColor.DEEPPURPLE -> R.drawable.color_button_deep_purple
            HabitColor.DEEPPURPLEDARK -> R.drawable.color_button_deep_purple_dark
        }
    }
}