package com.example.newapppp.presentation.main

import app.cash.turbine.test
import com.example.newapppp.domain.INetworkUtil
import com.example.newapppp.domain.model.Filter
import com.example.newapppp.domain.usecase.main.DeleteOfflineDeletedHabitsUseCase
import com.example.newapppp.domain.usecase.main.FetchHabitListUseCase
import com.example.newapppp.domain.usecase.main.PostOfflineHabitListUseCase
import com.example.newapppp.domain.usecase.main.PutOfflineHabitListUseCase
import com.example.newapppp.presentation.rule.UnconfinedDispatcherRule
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import net.bytebuddy.implementation.InvokeDynamic.lambda
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.mockito.Mockito.doAnswer
import org.mockito.Mockito.mock
import org.mockito.Mockito.`when`

internal class MainViewModelTest {

    private val networkUtil = mock<INetworkUtil>()
    private val deleteOfflineDeletedHabitsUseCase = mock<DeleteOfflineDeletedHabitsUseCase>()
    private val putOfflineHabitListUseCase = mock<PutOfflineHabitListUseCase>()
    private val fetchHabitListUseCase = mock<FetchHabitListUseCase>()
    private val postOfflineHabitListUseCase = mock<PostOfflineHabitListUseCase>()

    private lateinit var mainViewModel: MainViewModel

    @get:Rule
    val unconfinedDispatcherRule = UnconfinedDispatcherRule()

    private fun initViewModel(filterFlow: (Flow<Filter>) = flowOf()) {

        mainViewModel = MainViewModel(
            networkUtil,
            deleteOfflineDeletedHabitsUseCase,
            putOfflineHabitListUseCase,
            fetchHabitListUseCase,
            postOfflineHabitListUseCase
        )
    }

    @Test
    fun `returns initial connectionState when initialize`() = runTest {
        // Given
        `when`(networkUtil.observeIsOnline()).thenReturn(flow { emit(false) })

        // When
        mainViewModel.connectionState.test {
            val initialConnectionState = awaitItem()

            // Then
            assertFalse(initialConnectionState)
        }
    }

    @Test
    fun `returns true for connectionState`() = runTest {
        // Given
        `when`(networkUtil.observeIsOnline()).thenReturn(flow { emit(true) })

        // When
        mainViewModel.connectionState.test {
            skipItems(1)
            val connectionState = awaitItem()

            // Then
            assertTrue(connectionState)
        }
    }
}