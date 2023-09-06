package com.example.newapppp.domain.usecase.redactor

import com.example.newapppp.data.repository.HabitRepositoryImpl
import com.example.newapppp.data.remote.habit.HabitSave
import com.example.newapppp.domain.repository.HabitRepository

class SaveOrUpdateHabitUseCase(private val habitRepository: HabitRepository) {

    suspend operator fun invoke(saveHabit: HabitSave, habitId: String?) {
        habitRepository.saveOrUpdateHabit(saveHabit, habitId)
    }
}