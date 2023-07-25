package com.example.newapppp.data

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

    fun getColorRedId(): Int {
        return when(this) {
            //???почему я получаю не тот int?
            PINK -> R.color.colorPink
            RED -> R.color.colorRed
            DEEPORANGE -> R.color.colorDeepOrange
            ORANGE -> R.color.colorOrange
            AMBER -> R.color.colorAmber
            YELLOW -> R.color.colorYellow
            LIME -> R.color.colorLime
            LIGHTGREEN -> R.color.colorLightGreen
            GREEN -> R.color.colorGreen
            TEAL -> R.color.colorTeal
            CYAN -> R.color.colorCyan
            LIGHTBLUE -> R.color.colorLightBlue
            BLUE -> R.color.colorBlue
            DARKBLUE -> R.color.colorDarkBlue
            PURPLE -> R.color.colorPurple
            DEEPPURPLE -> R.color.colorDeepPurple
            DEEPPURPLEDARK -> R.color.colorDeepPurpleDark
        }
    }
}