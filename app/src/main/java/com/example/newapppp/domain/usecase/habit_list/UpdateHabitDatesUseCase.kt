package com.example.newapppp.domain.usecase.habit_list

import com.example.newapppp.domain.model.Habit
import com.example.newapppp.domain.repository.HabitRepository
import javax.inject.Inject

class UpdateHabitDatesUseCase @Inject constructor(
    private val habitRepository: HabitRepository
) {
    suspend operator fun invoke(habitId: String, date: Long): Habit {
        return habitRepository.updateHabitDates(habitId, date)
    }
}