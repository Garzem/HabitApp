package com.example.newapppp.presentation.util

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import com.example.newapppp.common.ui.theme.HabitTheme

inline fun ComposeView.setContentWithTheme(crossinline content: @Composable () -> Unit) {
    setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
    setContent {
        HabitTheme {
            content()
        }
    }
}