package com.example.newapppp.common.ui.screen

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.example.newapppp.common.ui.theme.HabitTheme
import com.example.newapppp.common.ui.topbar.BaseTopBar

@Composable
fun BaseDrawerScreen(
    @StringRes titleId: Int,
    containerColor: Color = HabitTheme.colors.background,
    onMenuActionClick: () -> Unit,
    content: @Composable () -> Unit
) {
    Scaffold(
        topBar = {
            BaseTopBar(
                titleId = titleId,
                openMenuActionClick = {
                    onMenuActionClick()
                }
            )
        },
        containerColor = containerColor
    ) { padding ->
        Box(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
        ) {
            content()
        }
    }
}