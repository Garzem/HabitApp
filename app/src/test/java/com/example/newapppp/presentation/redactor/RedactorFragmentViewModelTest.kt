package com.example.newapppp.presentation.redactor

import app.cash.turbine.test
import com.example.newapppp.domain.model.Habit
import com.example.newapppp.domain.model.HabitColor
import com.example.newapppp.domain.model.HabitCount
import com.example.newapppp.domain.model.HabitPriority
import com.example.newapppp.domain.model.HabitType
import com.example.newapppp.domain.usecase.DeleteHabitUseCase
import com.example.newapppp.domain.usecase.redactor.GetHabitByIdUseCase
import com.example.newapppp.domain.usecase.redactor.SaveOrUpdateHabitUseCase
import com.example.newapppp.presentation.habit_list.mapper.HabitCountMapper
import com.example.newapppp.presentation.habit_list.mapper.HabitPriorityMapper
import com.example.newapppp.presentation.redactor.state.UiState
import com.example.newapppp.presentation.rule.UnconfinedDispatcherRule
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.jupiter.api.Assertions.assertEquals
import org.mockito.Mockito.mock
import org.mockito.Mockito.times
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`

internal class RedactorFragmentViewModelTest {

    private val saveOrUpdateHabitUseCase = mock<SaveOrUpdateHabitUseCase>()
    private val getHabitByIdUseCase = mock<GetHabitByIdUseCase>()
    private val deleteHabitUseCase = mock<DeleteHabitUseCase>()
    private val habitCountMapper = mock<HabitCountMapper>()
    private val habitPriorityMapper = mock<HabitPriorityMapper>()

    private lateinit var redactorFragmentViewModel: RedactorFragmentViewModel

    @get:Rule
    val unconfinedDispatcherRule = UnconfinedDispatcherRule()

    @Before
    fun setUp() {
        redactorFragmentViewModel = RedactorFragmentViewModel(
            saveOrUpdateHabitUseCase,
            getHabitByIdUseCase,
            deleteHabitUseCase,
            habitCountMapper,
            habitPriorityMapper
        )
    }

    @Test
    fun `returns initial uiState when initialize`() = runTest {
        // Given
        val expectedUiState = getMockUiState()

        // When
        redactorFragmentViewModel.uiState.test {
            val initialUiState = awaitItem()

            // Then
            assertEquals(expectedUiState, initialUiState)
        }
    }

    @Test
    fun `setHabit should set values from all habit fields to uiState`() = runTest {
        // Given
        val habitId = "id"
        val habit = getMockHabit(id = "id")
        val expectedUiState = UiState(
            id = "id",
            uid = "uid",
            title = "title",
            titleCursorPosition = 0,
            description = "description",
            descriptionCursorPosition = 0,
            color = HabitColor.PINK,
            priority = 0,
            type = 0,
            frequency = "3",
            frequencyCursorPosition = 0,
            count = 0,
            doneDates = listOf(19610L), // 10/9/2023
        )
        `when`(getHabitByIdUseCase(habitId)).thenReturn(habit)

        // When
        redactorFragmentViewModel.uiState.test {
            skipItems(1)
            redactorFragmentViewModel.setHabit(habitId)
            val uiState = awaitItem()

            // Then
            verify(getHabitByIdUseCase, times(1)).invoke(habitId)
            assertEquals(expectedUiState, uiState)
        }
    }

    @Test
    fun `setHabit shouldn't set any values when habitId is null`() = runTest {
        // Given
        val habitId = null
        val expectedUiState = getMockUiState()

        // When
        redactorFragmentViewModel.uiState.test {

            redactorFragmentViewModel.setHabit(habitId)
            val uiState = awaitItem()

            // Then
            assertEquals(expectedUiState, uiState)
        }
    }

    @Test
    fun `setType should save type in uiState`() = runTest {
        // Given
        val typeBad = HabitType.BAD
        val expectedUiState = getMockUiState(type = 1)

        // When
        redactorFragmentViewModel.uiState.test {
            skipItems(1)
            redactorFragmentViewModel.setType(typeBad)
            val uiState = awaitItem()
            // Then
            assertEquals(expectedUiState, uiState)
        }
    }

    @Test
    fun `saveColor should save color in uiState`() = runTest {
        // Given
        val colorRed = HabitColor.RED
        val expectedUiState = getMockUiState(color = HabitColor.RED)

        // When
        redactorFragmentViewModel.uiState.test {
            skipItems(1)
            redactorFragmentViewModel.saveColor(colorRed)
            val uiState = awaitItem()
            // Then
            assertEquals(expectedUiState, uiState)
        }
    }

    @Test
    fun `onTitleChanged should save title and cursorPosition in uiState`() = runTest {
        // Given
        val title = "Hello"
        val cursorPosition = 4
        val expectedUiState = getMockUiState(
            title = "Hello",
            titleCursorPosition = 4
        )

        // When
        redactorFragmentViewModel.uiState.test {
            skipItems(1)
            redactorFragmentViewModel.onTitleChanged(title, cursorPosition)
            val uiState = awaitItem()
            // Then
            assertEquals(expectedUiState, uiState)
        }
    }

    @Test
    fun `onDescriptionChanged should save description and cursorPosition in uiState`() = runTest {
        // Given
        val description = "Hello"
        val cursorPosition = 4
        val expectedUiState = getMockUiState(
            description = "Hello",
            descriptionCursorPosition = 4
        )

        // When
        redactorFragmentViewModel.uiState.test {
            skipItems(1)
            redactorFragmentViewModel.onDescriptionChanged(description, cursorPosition)
            val uiState = awaitItem()
            // Then
            assertEquals(expectedUiState, uiState)
        }
    }

    @Test
    fun `onFrequencyChanged should save frequency and cursorPosition in uiState`() = runTest {
        // Given
        val frequency = "Hello"
        val cursorPosition = 4
        val expectedUiState = getMockUiState(
            frequency = "Hello",
            frequencyCursorPosition = 4
        )

        // When
        redactorFragmentViewModel.uiState.test {
            skipItems(1)
            redactorFragmentViewModel.onFrequencyChanged(frequency, cursorPosition)
            val uiState = awaitItem()
            // Then
            assertEquals(expectedUiState, uiState)
        }
    }

    @Test
    fun `onPrioritySelected should save priority and cursorPosition in uiState`() = runTest {
        // Given
        val priority = 2
        val expectedUiState = getMockUiState(priority = 2)

        // When
        redactorFragmentViewModel.uiState.test {
            skipItems(1)
            redactorFragmentViewModel.onPrioritySelected(priority)
            val uiState = awaitItem()
            // Then
            assertEquals(expectedUiState, uiState)
        }
    }

    @Test
    fun `onCountSelected should save count in uiState`() = runTest {
        // Given
        val count = 2
        val expectedUiState = getMockUiState(count = 2)

        // When
        redactorFragmentViewModel.uiState.test {
            skipItems(1)
            redactorFragmentViewModel.onCountSelected(count)
            val uiState = awaitItem()
            // Then
            assertEquals(expectedUiState, uiState)
        }
    }


    private fun getMockHabit(id: String): Habit {
        return Habit(
            id = id,
            uid = "uid",
            title = "title",
            description = "description",
            creationDate = 144424L,
            color = HabitColor.PINK,
            priority = HabitPriority.LOW,
            type = HabitType.GOOD,
            doneDates = listOf(19610L), // 10/9/2023
            count = HabitCount.WEEK,
            frequency = 3
        )
    }

    private fun getMockUiState(
        title: String = "",
        titleCursorPosition: Int = 0,
        description: String = "",
        descriptionCursorPosition: Int = 0,
        color: HabitColor = HabitColor.ORANGE,
        priority: Int = 3,
        type: Int = 0,
        frequency: String = "",
        frequencyCursorPosition: Int = 0,
        count: Int = 3,
        doneDates: List<Long> = emptyList()
    ): UiState {
        return UiState(
            id = null,
            uid = null,
            title = title,
            titleCursorPosition = titleCursorPosition,
            description = description,
            descriptionCursorPosition = descriptionCursorPosition,
            color = color,
            priority = priority,
            type = type,
            frequency = frequency,
            frequencyCursorPosition = frequencyCursorPosition,
            count = count,
            doneDates = doneDates
        )
    }
}