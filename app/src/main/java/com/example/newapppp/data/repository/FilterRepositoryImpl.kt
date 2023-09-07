package com.example.newapppp.data.repository

import com.example.newapppp.domain.model.Filter
import com.example.newapppp.domain.model.HabitPriority
import com.example.newapppp.domain.repository.FilterRepository
import kotlinx.coroutines.flow.MutableStateFlow

object FilterRepositoryImpl: FilterRepository {
    override val filterFlow = MutableStateFlow(
        Filter(
            filterByTitle = "",
            filterByPriority = HabitPriority.CHOOSE
        )
    )
}