package com.example.newapppp.presentation.habit_list

import app.cash.turbine.test
import com.example.newapppp.domain.model.Filter
import com.example.newapppp.domain.model.Habit
import com.example.newapppp.domain.model.HabitColor
import com.example.newapppp.domain.model.HabitCount
import com.example.newapppp.domain.model.HabitPriority
import com.example.newapppp.domain.model.HabitType
import com.example.newapppp.domain.repository.FilterRepository
import com.example.newapppp.domain.usecase.DeleteHabitUseCase
import com.example.newapppp.domain.usecase.habit_list.GetCurrentDateUseCase
import com.example.newapppp.domain.usecase.habit_list.GetHabitListUseCase
import com.example.newapppp.domain.usecase.habit_list.GetHabitUpdatedMessageUseCase
import com.example.newapppp.domain.usecase.habit_list.UpdateHabitDatesUseCase
import com.example.newapppp.presentation.rule.UnconfinedDispatcherRule
import com.example.newapppp.presentation.habit_list.state.HabitState
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.`when`

internal class HabitListViewModelTest {

    private val deleteHabitUseCase = mock<DeleteHabitUseCase>()
    private val getHabitListUseCase = mock<GetHabitListUseCase>()
    private val getHabitUpdatedMessageUseCase = mock<GetHabitUpdatedMessageUseCase>()
    private val updateHabitDatesUseCase = mock<UpdateHabitDatesUseCase>()
    private val getCurrentDateUseCase = mock<GetCurrentDateUseCase>()
    private val filterRepository = mock<FilterRepository>()

    private lateinit var habitListViewModel: HabitListViewModel

    @get:Rule
    val unconfinedDispatcherRule = UnconfinedDispatcherRule()

    private fun initViewModel(filterFlow1: (Flow<Filter>) = flowOf()) {
        `when`(filterRepository.filterFlow).thenReturn(filterFlow1)
        habitListViewModel = HabitListViewModel(
            deleteHabitUseCase,
            getHabitListUseCase,
            getHabitUpdatedMessageUseCase,
            updateHabitDatesUseCase,
            getCurrentDateUseCase,
            filterRepository
        )
    }

    @Test
    fun `check initial habitState`() = runTest {
        // Given
        val expectedHabitState = HabitState.Loading

        // When
        initViewModel()
        habitListViewModel.habitState.test {
            val initialViewState = awaitItem()

            // Then
            assertEquals(expectedHabitState, initialViewState)
        }
    }

    @Test
    fun `check updated habitState when HabitState is success`() = runTest {
        // Given
        val habitList = flowOf(
            listOf(
                getMockHabit(uid = null, type = HabitType.GOOD),
                getMockHabit(uid = "uid2", type = HabitType.GOOD)
            )
        )
        val filter = Filter(
            filterByTitle = "",
            filterByPriority = HabitPriority.CHOOSE
        )
        val expectedHabitState = HabitState.Success(
            habitList = listOf(
                getMockHabit(uid = null, type = HabitType.GOOD),
                getMockHabit(uid = "uid2", type = HabitType.GOOD)
            ),
            filter = filter
        )
        val updatedFilter = Filter(
            filterByTitle = "",
            filterByPriority = HabitPriority.HIGH
        )
        val expectedHabitStateWithUpdatedFilter = HabitState.Success(
            habitList = listOf(
                getMockHabit(uid = null, type = HabitType.GOOD),
                getMockHabit(uid = "uid2", type = HabitType.GOOD)
            ),
            filter = updatedFilter
        )
        `when`(getHabitListUseCase(HabitType.GOOD)).thenReturn(habitList)
        `when`(filterRepository.valueFilter()).thenReturn(filter)

        // When
        initViewModel(filterFlow1 = flowOf(updatedFilter).onEach { delay(100) })
        habitListViewModel.habitState.test {
            // Skip initial habitState
            skipItems(1)

            habitListViewModel.getAndRefreshHabitList(HabitType.GOOD)
            val habitState = awaitItem()

            // Then
            assertEquals(expectedHabitState, habitState)

            delay(100)
            val habitStateWithUpdatedFilter = awaitItem()

            assertEquals(expectedHabitStateWithUpdatedFilter, habitStateWithUpdatedFilter)
        }
    }

    @Test
    fun `shouldn't update filters in habitState when HabitState is success`() = runTest {
        // Given
        val habitList = flowOf(
            listOf(
                getMockHabit(uid = null, type = HabitType.GOOD),
                getMockHabit(uid = "uid2", type = HabitType.GOOD)
            )
        )
        val filter = Filter(
            filterByTitle = "",
            filterByPriority = HabitPriority.CHOOSE
        )
        val updatedFilter = Filter(
            filterByTitle = "",
            filterByPriority = HabitPriority.HIGH
        )
        val expectedHabitState = HabitState.Success(
            habitList = listOf(
                getMockHabit(uid = null, type = HabitType.GOOD),
                getMockHabit(uid = "uid2", type = HabitType.GOOD)
            ),
            filter = updatedFilter
        )
        `when`(getHabitListUseCase(HabitType.GOOD)).thenReturn(habitList)
        `when`(filterRepository.valueFilter()).thenReturn(filter)

        // When
        initViewModel(filterFlow1 = flowOf(updatedFilter).onEach { delay(100) })
        habitListViewModel.habitState.test {
            // Skip initial habitState
            skipItems(1)

            habitListViewModel.getAndRefreshHabitList(HabitType.GOOD)
            skipItems(1)
            val habitState = awaitItem()
            // Then
            assertEquals(expectedHabitState, habitState)
        }
    }

    @Test
    fun `shouldn't update filters in habitState when HabitState is loading`() = runTest {
        // Given
        val updatedFilter = Filter(
            filterByTitle = "",
            filterByPriority = HabitPriority.HIGH
        )
        val expectedHabitState = HabitState.Loading

        // When
        initViewModel(flowOf(updatedFilter))
        habitListViewModel.habitState.test {
            val habitState = awaitItem()

            // Then
            assertEquals(expectedHabitState, habitState)
        }
    }

    private fun getMockHabit(
        uid: String?,
        type: HabitType,
        priority: HabitPriority = HabitPriority.LOW
    ): Habit {
        return Habit(
            id = "id",
            uid = uid,
            title = "title",
            description = "description",
            creationDate = 144424L,
            color = HabitColor.PINK,
            priority = priority,
            type = type,
            doneDates = listOf(19610L), // 10/9/2023
            count = HabitCount.WEEK,
            frequency = 3
        )
    }

}