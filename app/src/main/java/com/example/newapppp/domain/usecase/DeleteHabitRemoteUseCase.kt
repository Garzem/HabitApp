package com.example.newapppp.domain.usecase

import com.example.newapppp.data.repository.HabitRepositoryImpl
import com.example.newapppp.data.database.habit_local.Habit
import com.example.newapppp.domain.repository.HabitRepository
import javax.inject.Inject

class DeleteHabitRemoteUseCase @Inject constructor(private val habitRepository: HabitRepository) {

    suspend fun execute(habit: Habit) {
        habitRepository.deleteHabit(habit)
    }
}