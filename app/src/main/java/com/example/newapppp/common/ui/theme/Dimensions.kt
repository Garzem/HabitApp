package com.example.newapppp.common.ui.theme

import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

internal val LocalAppDimensions = staticCompositionLocalOf { AppDimensions() }

data class AppDimensions(
    val paddingSmall: Dp = 4.dp,
    val paddingNormal: Dp = 8.dp,
    val paddingBig: Dp = 16.dp,
    val paddingLarge: Dp = 24.dp,
    val paddingHuge: Dp = 32.dp,
    val colorButtonWidth: Dp = 50.dp,
    val colorButtonWidthSmall: Dp = 40.dp,
    val buttonRadius: Dp = 5.dp,
    val boxRadius: Dp = 0.dp,
    val bottomSheetRadius: Dp = 16.dp,
    val profile: Dp = 80.dp,
    val spacer: Dp = 16.dp,
    val navigationHeight: Dp = 48.dp,
    val stroke: Dp = 1.dp,
    val viewRoundedCorner: Dp = 4.dp,
    val standardSpace: Dp = 16.dp
)