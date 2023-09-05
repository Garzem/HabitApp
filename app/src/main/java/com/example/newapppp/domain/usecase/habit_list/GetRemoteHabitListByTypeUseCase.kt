package com.example.newapppp.domain.usecase.habit_list

import com.example.newapppp.data.database.habit_local.Habit
import com.example.newapppp.data.database.habit_local.HabitType
import com.example.newapppp.domain.repository.HabitRepository
import javax.inject.Inject

class GetRemoteHabitListByTypeUseCase @Inject constructor(private val habitRepository: HabitRepository) {

    suspend fun execute(habitType: HabitType): List<Habit> {
        val habitListRemote = habitRepository.getHabitList()
        val filteredByType = habitListRemote.filter { habit ->
            habit.type == habitType
        }
        return filteredByType
    }
}