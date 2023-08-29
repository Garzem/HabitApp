package com.example.newapppp.data.habit_local

import com.example.newapppp.R

enum class HabitColor {
    PINK,
    RED,
    DEEPORANGE,
    ORANGE,
    AMBER,
    YELLOW,
    LIME,
    LIGHTGREEN,
    GREEN,
    TEAL,
    CYAN,
    LIGHTBLUE,
    BLUE,
    DARKBLUE,
    PURPLE,
    DEEPPURPLE,
    DEEPPURPLEDARK;

    fun getBackGroundResId(): Int {
        return when(this) {
            PINK -> R.drawable.color_button_pink
            RED -> R.drawable.color_button_red
            DEEPORANGE -> R.drawable.color_button_deep_orange
            ORANGE -> R.drawable.color_button_orange
            AMBER -> R.drawable.color_button_amber
            YELLOW -> R.drawable.color_button_yellow
            LIME -> R.drawable.color_button_lime
            LIGHTGREEN -> R.drawable.color_button_light_green
            GREEN -> R.drawable.color_button_green
            TEAL -> R.drawable.color_button_teal
            CYAN -> R.drawable.color_button_cyan
            LIGHTBLUE -> R.drawable.color_button_light_blue
            BLUE -> R.drawable.color_button_blue
            DARKBLUE -> R.drawable.color_button_dark_blue
            PURPLE -> R.drawable.color_button_purple
            DEEPPURPLE -> R.drawable.color_button_deep_purple
            DEEPPURPLEDARK -> R.drawable.color_button_deep_purple_dark
        }
    }
}