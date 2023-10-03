package com.example.newapppp.domain.usecase.habit_list

import com.example.newapppp.domain.DateHelper
import com.example.newapppp.domain.model.Habit
import com.example.newapppp.domain.model.HabitColor
import com.example.newapppp.domain.model.HabitCount
import com.example.newapppp.domain.model.HabitPriority
import com.example.newapppp.domain.model.HabitType
import com.example.newapppp.domain.model.Message
import org.junit.Before
import org.junit.Test
import org.junit.jupiter.api.Assertions.assertEquals
import org.mockito.Mockito.mock
import org.mockito.Mockito.`when`
import java.time.LocalDate

internal class GetHabitUpdatedMessageUseCaseTest {

    private val dateHelper = mock<DateHelper>()
    private lateinit var getHabitUpdatedMessageUseCase: GetHabitUpdatedMessageUseCase

    private val currentDate = LocalDate.of(2023, 9, 30).toEpochDay()
    private val periodStart = LocalDate.of(2023, 9, 1).toEpochDay()


    @Before
    fun setUp() {
        getHabitUpdatedMessageUseCase = GetHabitUpdatedMessageUseCase(dateHelper)
    }

    @Test
    fun `return message about remaining repetition amounts for bad habit`() {
        // Given
        val habit = getMockHabit(HabitType.BAD, 2)
        val expected = Message.TimesLeftForBadHabit(1)
        `when`(dateHelper.getStartDate(currentDate, habit.count)).thenReturn(periodStart)

        // When
        val actual = getHabitUpdatedMessageUseCase(habit, currentDate)

        // Then
        assertEquals(expected, actual)
    }

    @Test
    fun `return message about remaining repetition amounts for good habit`() {
        // Given
        val habit = getMockHabit(HabitType.GOOD, 3)
        val expected = Message.TimesLeftForGoodHabit(2)
        `when`(dateHelper.getStartDate(currentDate, habit.count)).thenReturn(periodStart)

        // When
        val actual = getHabitUpdatedMessageUseCase(habit, currentDate)

        // Then
        assertEquals(expected, actual)
    }

    @Test
    fun `return message about completed bad habit`() {
        // Given
        val habit = getMockHabit(HabitType.BAD, 1)
        val expected = Message.StopDoingBadHabit
        `when`(dateHelper.getStartDate(currentDate, habit.count)).thenReturn(periodStart)

        // When
        val actual = getHabitUpdatedMessageUseCase(habit, currentDate)

        // Then
        assertEquals(expected, actual)
    }

    @Test
    fun `return message about completed good habit`() {
        // Given
        val habit = getMockHabit(HabitType.GOOD, 1)
        val expected = Message.MetOrExceededGoodHabit
        `when`(dateHelper.getStartDate(currentDate, habit.count)).thenReturn(periodStart)

        // When
        val actual = getHabitUpdatedMessageUseCase(habit, currentDate)

        // Then
        assertEquals(expected, actual)
    }

    @Test
    fun `return error message`() {
        // Given
        val habit = getMockHabit(HabitType.GOOD, -1)
        val expected = Message.Error
        `when`(dateHelper.getStartDate(currentDate, habit.count)).thenReturn(periodStart)

        // When
        val actual = getHabitUpdatedMessageUseCase(habit, currentDate)

        // Then
        assertEquals(expected, actual)
    }

    private fun getMockHabit(type: HabitType, frequency: Int): Habit {
        return Habit(
            id = "id",
            uid = "uid",
            title = "title",
            description = "description",
            creationDate = 144424L,
            color = HabitColor.BLUE,
            priority = HabitPriority.HIGH,
            type = type,
            doneDates = listOf(LocalDate.of(2023, 9, 10).toEpochDay()),
            count = HabitCount.MONTH,
            frequency = frequency
        )
    }
}