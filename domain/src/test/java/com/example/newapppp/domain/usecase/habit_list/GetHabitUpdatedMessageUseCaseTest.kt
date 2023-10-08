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

internal class GetHabitUpdatedMessageUseCaseTest {

    private val dateHelper = mock<DateHelper>()
    private lateinit var getHabitUpdatedMessageUseCase: GetHabitUpdatedMessageUseCase

    private val currentDate = 19630L // 30/9/2023

    @Before
    fun setUp() {
        getHabitUpdatedMessageUseCase = GetHabitUpdatedMessageUseCase(dateHelper)
    }

    @Test
    fun `should return message about remaining repetition amounts for bad habit`() {
        // Given
        val habit = getMockHabit(
            type = HabitType.BAD,
            frequency = 2,
            count = HabitCount.WEEK,
            doneDates = listOf(19630L)
            )
        val periodStart = 19625L // 25/9/2023
        val expected = Message.TimesLeftForBadHabit(1)
        `when`(dateHelper.getStartDate(currentDate, habit.count)).thenReturn(periodStart)

        // When
        val actual = getHabitUpdatedMessageUseCase(habit, currentDate)

        // Then
        assertEquals(expected, actual)
    }

    @Test
    fun `should then return message about remaining repetition amounts for good habit`() {
        // Given
        val habit = getMockHabit(
            type = HabitType.GOOD,
            frequency = 3,
            count = HabitCount.MONTH,
            doneDates = listOf(19630L)
        )
        val periodStart = 19601L // 1/9/2023
        val expected = Message.TimesLeftForGoodHabit(2)
        `when`(dateHelper.getStartDate(currentDate, habit.count)).thenReturn(periodStart)

        // When
        val actual = getHabitUpdatedMessageUseCase(habit, currentDate)

        // Then
        assertEquals(expected, actual)
    }

    @Test
    fun `should return message about completed bad habit`() {
        // Given
        val habit = getMockHabit(
            type = HabitType.BAD,
            frequency = 1,
            count = HabitCount.YEAR,
            doneDates = listOf(19630L)
        )
        val periodStart = 19358L // 1/1/2023
        val expected = Message.StopDoingBadHabit
        `when`(dateHelper.getStartDate(currentDate, habit.count)).thenReturn(periodStart)

        // When
        val actual = getHabitUpdatedMessageUseCase(habit, currentDate)

        // Then
        assertEquals(expected, actual)
    }

    @Test
    fun `should return message about completed good habit`() {
        // Given
        val habit = getMockHabit(
            type = HabitType.GOOD,
            frequency = 1,
            count = HabitCount.WEEK,
            doneDates = listOf(19630L)
        )
        val periodStart = 19625L // 25/9/2023
        val expected = Message.MetOrExceededGoodHabit
        `when`(dateHelper.getStartDate(currentDate, habit.count)).thenReturn(periodStart)

        // When
        val actual = getHabitUpdatedMessageUseCase(habit, currentDate)

        // Then
        assertEquals(expected, actual)
    }

    @Test
    fun `incorrect frequency should return error message`() {
        // Given
        val habit = getMockHabit(
            type = HabitType.GOOD,
            frequency = -1,
            count = HabitCount.MONTH,
            doneDates = listOf(19630L)
        )
        val periodStart = 19601L // 1/9/2023
        val expected = Message.Error
        `when`(dateHelper.getStartDate(currentDate, habit.count)).thenReturn(periodStart)

        // When
        val actual = getHabitUpdatedMessageUseCase(habit, currentDate)

        // Then
        assertEquals(expected, actual)
    }

    private fun getMockHabit(
        type: HabitType,
        frequency: Int,
        count: HabitCount,
        doneDates: List<Long> = emptyList()
    ): Habit {
        return Habit(
            id = "id",
            uid = "uid",
            title = "title",
            description = "description",
            creationDate = 144424L,
            color = HabitColor.BLUE,
            priority = HabitPriority.HIGH,
            type = type,
            doneDates = doneDates,
            count = count,
            frequency = frequency
        )
    }
}