package com.example.newapppp.data.repository

import com.example.newapppp.domain.model.Filter
import com.example.newapppp.domain.model.HabitPriority
import kotlinx.coroutines.flow.MutableStateFlow

object FilterRepositoryImpl {
    val filterFlow = MutableStateFlow(
        Filter(
            filterByTitle = "",
            filterByPriority = HabitPriority.CHOOSE
        )
    )
}