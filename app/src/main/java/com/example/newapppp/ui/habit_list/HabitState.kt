package com.example.newapppp.ui.habit_list

import com.example.newapppp.data.Habit
import com.example.newapppp.data.HabitPriority
import com.example.newapppp.data.HabitType

data class HabitState(
    val allHabits: List<Habit>,
    val filterByType: HabitType?,
    val filterByTitle: String,
    val filterByPriority: HabitPriority,
) {
    val isFilterApplied: Boolean = filterByTitle.isNotBlank() || filterByPriority != HabitPriority.CHOOSE
    val filteredHabits = allHabits.filter { habit ->
        val matchType = habit.type == filterByType
        val matchTitle = habit.title.contains(filterByTitle, ignoreCase = true)
        val matchPriority = habit.priority == filterByPriority
                && habit.priority != HabitPriority.CHOOSE
        matchType && (matchPriority || matchTitle)
    }
}