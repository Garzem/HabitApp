package com.example.newapppp.data.repository

import com.example.newapppp.domain.model.Filter
import com.example.newapppp.domain.model.Habit
import com.example.newapppp.domain.model.HabitPriority
import com.example.newapppp.domain.model.HabitType
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.junit.jupiter.api.Assertions.assertEquals
import org.mockito.Mockito.`when`

internal class FilterRepositoryImplTest {

    private lateinit var filterRepository: FilterRepositoryImpl

    @Before
    fun setUp() {
        filterRepository = FilterRepositoryImpl()
    }

    @Test
    fun `initial filterFlow value should match default Filter`() = runTest {
        // Given
        val expectedFilter = Filter(
            filterByTitle = "",
            filterByPriority = HabitPriority.CHOOSE
        )

        // When
        val actual = filterRepository.filterFlow.first()

        // Then
        assertEquals(expectedFilter, actual)
    }

    @Test
    fun `updateFilter should update Filter correctly`() = runTest {
        // Given
        val initialFilter  = Filter(
            filterByTitle = "",
            filterByPriority = HabitPriority.CHOOSE
        )
        val expectedFilter = Filter(
            filterByTitle = "Updated Filter",
            filterByPriority = HabitPriority.HIGH
        )


        // When
        filterRepository.updateFilter {
            initialFilter.copy(
                filterByTitle = "Updated Filter",
                filterByPriority = HabitPriority.HIGH,
            )
        }
        val actual = filterRepository.filterFlow.first()

        // Then
        assertEquals(expectedFilter, actual)
    }

    @Test
    fun `value should return correct value of filterFlow`() = runTest {
        // Given
        val expectedFilter = Filter(
            filterByTitle = "",
            filterByPriority = HabitPriority.CHOOSE
        )


        // When
        val actual = filterRepository.filterFlow.value

        // Then
        assertEquals(expectedFilter, actual)
    }
}