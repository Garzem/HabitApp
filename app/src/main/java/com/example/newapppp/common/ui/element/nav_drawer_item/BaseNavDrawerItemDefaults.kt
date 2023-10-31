package com.example.newapppp.common.ui.element.nav_drawer_item

import androidx.compose.material3.NavigationDrawerItemColors
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.runtime.Composable
import com.example.newapppp.common.ui.theme.HabitTheme

object BaseNavDrawerItemDefaults {

    val navDrawerColors: NavigationDrawerItemColors
        @Composable get() = NavigationDrawerItemDefaults.colors(
            selectedContainerColor = HabitTheme.colors.selected,
            unselectedContainerColor = HabitTheme.colors.background,
        )
}