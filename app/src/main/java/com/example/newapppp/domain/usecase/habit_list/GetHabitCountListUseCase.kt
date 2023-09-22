package com.example.newapppp.domain.usecase.habit_list

import com.example.newapppp.domain.model.HabitCount
import com.example.newapppp.presentation.habit_list.HabitCountMapper
import javax.inject.Inject

class GetHabitCountListUseCase @Inject constructor(
    private val habitCountMapper: HabitCountMapper
) {

    operator fun invoke(): List<String> {
        return HabitCount.values().map {
            habitCountMapper.getCountName(it)
        }
    }
}