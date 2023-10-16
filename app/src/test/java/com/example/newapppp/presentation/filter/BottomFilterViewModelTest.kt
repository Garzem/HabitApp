package com.example.newapppp.presentation.filter

import app.cash.turbine.test
import com.example.newapppp.domain.model.Filter
import com.example.newapppp.domain.model.HabitPriority
import com.example.newapppp.domain.repository.FilterRepository
import com.example.newapppp.presentation.filter.state.FilterEvent
import com.example.newapppp.presentation.filter.state.FilterState
import com.example.newapppp.presentation.habit_list.mapper.HabitPriorityMapper
import com.example.newapppp.presentation.rule.UnconfinedDispatcherRule
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.time.delay
import org.junit.Rule
import org.junit.Test
import org.junit.jupiter.api.Assertions.assertEquals
import org.mockito.Mockito.doNothing
import org.mockito.Mockito.mock
import org.mockito.Mockito.times
import org.mockito.Mockito.`when`
import org.mockito.kotlin.anyOrNull
import org.mockito.kotlin.verify

internal class BottomFilterViewModelTest {

    private val habitPriorityMapper = mock<HabitPriorityMapper>()
    private val filterRepository = mock<FilterRepository>()

    private lateinit var bottomFilterViewModel: BottomFilterViewModel

    @get:Rule
    val unconfinedDispatcherRule = UnconfinedDispatcherRule()


    private fun initViewModel(filterFlow: Flow<Filter> = flowOf()) {
        `when`(filterRepository.filterFlow).thenReturn(filterFlow)
        bottomFilterViewModel = BottomFilterViewModel(habitPriorityMapper, filterRepository)
    }

    @Test
    fun `check initial filterState`() = runTest {
        // Given
        val expectedFilterState = FilterState(filter = null)

        // When
        initViewModel()
        bottomFilterViewModel.state.test {
            val initialFilterState = awaitItem()

            // Then
            assertEquals(expectedFilterState, initialFilterState)
        }
    }

    @Test
    fun `check filterState when filterFlow was changed`() = runTest {
        // Given
        val filter = Filter(
            filterByTitle = "title",
            filterByPriority = HabitPriority.HIGH
        )
        val expectedFilterState = FilterState(filter = filter)

        // When
        initViewModel(flowOf(filter).onEach { kotlinx.coroutines.delay(100) })
        bottomFilterViewModel.state.test {
            skipItems(1)
            val filterState = awaitItem()

            // Then
            assertEquals(expectedFilterState, filterState)
        }
    }

    @Test
    fun `check event go back when title is blank`() = runTest {
        // Given
        val expectedEvent = FilterEvent.GoBack
        doNothing().`when`(filterRepository).updateFilter(anyOrNull())

        // When
        initViewModel()
        with(bottomFilterViewModel) {
            bottomFilterViewModel.event.test {
                skipItems(1)
                onPriorityChanged(priorityInt = 2)
                onFilterClicked(title = "")
                val event = awaitItem()

                // Then
                verify(filterRepository, times(1)).updateFilter(anyOrNull())
                assertEquals(expectedEvent, event)
            }
        }
    }

    @Test
    fun `check event go back when priority is choose`() = runTest {
        // Given
        val expectedEvent = FilterEvent.GoBack
        doNothing().`when`(filterRepository).updateFilter(anyOrNull())

        // When
        initViewModel()
        with(bottomFilterViewModel) {
            bottomFilterViewModel.event.test {
                skipItems(1)
                onPriorityChanged(priorityInt = 3)
                onFilterClicked(title = "title")
                val event = awaitItem()

                // Then
                verify(filterRepository, times(1)).updateFilter(anyOrNull())
                assertEquals(expectedEvent, event)
            }
        }
    }

    @Test
    fun `check event go back`() = runTest {
        // Given
        val expectedEvent = FilterEvent.GoBack
        doNothing().`when`(filterRepository).updateFilter(anyOrNull())

        // When
        initViewModel()
        with(bottomFilterViewModel) {
            bottomFilterViewModel.event.test {
                skipItems(1)
                onPriorityChanged(priorityInt = 2)
                onFilterClicked(title = "title")
                val event = awaitItem()

                // Then
                verify(filterRepository, times(1)).updateFilter(anyOrNull())
                assertEquals(expectedEvent, event)
            }
        }
    }

    @Test
    fun `check event show error toast`() = runTest {
        // Given
        val expectedEvent = FilterEvent.ShowErrorToast

        // When
        initViewModel()
        with(bottomFilterViewModel) {
            bottomFilterViewModel.event.test {
                skipItems(1)
                onPriorityChanged(priorityInt = 3)
                onFilterClicked(title = "")
                val event = awaitItem()

                // Then
                assertEquals(expectedEvent, event)
            }
        }
    }

    // ???
    @Test
    fun `cancelFilter should reset filter to default values`() = runTest {
        // Given
        val expectedFilter = FilterState(
            filter = Filter(
                filterByTitle = "",
                filterByPriority = HabitPriority.CHOOSE
            )
        )
        doNothing().`when`(filterRepository).updateFilter(anyOrNull())

        // When
        initViewModel(flowOf(
            Filter(
                filterByTitle = "",
                filterByPriority = HabitPriority.CHOOSE
            )
                ).onEach { kotlinx.coroutines.delay(100) })
        bottomFilterViewModel.state.test {
            skipItems(1)
            bottomFilterViewModel.cancelFilter()
            val filterState = awaitItem()
            // Then
            verify(filterRepository, times(1)).updateFilter(anyOrNull())
            assertEquals(expectedFilter, filterState)
        }
    }
}