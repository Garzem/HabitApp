package com.example.newapppp.domain.usecase.redactor

import com.example.newapppp.data.database.habit_local.HabitPriority

class GetPriorityIntUseCase {

    fun execute(priority: HabitPriority): Int {
        return HabitPriority.values().indexOf(priority)
    }
}