package com.example.newapppp.domain.usecase.redactor

import com.example.newapppp.domain.model.Habit
import com.example.newapppp.domain.repository.HabitRepository

class GetHabitByIdUseCase(private val habitRepository: HabitRepository) {

    suspend operator fun invoke(id: String): Habit {
        return habitRepository.getHabitById(id)
    }
}