package com.example.newapppp.presentation.habit_list

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.newapppp.data.database.habit_local.Habit
import com.example.newapppp.data.database.habit_local.HabitType
import com.example.newapppp.data.repository.FilterRepositoryImpl
import com.example.newapppp.data.repository.HabitRepositoryImpl
import com.example.newapppp.domain.SingleLiveEvent
import com.example.newapppp.domain.emit
import com.example.newapppp.domain.state.HabitState
import com.example.newapppp.domain.usecase.DeleteHabitRemoteUseCase
import com.example.newapppp.domain.usecase.habit_list.GetLocalHabitListTypeUseCase
import com.example.newapppp.domain.usecase.habit_list.GetLocalHabitListUseCase
import com.example.newapppp.domain.usecase.habit_list.GetRemoteHabitListByTypeUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class HabitListViewModel(
    private val getRemoteHabitListByTypeUseCase: GetRemoteHabitListByTypeUseCase,
    private val deleteHabitRemoteUseCase: DeleteHabitRemoteUseCase,
    private val getLocalHabitListUseCase: GetLocalHabitListUseCase,
    private val getLocalHabitListTypeUseCase: GetLocalHabitListTypeUseCase
) : ViewModel() {

    private val _habitState = MutableStateFlow<HabitState>(HabitState.Loading)

    val habitState: StateFlow<HabitState> = _habitState.asStateFlow()

    private val _showDeleteError = SingleLiveEvent<List<Habit>>()

    val showDeleteError: LiveData<List<Habit>> get() = _showDeleteError


    init {
        FilterRepositoryImpl.filterFlow.onEach { filter ->
            _habitState.update { state ->
                if (state is HabitState.Success) state.copy(filter = filter)
                else state
            }
        }.launchIn(viewModelScope)
    }

    fun fetchHabitList(habitType: HabitType) {
        viewModelScope.launch {
            _habitState.update { HabitState.Loading }
            try {
                val filteredByType = getRemoteHabitListByTypeUseCase.execute(habitType)
                _habitState.update {
                    HabitState.Success(
                        habitList = filteredByType,
                        filter = FilterRepositoryImpl.filterFlow.value
                    )
                }
            } catch (e: Exception) {
                _habitState.update {
                    HabitState.Success(
                        habitList = getLocalHabitListTypeUseCase.execute(habitType),
                        filter = FilterRepositoryImpl.filterFlow.value
                    )
                }
            }
        }
    }

    fun deleteHabit(habit: Habit) {
        viewModelScope.launch {
            try {
                deleteHabitRemoteUseCase.execute(habit)
            } catch (e: Exception) {
                val actualHabitList = getLocalHabitListUseCase.execute(habit)
                _habitState.update { state ->
                    if (state is HabitState.Success) state.copy( habitList = actualHabitList )
                    else state
                }
                (_habitState.value as? HabitState.Success)?.let {
                    _showDeleteError.emit(it.filteredHabits)
                }
            }
        }
    }
}