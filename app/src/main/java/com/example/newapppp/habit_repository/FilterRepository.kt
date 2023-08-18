package com.example.newapppp.habit_repository

import com.example.newapppp.data.Filter
import com.example.newapppp.data.HabitPriority
import kotlinx.coroutines.flow.MutableStateFlow

object FilterRepository {
    val filter = MutableStateFlow(
        Filter(
            filterByTitle = "",
            filterByPriority = HabitPriority.CHOOSE
        )
    )
}