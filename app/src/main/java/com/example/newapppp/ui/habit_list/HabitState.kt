package com.example.newapppp.ui.habit_list

import com.example.newapppp.data.Habit
import com.example.newapppp.data.HabitPriority
import com.example.newapppp.habit_repository.FilterRepository


data class HabitState(
    val habitList: List<Habit>,
    val filters: FilterRepository
) {
    val isFilterApplied: Boolean
        get() =
            filters.filterByTitle.isNotBlank() || filters.filterByPriority != HabitPriority.CHOOSE

    val filteredHabits
        get() =
            if (isFilterApplied) {
                habitList.filter { habit ->
                    val matchTitle =
                        if (filters.filterByTitle.isNotBlank()) {
                            habit.title.contains(filters.filterByTitle, ignoreCase = true)
                        } else {
                            false
                        }
                    val matchPriority = habit.priority == filters.filterByPriority
                            && habit.priority != HabitPriority.CHOOSE
                    matchPriority || matchTitle
                }
            } else {
                habitList
            }
}