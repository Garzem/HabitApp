package com.example.newapppp.presentation.filter

import app.cash.turbine.test
import com.example.newapppp.domain.model.Filter
import com.example.newapppp.domain.model.HabitPriority
import com.example.newapppp.domain.repository.FilterRepository
import com.example.newapppp.presentation.habit_list.mapper.HabitPriorityMapper
import com.example.newapppp.presentation.rule.UnconfinedDispatcherRule
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.jupiter.api.Assertions.assertEquals
import org.mockito.Mockito.any
import org.mockito.Mockito.doNothing
import org.mockito.Mockito.mock
import org.mockito.Mockito.`when`
import org.mockito.kotlin.anyOrNull

internal class BottomFilterViewModelTest {

    private val habitPriorityMapper = mock<HabitPriorityMapper>()
    private val filterRepository = mock<FilterRepository>()

    private lateinit var bottomFilterViewModel: BottomFilterViewModel

    @get:Rule
    val unconfinedDispatcherRule = UnconfinedDispatcherRule()

    @Before
    fun setUp() {
        bottomFilterViewModel = BottomFilterViewModel(habitPriorityMapper, filterRepository)
    }

    @Test
    fun `returns initial filterState when initialize`() = runTest {
        // When
        bottomFilterViewModel.filterState.test {
            val initialViewState = awaitItem()

            // Then
            assertEquals(null, initialViewState)
        }
    }

    @Test
    fun `cancelFilter should reset filter to default values`() = runTest {
        // Given
        val expectedFilter = Filter(
            filterByTitle = "",
            filterByPriority = HabitPriority.CHOOSE
        )
        doNothing().`when`(filterRepository).updateFilter(anyOrNull())

        // When
        bottomFilterViewModel.filterState.test {
            skipItems(1)
            bottomFilterViewModel.cancelFilter()
            val filterState = awaitItem()
            // Then
            assertEquals(expectedFilter, filterState)
        }
    }
}