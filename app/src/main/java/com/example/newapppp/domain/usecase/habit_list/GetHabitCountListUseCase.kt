package com.example.newapppp.domain.usecase.habit_list

import com.example.newapppp.domain.model.HabitCount
import com.example.newapppp.presentation.habit_list.HabitCountMapperRedactorFragment
import javax.inject.Inject

class GetHabitCountListUseCase @Inject constructor(
    private val habitCountMapperRedactorFragment: HabitCountMapperRedactorFragment
) {

    operator fun invoke(): List<String> {
        return HabitCount.values().map {
            habitCountMapperRedactorFragment.getCountName(it)
        }
    }
}