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
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import net.bytebuddy.matcher.ElementMatchers.any
import org.junit.Before
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

    @Before
    fun setUp() {
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
    fun `returns initial habitState when initialize`() = runTest {
        // Given
        val expectedHabitState = HabitState.Loading

        // When
        habitListViewModel.habitState.test {
            val initialViewState = awaitItem()

            // Then
            assertEquals(expectedHabitState, initialViewState)
        }
    }

    @Test
    fun `returns updated habitState when habitState is success`() = runTest {
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
        `when`(getHabitListUseCase(HabitType.GOOD)).thenReturn(habitList)
        `when`(filterRepository.valueFilter()).thenReturn(filter)

        // When
        habitListViewModel.habitState.test {
            // Skip initial habitState
            skipItems(1)

            habitListViewModel.getAndRefreshHabitList(HabitType.GOOD)
            val habitState = awaitItem()

            // Then
            assertEquals(expectedHabitState, habitState)
        }
    }

    @Test
    fun `should update filters in habitState when filters were changed`() = runTest {
        // Given
        val updatedFilter = Filter(
            filterByTitle = "",
            filterByPriority = HabitPriority.HIGH
        )
        val expectedHabitState = HabitState.Success(
            habitList = emptyList(),
            filter = updatedFilter
        )

        `when`(filterRepository.filterFlow).thenReturn(flowOf(updatedFilter))

        // When
        habitListViewModel.habitState.test {
            // Skip initial habitState
            skipItems(1)

            filterRepository.updateFilter { filter ->
                filter.copy(
                    filterByPriority = HabitPriority.HIGH
                )
            }

            val habitState = awaitItem()

            // Then
            assertEquals(expectedHabitState, habitState)
        }
    }

    private fun getMockHabit(uid: String?, type: HabitType): Habit {
        return Habit(
            id = "id",
            uid = uid,
            title = "title",
            description = "description",
            creationDate = 144424L,
            color = HabitColor.PINK,
            priority = HabitPriority.LOW,
            type = type,
            doneDates = listOf(19610L), // 10/9/2023
            count = HabitCount.WEEK,
            frequency = 3
        )
    }

}