package com.example.newapppp.common.ui.topbar

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.example.newapppp.common.ui.theme.HabitTheme

object BaseTopBarDefaults {
    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun topAppBarColors(
        containerColor: Color = HabitTheme.colors.primary,
        scrolledContainerColor: Color = HabitTheme.colors.primary,
        navigationIconContentColor: Color = HabitTheme.colors.onPrimary,
        titleContentColor: Color = HabitTheme.colors.onPrimary,
        actionIconContentColor: Color = HabitTheme.colors.onPrimary
    ) = TopAppBarDefaults.topAppBarColors(
        containerColor = containerColor,
        scrolledContainerColor = scrolledContainerColor,
        navigationIconContentColor = navigationIconContentColor,
        titleContentColor = titleContentColor,
        actionIconContentColor = actionIconContentColor
    )
}