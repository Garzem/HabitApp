package com.example.newapppp.common.ui.element.drawer

import androidx.compose.material3.DrawerState
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.runtime.Composable
import com.example.newapppp.presentation.main.NavItem

@Composable
fun BaseDrawer(
    drawerState: DrawerState,
    drawerMenu: @Composable () -> Unit,
    content: @Composable () -> Unit
) {
    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            drawerMenu()
        }
    ) {
        content()
    }
}