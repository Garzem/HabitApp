package com.example.newapppp.domain.usecase.main_activity

import com.example.newapppp.domain.model.Habit
import com.example.newapppp.domain.repository.HabitRepository
import javax.inject.Inject

class PostOfflineHabitUseCase @Inject constructor(
    private val habitRepository: HabitRepository
) {
    suspend operator fun invoke() {
        habitRepository.postOfflineHabit()
    }
}