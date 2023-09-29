package com.example.newapppp.domain.usecase.redactor

import com.example.newapppp.domain.model.HabitSave
import com.example.newapppp.domain.repository.HabitRepository
import javax.inject.Inject

class SaveOrUpdateHabitUseCase @Inject constructor(private val habitRepository: HabitRepository) {

    suspend operator fun invoke(habitSave: HabitSave, habitId: String?) {
        habitRepository.saveOrUpdateHabit(habitSave, habitId)
    }
}