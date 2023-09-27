package com.example.newapppp.domain.usecase

import com.example.newapppp.domain.model.Habit
import com.example.newapppp.domain.repository.HabitRepository
import javax.inject.Inject

class DeleteHabitUseCase @Inject constructor(private val habitRepository: HabitRepository) {

    suspend operator fun invoke(habit: Habit) {
        habitRepository.deleteHabit(habit)
    }
}