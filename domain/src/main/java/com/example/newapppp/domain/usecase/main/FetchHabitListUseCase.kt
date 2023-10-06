package com.example.newapppp.domain.usecase.main

import com.example.newapppp.domain.repository.HabitRepository
import javax.inject.Inject

class FetchHabitListUseCase @Inject constructor(
    private val habitRepository: HabitRepository
) {
    suspend operator fun invoke() {
        habitRepository.fetchHabitList()
    }
}