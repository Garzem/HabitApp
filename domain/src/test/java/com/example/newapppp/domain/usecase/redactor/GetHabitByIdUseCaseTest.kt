package com.example.newapppp.domain.usecase.redactor

import com.example.newapppp.domain.Constants.ID
import com.example.newapppp.domain.model.Habit
import com.example.newapppp.domain.model.HabitColor
import com.example.newapppp.domain.model.HabitCount
import com.example.newapppp.domain.model.HabitPriority
import com.example.newapppp.domain.model.HabitType
import com.example.newapppp.domain.repository.HabitRepository
import kotlinx.coroutines.test.runTest
import org.junit.Test
import org.junit.jupiter.api.Assertions
import org.mockito.Mockito
import org.mockito.Mockito.mock

internal class GetHabitByIdUseCaseTest {

    private val habitRepository = mock<HabitRepository>()

    @Test
    fun getHabitByIdUseCase_invoke_returnHabit() = runTest {

        val habitId = ID

        val habit = Habit(
            id = habitId,
            uid = "your_uid",
            title = "Your Habit",
            description = "Your Habit Description",
            creationDate = 12345L,
            color = HabitColor.RED,
            priority = HabitPriority.HIGH,
            type = HabitType.BAD,
            count = HabitCount.WEEK,
            doneDates = listOf(1111L, 2222L),
            frequency = 1
        )

        Mockito.`when`(habitRepository.getHabitById(habitId)).thenReturn(habit)

        val useCase = GetHabitByIdUseCase(habitRepository = habitRepository)
        val actual = useCase(habitId)

        val expected = Habit(
            id = habitId,
            uid = "your_uid",
            title = "Your Habit",
            description = "Your Habit Description",
            creationDate = 12345L,
            color = HabitColor.RED,
            priority = HabitPriority.HIGH,
            type = HabitType.BAD,
            count = HabitCount.WEEK,
            doneDates = listOf(1111L, 2222L),
            frequency = 1
        )

        Assertions.assertEquals(expected, actual)
    }
}