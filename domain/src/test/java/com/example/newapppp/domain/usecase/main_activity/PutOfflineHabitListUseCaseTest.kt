package com.example.newapppp.domain.usecase.main_activity

import com.example.newapppp.domain.repository.HabitRepository
import kotlinx.coroutines.test.runTest
import org.junit.Test
import org.mockito.Mockito
import org.mockito.Mockito.mock

internal class PutOfflineHabitListUseCaseTest {

    private val habitRepository = mock<HabitRepository>()

    @Test
    fun putOfflineHabitListUseCase_invoke_putOfflineHabitListToServer() = runTest {
        val useCase = PutOfflineHabitListUseCase(habitRepository = habitRepository)

        useCase()

        Mockito.verify(habitRepository, Mockito.times(1)).putOfflineHabitList()
    }
}