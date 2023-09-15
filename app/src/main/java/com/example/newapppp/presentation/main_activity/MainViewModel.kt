package com.example.newapppp.presentation.main_activity

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.newapppp.domain.INetworkUtil
import com.example.newapppp.domain.usecase.habit_list.FetchHabitListUseCase
import com.example.newapppp.domain.usecase.main_activity.DeleteOfflineDeletedHabitsUseCase
import com.example.newapppp.domain.usecase.main_activity.PutOfflineHabitListUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val networkUtil: INetworkUtil,
    private val deleteOfflineDeletedHabitsUseCase: DeleteOfflineDeletedHabitsUseCase,
    private val putOfflineHabitListUseCase: PutOfflineHabitListUseCase,
    private val fetchHabitListUseCase: FetchHabitListUseCase
) : ViewModel() {

    private val _connectionState = MutableStateFlow(false)

    val connectionState: StateFlow<Boolean> = _connectionState.asStateFlow()

    init {
        observeNetworkConnection()
    }

    private fun observeNetworkConnection() {
        networkUtil.observeIsOnline().onEach { isOnline ->
            _connectionState.update { isOnline }
        }.launchIn(viewModelScope)
    }

    suspend fun makeRequestForDataVerification() {
        deleteOfflineDeletedHabitsUseCase()
        putOfflineHabitListUseCase()
        fetchHabitListUseCase()
    }
}