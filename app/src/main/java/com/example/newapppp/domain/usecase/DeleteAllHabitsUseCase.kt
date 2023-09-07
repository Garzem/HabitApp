package com.example.newapppp.domain.usecase

import com.example.newapppp.domain.repository.HabitRepository
import javax.inject.Inject

class DeleteAllHabitsUseCase @Inject constructor(private val habitRepository: HabitRepository) {

    suspend operator fun invoke() {
        habitRepository.deleteAllHabits()
    }
}