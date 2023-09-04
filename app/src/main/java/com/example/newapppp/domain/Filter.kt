package com.example.newapppp.domain

import com.example.newapppp.domain.habit_local.HabitPriority

data class Filter(
    var filterByTitle: String,
    var filterByPriority: HabitPriority
) {
    val isFilterApplied: Boolean
        get() =
            filterByTitle.isNotBlank() || filterByPriority != HabitPriority.CHOOSE
}