package com.example.newapppp.data.repository

import com.example.newapppp.domain.Filter
import com.example.newapppp.data.database.habit_local.HabitPriority
import kotlinx.coroutines.flow.MutableStateFlow

object FilterRepositoryImpl {
    val filterFlow = MutableStateFlow(
        Filter(
            filterByTitle = "",
            filterByPriority = HabitPriority.CHOOSE
        )
    )
}