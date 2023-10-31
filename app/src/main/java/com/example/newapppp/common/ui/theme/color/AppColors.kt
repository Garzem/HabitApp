package com.example.newapppp.common.ui.theme.color

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

val LocalAppColors = staticCompositionLocalOf { HabitColors }

class AppColors(
    background: Color,
    onBackground: Color,
    primary: Color,
    secondary: Color,
    onPrimary: Color,
    onSecondary: Color,
    onProfileBackground: Color,
    selected: Color,
    anotherTextColor: Color,
    borderStroke: Color,
    colorTextInField: Color
) {
    var background by mutableStateOf(background)
        private set
    var onBackground by mutableStateOf(onBackground)
        private set
    var primary by mutableStateOf(primary)
        private set
    var secondary by mutableStateOf(secondary)
        private set
    var onPrimary by mutableStateOf(onPrimary)
        private set
    var onProfileBackground by mutableStateOf(onProfileBackground)
        private set
    var onSecondary by mutableStateOf(onSecondary)
        private set
    var selected by mutableStateOf(selected)
        private set
    var anotherTextColor by mutableStateOf(anotherTextColor)
        private set
    var borderStroke by mutableStateOf(borderStroke)
        private set
    var colorTextInField by mutableStateOf(colorTextInField)
        private set

    fun copy(
        background: Color = this.background,
        onBackground: Color = this.onBackground,
        primary: Color = this.primary,
        secondary: Color = this.secondary,
        onPrimary: Color = this.onPrimary,
        onSecondary: Color = this.onPrimary,
        onProfileBackground: Color = this.onProfileBackground,
        selected: Color = this.selected,
        anotherTextColor: Color = this.anotherTextColor,
        borderStroke: Color = this.borderStroke,
        colorTextInField: Color = this.colorTextInField
    ) = AppColors(
        background = background,
        onBackground = onBackground,
        primary = primary,
        secondary = secondary,
        onPrimary = onPrimary,
        onSecondary = onSecondary,
        onProfileBackground = onProfileBackground,
        selected = selected,
        anotherTextColor = anotherTextColor,
        borderStroke = borderStroke,
        colorTextInField = colorTextInField
    )

    fun update(other: AppColors) {
        background = other.background
        onBackground = other.onBackground
        primary = other.primary
        secondary = other.secondary
        onPrimary = other.onPrimary
        onSecondary = other.onSecondary
        onProfileBackground = other.onProfileBackground
        selected = other.selected
        anotherTextColor = other.anotherTextColor
        borderStroke = other.borderStroke
        colorTextInField = other.colorTextInField
    }
}
