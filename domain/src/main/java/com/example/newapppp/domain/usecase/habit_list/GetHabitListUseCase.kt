package com.example.newapppp.domain.usecase.habit_list

import com.example.newapppp.domain.model.Habit
import com.example.newapppp.domain.model.HabitType
import com.example.newapppp.domain.repository.HabitRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetHabitListUseCase @Inject constructor(
    private val habitRepository: HabitRepository
) {

    operator fun invoke(habitType: HabitType): Flow<List<Habit>> {
        return habitRepository.getHabitListFlow().map { habitList ->
            habitList.filter { habit ->
                habit.type == habitType
            }.sortedBy { habit ->
                habit.creationDate
            }
        }
    }
}