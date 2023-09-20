package com.example.newapppp.domain.usecase.done_dates

import com.example.newapppp.domain.model.Habit
import com.example.newapppp.domain.repository.HabitRepository
import javax.inject.Inject

class SaveOrUpdateSelectedDatesUseCase @Inject constructor(
    private val habitRepository: HabitRepository
) {
    suspend operator fun invoke(habit: Habit) {
        habitRepository.saveOrUpdateSelectedDates(habit)
    }
}