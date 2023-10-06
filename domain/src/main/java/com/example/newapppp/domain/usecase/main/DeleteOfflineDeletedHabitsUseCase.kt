package com.example.newapppp.domain.usecase.main

import com.example.newapppp.domain.repository.HabitRepository
import javax.inject.Inject

class DeleteOfflineDeletedHabitsUseCase @Inject constructor(private val habitRepository: HabitRepository) {

    suspend operator fun invoke() {
        habitRepository.deleteOfflineDeletedHabits()
    }
}