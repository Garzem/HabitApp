package com.example.newapppp.data.habit_repository

import com.example.newapppp.domain.Filter
import com.example.newapppp.domain.habit_local.HabitPriority
import kotlinx.coroutines.flow.MutableStateFlow

object FilterRepository {
    val filterFlow = MutableStateFlow(
        Filter(
            filterByTitle = "",
            filterByPriority = HabitPriority.CHOOSE
        )
    )
}