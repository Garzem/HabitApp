package com.example.newapppp.ui.filter

import com.example.newapppp.data.HabitType

data class FilterState(
    val title: String?,
    val priority: Int,
    val type: HabitType
)