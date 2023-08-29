package com.example.newapppp.data

import com.example.newapppp.data.habit_local.HabitPriority

data class Filter(
    var filterByTitle: String,
    var filterByPriority: HabitPriority
) {
    val isFilterApplied: Boolean
        get() =
            filterByTitle.isNotBlank() || filterByPriority != HabitPriority.CHOOSE
}
