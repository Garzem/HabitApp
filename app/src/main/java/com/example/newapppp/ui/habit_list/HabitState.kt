package com.example.newapppp.ui.habit_list

import com.example.newapppp.data.Filter
import com.example.newapppp.data.habit_local.Habit
import com.example.newapppp.data.habit_local.HabitPriority

sealed interface HabitState {
    data class Success(
        val habitList: List<Habit>,
        val filter: Filter,
    ): HabitState {
        val filteredHabits
            get() =
                if (filter.isFilterApplied) {
                    habitList.filter { habit ->
                        val matchTitle =
                            if (filter.filterByTitle.isNotBlank()) {
                                habit.title.contains(filter.filterByTitle, ignoreCase = true)
                            } else {
                                false
                            }
                        val matchPriority = habit.priority == filter.filterByPriority
                                && habit.priority != HabitPriority.CHOOSE
                        matchPriority || matchTitle
                    }
                } else {
                    habitList
                }
    }
    object Loading: HabitState
}