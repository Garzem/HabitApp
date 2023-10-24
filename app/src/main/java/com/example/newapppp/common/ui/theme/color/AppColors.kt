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
    onProfileBackground: Color
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
    var onSecondary by mutableStateOf(onSecondary)
        private set

    var onAvatarBackground by mutableStateOf(onProfileBackground)
        private set

    fun copy(
        background: Color = this.background,
        onBackground: Color = this.onBackground,
        primary: Color = this.primary,
        secondary: Color = this.secondary,
        onPrimary: Color = this.onPrimary,
        onSecondary: Color = this.onPrimary,
        onAvatarBackground: Color = this.onAvatarBackground
    ) = AppColors(
        background = background,
        onBackground = onBackground,
        primary = primary,
        secondary = secondary,
        onPrimary = onPrimary,
        onSecondary = onSecondary,
        onProfileBackground = onAvatarBackground
    )

    fun update(other: AppColors) {
        background = other.background
        onBackground = other.onBackground
        primary = other.primary
        secondary = other.secondary
        onPrimary = other.onPrimary
        onSecondary = other.onSecondary
    }
}
