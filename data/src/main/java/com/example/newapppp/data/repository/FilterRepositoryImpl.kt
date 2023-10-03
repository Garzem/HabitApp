package com.example.newapppp.data.repository

import com.example.newapppp.domain.model.Filter
import com.example.newapppp.domain.model.HabitPriority
import com.example.newapppp.domain.repository.FilterRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

class FilterRepositoryImpl @Inject constructor() : FilterRepository {
    override val filterFlow = MutableStateFlow(
        Filter(
            filterByTitle = "",
            filterByPriority = HabitPriority.CHOOSE
        )
    )

    override fun updateFilter(updateFunction: (Filter) -> Filter) {
        filterFlow.value = updateFunction(filterFlow.value)
    }

    override fun valueFilter(): Filter {
        return filterFlow.value
    }
}