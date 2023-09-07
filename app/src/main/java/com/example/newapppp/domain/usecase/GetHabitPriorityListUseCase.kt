package com.example.newapppp.domain.usecase

import com.example.newapppp.domain.model.HabitPriority
import com.example.newapppp.presentation.habit_list.HabitPriorityMapper
import dagger.hilt.android.scopes.ViewModelScoped
import javax.inject.Inject

@ViewModelScoped
class GetHabitPriorityListUseCase @Inject constructor(
    private val habitPriorityMapper: HabitPriorityMapper
) {
    operator fun invoke(): List<String> {
        return HabitPriority.values().map {
            habitPriorityMapper.getPriorityName(it)
        }
    }
}