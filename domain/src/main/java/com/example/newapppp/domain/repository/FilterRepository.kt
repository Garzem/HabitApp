package com.example.newapppp.domain.repository

import com.example.newapppp.domain.model.Filter
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

interface FilterRepository {

    val filterFlow: Flow<Filter>

    fun updateFilter(updateFunction: (Filter) -> Filter)

    fun valueFilter(): Filter

}