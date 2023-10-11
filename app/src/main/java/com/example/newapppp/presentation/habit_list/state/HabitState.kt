package com.example.newapppp.presentation.habit_list.state

import com.example.newapppp.abstracts.BaseState
import com.example.newapppp.domain.model.Filter
import com.example.newapppp.domain.model.Habit
import com.example.newapppp.domain.model.HabitPriority

sealed interface HabitState: BaseState {
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