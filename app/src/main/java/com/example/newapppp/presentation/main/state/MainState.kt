package com.example.newapppp.presentation.main.state

import com.example.newapppp.presentation.abstracts.BaseState
import com.example.newapppp.presentation.main.NavItem

data class MainState(
    val connected: Boolean,
    val navItem: NavItem?
): BaseState
