package com.example.newapppp.domain.usecase.habit_list

import com.example.newapppp.data.database.habit_local.Habit
import com.example.newapppp.data.database.habit_local.HabitType
import com.example.newapppp.domain.repository.HabitRepository
import javax.inject.Inject

class GetLocalHabitListTypeUseCase @Inject constructor(private val habitRepository: HabitRepository) {

    suspend fun execute(type: HabitType): List<Habit> {
        return habitRepository.getHabitListByType(type)
    }
}