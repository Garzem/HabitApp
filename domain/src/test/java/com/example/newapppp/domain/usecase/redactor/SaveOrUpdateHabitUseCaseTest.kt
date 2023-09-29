package com.example.newapppp.domain.usecase.redactor

import com.example.newapppp.domain.Constants
import com.example.newapppp.domain.model.HabitColor
import com.example.newapppp.domain.model.HabitCount
import com.example.newapppp.domain.model.HabitPriority
import com.example.newapppp.domain.model.HabitSave
import com.example.newapppp.domain.model.HabitType
import com.example.newapppp.domain.repository.HabitRepository
import kotlinx.coroutines.test.runTest
import org.junit.Test
import org.mockito.Mockito
import org.mockito.Mockito.mock

internal class SaveOrUpdateHabitUseCaseTest {

    private val habitRepository = mock<HabitRepository>()

    @Test
    fun putOfflineHabitListUseCase_invoke_putOfflineHabitListToServer() = runTest {

        val habitId = Constants.ID
        val habitSave = HabitSave(
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

        val useCase = SaveOrUpdateHabitUseCase(habitRepository = habitRepository)

        useCase(habitSave,habitId)

        Mockito.verify(habitRepository, Mockito.times(1)).saveOrUpdateHabit(habitSave,habitId)
    }
}