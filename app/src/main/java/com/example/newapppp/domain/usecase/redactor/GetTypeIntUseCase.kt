package com.example.newapppp.domain.usecase.redactor

import com.example.newapppp.data.database.habit_local.HabitType

class GetTypeIntUseCase {

    fun execute(type: HabitType): Int {
        return HabitType.values().indexOf(type)
    }
}