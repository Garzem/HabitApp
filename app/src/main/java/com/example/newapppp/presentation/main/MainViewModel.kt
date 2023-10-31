package com.example.newapppp.presentation.main

import androidx.lifecycle.viewModelScope
import com.example.newapppp.presentation.abstracts.BaseViewModel
import com.example.newapppp.domain.INetworkUtil
import com.example.newapppp.domain.usecase.main.FetchHabitListUseCase
import com.example.newapppp.domain.usecase.main.DeleteOfflineDeletedHabitsUseCase
import com.example.newapppp.domain.usecase.main.PostOfflineHabitListUseCase
import com.example.newapppp.domain.usecase.main.PutOfflineHabitListUseCase
import com.example.newapppp.presentation.main.state.MainState
import com.example.newapppp.presentation.main.state.MainEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val networkUtil: INetworkUtil,
    private val deleteOfflineDeletedHabitsUseCase: DeleteOfflineDeletedHabitsUseCase,
    private val putOfflineHabitListUseCase: PutOfflineHabitListUseCase,
    private val fetchHabitListUseCase: FetchHabitListUseCase,
    private val postOfflineHabitListUseCase: PostOfflineHabitListUseCase
) : BaseViewModel<MainState, MainEvent>(
    initState = MainState(connected = false, navItem = null)
) {

    init {
        observeNetworkConnection()
    }

    fun onNavDestinationUpdated(navItem: NavItem?) {
        _state.update {
            it.copy(navItem = navItem)
        }
    }

    fun openDrawer() {
        _events.update {
            MainEvent.OpenDrawer
        }
    }

    private fun observeNetworkConnection() {
        networkUtil.observeIsOnline().onEach { isOnline ->
            _state.update {
                it.copy(connected = isOnline)
            }
        }.launchIn(viewModelScope)
    }

    suspend fun makeRequestForDataVerification() {
        deleteOfflineDeletedHabitsUseCase()
        putOfflineHabitListUseCase()
        fetchHabitListUseCase()
        postOfflineHabitListUseCase()
    }
}