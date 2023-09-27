package com.example.newapppp.domain.usecase.main_activity

import com.example.newapppp.domain.repository.HabitRepository
import javax.inject.Inject

class PutOfflineHabitListUseCase @Inject constructor(private val habitRepository: HabitRepository) {

    suspend operator fun invoke() {
        habitRepository.putOfflineHabitList()
    }
}