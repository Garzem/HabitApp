package com.example.newapppp.domain

import com.example.newapppp.data.database.habit_local.HabitPriority

data class Filter(
    var filterByTitle: String,
    var filterByPriority: HabitPriority
) {
    val isFilterApplied: Boolean
        get() =
            filterByTitle.isNotBlank() || filterByPriority != HabitPriority.CHOOSE
}
