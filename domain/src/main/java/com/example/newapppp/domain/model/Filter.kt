package com.example.newapppp.domain.model

data class Filter(
    var filterByTitle: String,
    var filterByPriority: HabitPriority
) {
    val isFilterApplied: Boolean
        get() =
            filterByTitle.isNotBlank() || filterByPriority != HabitPriority.CHOOSE
}
