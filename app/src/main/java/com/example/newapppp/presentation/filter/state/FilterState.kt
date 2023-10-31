package com.example.newapppp.presentation.filter.state

import com.example.newapppp.presentation.abstracts.BaseState
import com.example.newapppp.domain.model.HabitPriority

data class FilterState(
    val selectedTitle: String,
    val selectedPriorityLocalized: String,
    val selectedPriority: HabitPriority
): BaseState {
    val isFilterApplied: Boolean
        get() =
            selectedTitle.isNotBlank() || selectedPriority != HabitPriority.CHOOSE
}
