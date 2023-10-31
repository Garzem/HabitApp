package com.example.newapppp.common.ui.theme

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.LocalOverscrollConfiguration
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.remember
import com.example.newapppp.common.ui.theme.color.AppColors
import com.example.newapppp.common.ui.theme.color.HabitColors
import com.example.newapppp.common.ui.theme.color.LocalAppColors

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun HabitTheme(
    content: @Composable () -> Unit
) {
    val colors = HabitColors

    val rememberedColors = remember {
        colors.copy()
    }.apply {
        update(colors)
    }

    CompositionLocalProvider(
        LocalAppColors provides rememberedColors,
        LocalAppDimensions provides AppDimensions(),
        LocalAppTypography provides AppTypography(),
        content = content
    )
}

object HabitTheme {
    val colors: AppColors
        @Composable
        @ReadOnlyComposable
        get() = LocalAppColors.current
    val typography: AppTypography
        @Composable
        @ReadOnlyComposable
        get() = LocalAppTypography.current
    val dimensions: AppDimensions
        @Composable
        @ReadOnlyComposable
        get() = LocalAppDimensions.current
}