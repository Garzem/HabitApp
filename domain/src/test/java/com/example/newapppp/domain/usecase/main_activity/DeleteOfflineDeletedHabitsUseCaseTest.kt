package com.example.newapppp.domain.usecase.main_activity

import com.example.newapppp.domain.repository.HabitRepository
import kotlinx.coroutines.test.runTest
import org.junit.Test
import org.mockito.Mockito
import org.mockito.Mockito.mock

internal class DeleteOfflineDeletedHabitsUseCaseTest {

    private val habitRepository = mock<HabitRepository>()

    @Test
    fun deleteOfflineDeletedHabits_invoke_deleteHabitsWithTrueDeletedField() = runTest {
        val useCase = DeleteOfflineDeletedHabitsUseCase(habitRepository = habitRepository)

        useCase()

        Mockito.verify(habitRepository, Mockito.times(1)).deleteOfflineDeletedHabits()
    }
}