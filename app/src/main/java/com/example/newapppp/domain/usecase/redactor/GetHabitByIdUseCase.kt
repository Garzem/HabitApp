package com.example.newapppp.domain.usecase.redactor

import com.example.newapppp.data.repository.HabitRepositoryImpl
import com.example.newapppp.data.database.habit_local.Habit
import com.example.newapppp.domain.repository.HabitRepository

class GetHabitByIdUseCase(private val habitRepository: HabitRepository) {

    suspend fun execute(id: String): Habit {
        return habitRepository.getHabitById(id)
    }
}