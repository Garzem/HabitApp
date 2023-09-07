package com.example.newapppp.domain.usecase.redactor

import com.example.newapppp.data.repository.HabitRepositoryImpl
import com.example.newapppp.data.remote.habit.HabitSave
import com.example.newapppp.domain.repository.HabitRepository
import javax.inject.Inject

class SaveOrUpdateHabitUseCase @Inject constructor(private val habitRepository: HabitRepository) {

    suspend operator fun invoke(saveHabit: HabitSave, habitId: String?) {
        habitRepository.saveOrUpdateHabit(saveHabit, habitId)
    }
}