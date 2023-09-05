package com.example.newapppp.domain.usecase.habit_list

import com.example.newapppp.data.repository.HabitRepositoryImpl
import com.example.newapppp.data.database.habit_local.Habit
import com.example.newapppp.domain.repository.HabitRepository
import javax.inject.Inject

class GetLocalHabitListUseCase @Inject constructor(private val habitRepository: HabitRepository) {

    suspend fun execute(habit: Habit): List<Habit> {
        return habitRepository.getHabitListByType(habit.type)
    }
}