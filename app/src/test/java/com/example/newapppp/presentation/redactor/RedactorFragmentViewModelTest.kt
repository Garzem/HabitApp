package com.example.newapppp.presentation.redactor

import android.provider.Settings
import app.cash.turbine.test
import app.cash.turbine.turbineScope
import com.example.newapppp.abstracts.RedactorEvents
import com.example.newapppp.domain.model.Habit
import com.example.newapppp.domain.model.HabitColor
import com.example.newapppp.domain.model.HabitCount
import com.example.newapppp.domain.model.HabitPriority
import com.example.newapppp.domain.model.HabitSave
import com.example.newapppp.domain.model.HabitType
import com.example.newapppp.domain.usecase.DeleteHabitUseCase
import com.example.newapppp.domain.usecase.redactor.GetHabitByIdUseCase
import com.example.newapppp.domain.usecase.redactor.SaveOrUpdateHabitUseCase
import com.example.newapppp.presentation.habit_list.mapper.HabitCountMapper
import com.example.newapppp.presentation.habit_list.mapper.HabitPriorityMapper
import com.example.newapppp.presentation.redactor.state.UiState
import com.example.newapppp.presentation.rule.UnconfinedDispatcherRule
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.stateIn
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
    fun `check initial uiState when initialize`() = runTest {
        // Given
        val expectedUiState = getMockUiState()

        // When
        redactorFragmentViewModel.state.test {
            val initialUiState = awaitItem()

            // Then
            assertEquals(expectedUiState, initialUiState)
        }
    }

    @Test
    fun `check uiState when set habit with habitId`() = runTest {
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
        redactorFragmentViewModel.state.test {
            skipItems(1)
            redactorFragmentViewModel.setHabit(habitId)
            val uiState = awaitItem()

            // Then
            assertEquals(expectedUiState, uiState)
        }
    }

    @Test
    fun `check uiState when set habit with null habitId`() = runTest {
        // Given
        val habitId = null
        val expectedUiState = getMockUiState()

        // When
        redactorFragmentViewModel.state.test {

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
        redactorFragmentViewModel.state.test {
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
        redactorFragmentViewModel.state.test {
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
        val cursorPosition = 5
        val expectedUiState = getMockUiState(
            title = "Hello",
            titleCursorPosition = 5
        )

        // When
        redactorFragmentViewModel.state.test {
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
        redactorFragmentViewModel.state.test {
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
        redactorFragmentViewModel.state.test {
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
        redactorFragmentViewModel.state.test {
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
        redactorFragmentViewModel.state.test {
            skipItems(1)
            redactorFragmentViewModel.onCountSelected(count)
            val uiState = awaitItem()
            // Then
            assertEquals(expectedUiState, uiState)
        }
    }

    @Test
    fun `check deleteHabit when uiState with id`() = runTest {
        // Given
        val habit = getMockHabit(id = "id")
        val habitId = "id"
        val expectedEvent = RedactorEvents.GoBack
        `when`(getHabitByIdUseCase(habitId)).thenReturn(habit)
        `when`(deleteHabitUseCase(habit)).thenReturn(Unit)

        // When
        turbineScope {
            val stateTurbine = redactorFragmentViewModel.state.testIn(backgroundScope)
            val eventTurbine = redactorFragmentViewModel.event.testIn(backgroundScope)
            stateTurbine.skipItems(1)
            eventTurbine.skipItems(1)
            redactorFragmentViewModel.setHabit(habitId)
            redactorFragmentViewModel.deleteHabit()
            val event = eventTurbine.awaitItem()
            // Then
            verify(deleteHabitUseCase, times(1)).invoke(habit)
            assertEquals(expectedEvent, event)
        }
    }

    @Test
    fun `should delete habit with automatically created creation date`() = runTest {
        // Given
        val habit = getMockHabit(
            id = "id",
            priority = HabitPriority.HIGH,
            count = HabitCount.MONTH,
            doneDates = emptyList()
        )
        val expectedCreationDate = 144424L
        val expectedEvent = RedactorEvents.GoBack
        `when`(redactorFragmentViewModel.state.value).thenReturn(
            UiState(
                id = "id",
                uid = "uid",
                color = HabitColor.PINK,
                description = "description",
                frequency = "3",
                priority = 1,
                title = "title",
                type = 1,
                count = 1,
                doneDates = emptyList(),
                descriptionCursorPosition = 1,
                titleCursorPosition = 1,
                frequencyCursorPosition = 1
            )
        )
        `when`(System.currentTimeMillis()).thenReturn(14444L)
        `when`(deleteHabitUseCase(habit)).thenReturn(Unit)

        // When
        with(redactorFragmentViewModel) {
            event.test {
                skipItems(1)
                redactorFragmentViewModel.deleteHabit()
                val event = awaitItem()

                // Then
                assertEquals(expectedCreationDate, )
                verify(deleteHabitUseCase, times(1)).invoke(habit)
                assertEquals(expectedEvent, event)
            }
        }
    }


    // ??
//    @Test
//    fun `check deleteHabit when uiState is null`() = runTest {
//        // Given
//        val habitId = null
//        val expectedEvent = null
//
//        // When
//        redactorFragmentViewModel.event.test {
//            redactorFragmentViewModel.setHabit(habitId)
//            redactorFragmentViewModel.deleteHabit()
//            val event = awaitItem()
//            // Then
//            assertEquals(expectedEvent, event)
//        }
//    }

    @Test
    fun `should save habit`() = runTest {
        // Given
        val habitSave = getMockHabitSave()
        val habitId = "id"
        val habit = getMockHabit(id = "id")
        val expectedEvent = RedactorEvents.GoBack
        `when`(saveOrUpdateHabitUseCase(habitSave, habitId)).thenReturn(Unit)
        `when`(getHabitByIdUseCase(habitId)).thenReturn(habit)

        // When
        redactorFragmentViewModel.event.test {
            skipItems(1)

            redactorFragmentViewModel.setHabit(habitId)
            redactorFragmentViewModel.saveClick()
            val event = awaitItem()

            // Then
            assertEquals(expectedEvent, event)
        }
    }

    @Test
    fun `shouldn't save habit when title is blank`() = runTest {
        // Given
        val habitId = "id"
        val habit = getMockHabit(id = "id", title = "")
        val expectedEvent = RedactorEvents.ShowValidationError
        `when`(getHabitByIdUseCase(habitId)).thenReturn(habit)

        // When
        redactorFragmentViewModel.event.test {
            skipItems(1)

            redactorFragmentViewModel.setHabit(habitId)
            redactorFragmentViewModel.saveClick()
            val event = awaitItem()

            // Then
            assertEquals(expectedEvent, event)
        }
    }

    @Test
    fun `shouldn't save habit when description is blank`() = runTest {
        // Given
        val habitId = "id"
        val habit = getMockHabit(id = "id", description = "")
        val expectedEvent = RedactorEvents.ShowValidationError
        `when`(getHabitByIdUseCase(habitId)).thenReturn(habit)

        // When
        redactorFragmentViewModel.event.test {
            skipItems(1)

            redactorFragmentViewModel.setHabit(habitId)
            redactorFragmentViewModel.saveClick()
            val event = awaitItem()

            // Then
            assertEquals(expectedEvent, event)
        }
    }

    @Test
    fun `shouldn't save habit when priority is choose`() = runTest {
        // Given
        val habitId = "id"
        val habit = getMockHabit(id = "id", priority = HabitPriority.CHOOSE)
        val expectedEvent = RedactorEvents.ShowValidationError
        `when`(getHabitByIdUseCase(habitId)).thenReturn(habit)

        // When
        redactorFragmentViewModel.event.test {
            skipItems(1)

            redactorFragmentViewModel.setHabit(habitId)
            redactorFragmentViewModel.saveClick()
            val event = awaitItem()

            // Then
            assertEquals(expectedEvent, event)
        }
    }

    @Test
    fun `shouldn't save habit when frequency is 0`() = runTest {
        // Given
        val habitId = "id"
        val habit = getMockHabit(id = "id", frequency = 0)
        val expectedEvent = RedactorEvents.ShowValidationError
        `when`(getHabitByIdUseCase(habitId)).thenReturn(habit)

        // When
        redactorFragmentViewModel.event.test {
            skipItems(1)

            redactorFragmentViewModel.setHabit(habitId)
            redactorFragmentViewModel.saveClick()
            val event = awaitItem()

            // Then
            assertEquals(expectedEvent, event)
        }
    }

    @Test
    fun `shouldn't save habit when frequency is blank`() = runTest {
        // Given
        val habitId = "id"
        val habit = getMockHabit(id = "id", frequency = 0)
        val expectedEvent = RedactorEvents.ShowValidationError
        `when`(getHabitByIdUseCase(habitId)).thenReturn(habit)

        // When
        redactorFragmentViewModel.event.test {
            skipItems(1)

            redactorFragmentViewModel.setHabit(habitId)
            redactorFragmentViewModel.onFrequencyChanged(frequency = "", cursorPosition = 0)
            redactorFragmentViewModel.saveClick()
            val event = awaitItem()

            // Then
            assertEquals(expectedEvent, event)
        }
    }

    @Test
    fun `shouldn't save habit when count is choose`() = runTest {
        // Given
        val habitId = "id"
        val habit = getMockHabit(id = "id", count = HabitCount.CHOOSE)
        val expectedEvent = RedactorEvents.ShowValidationError
        `when`(getHabitByIdUseCase(habitId)).thenReturn(habit)

        // When
        redactorFragmentViewModel.event.test {
            skipItems(1)

            redactorFragmentViewModel.setHabit(habitId)
            redactorFragmentViewModel.saveClick()
            val event = awaitItem()

            // Then
            assertEquals(expectedEvent, event)
        }
    }


    private fun getMockHabitSave(): HabitSave {
        return HabitSave(
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


    private fun getMockHabit(
        id: String,
        title: String = "title",
        description: String = "description",
        priority: HabitPriority = HabitPriority.LOW,
        frequency: Int = 3,
        count: HabitCount = HabitCount.WEEK,
        doneDates: List<Long> = listOf(19610L) // 10/9/2023
    ): Habit {
        return Habit(
            id = id,
            uid = "uid",
            title = title,
            description = description,
            creationDate = 144424L,
            color = HabitColor.PINK,
            priority = priority,
            type = HabitType.GOOD,
            doneDates = doneDates,
            count = count,
            frequency = frequency
        )
    }

    private fun getMockUiState(
        id: String? = null,
        uid: String? = null,
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
            id = id,
            uid = uid,
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