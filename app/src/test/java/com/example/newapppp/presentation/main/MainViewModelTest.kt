package com.example.newapppp.presentation.main

import app.cash.turbine.test
import com.example.newapppp.domain.INetworkUtil
import com.example.newapppp.domain.usecase.main.DeleteOfflineDeletedHabitsUseCase
import com.example.newapppp.domain.usecase.main.FetchHabitListUseCase
import com.example.newapppp.domain.usecase.main.PostOfflineHabitListUseCase
import com.example.newapppp.domain.usecase.main.PutOfflineHabitListUseCase
import com.example.newapppp.presentation.rule.UnconfinedDispatcherRule
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.Test
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
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

    private fun initViewModel(isOnlineFlow: Flow<Boolean> = flowOf()) {
        `when`(networkUtil.observeIsOnline()).thenReturn(isOnlineFlow)
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
        // When
        initViewModel()
        mainViewModel.state.test {
            val initialMainState = awaitItem()

            // Then
            assertFalse(initialMainState.connected)
        }
    }

    @Test
    fun `returns true for connectionState`() = runTest {
        // When
        initViewModel(flowOf(true).onEach { delay(100) })
        mainViewModel.state.test {
            skipItems(1)
            val mainState = awaitItem()

            // Then
            assertTrue(mainState.connected)
        }
    }
}