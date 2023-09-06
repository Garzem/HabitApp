package com.example.newapppp.domain.usecase.habit_list

import com.example.newapppp.domain.model.Habit
import com.example.newapppp.domain.model.HabitType
import com.example.newapppp.domain.repository.HabitRepository
import javax.inject.Inject

class FetchHabitListUseCase @Inject constructor(
    private val habitRepository: HabitRepository
) {

    suspend operator fun invoke(habitType: HabitType): List<Habit> {
        return habitRepository.fetchHabitList().filter { habit ->
            habit.type == habitType
        }.sortedBy { habit ->
            habit.creationDate
        }
    }
}