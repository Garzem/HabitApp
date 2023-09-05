package com.example.newapppp.domain.usecase.filter

import com.example.newapppp.data.database.habit_local.HabitPriority

class GetPriorityUseCase {
    fun execute(priorityInt: Int): HabitPriority {
        return HabitPriority.values()[priorityInt]
    }
}