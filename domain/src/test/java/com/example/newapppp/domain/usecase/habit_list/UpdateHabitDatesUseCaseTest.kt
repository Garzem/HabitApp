package com.example.newapppp.domain.usecase.habit_list

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

internal class UpdateHabitDatesUseCaseTest {

    private val habitRepository = mock<HabitRepository>()

    @Test
    fun updateHabitDatesUseCase_invoke_returnHabitWithUpdatedDoneDates() = runTest {

        val habitId = ID
        val date = 14234L
        val existingDates = listOf(12345L, 67890L)
        val updatedHabit = Habit(
            id = habitId,
            uid = "your_uid",
            title = "Your Habit",
            description = "Your Habit Description",
            creationDate = 12345L,
            color = HabitColor.RED,
            priority = HabitPriority.HIGH,
            type = HabitType.BAD,
            count = HabitCount.WEEK,
            doneDates = existingDates + date,
            frequency = 1
        )

        Mockito.`when`(habitRepository.updateHabitDates(habitId, date)).thenReturn(updatedHabit)

        val useCase = UpdateHabitDatesUseCase(habitRepository = habitRepository)
        val actual = useCase(habitId, date)

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
            doneDates = existingDates + date,
            frequency = 1
        )

        Assertions.assertEquals(expected, actual)
    }

}