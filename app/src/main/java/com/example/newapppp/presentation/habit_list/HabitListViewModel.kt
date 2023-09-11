package com.example.newapppp.presentation.habit_list

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.newapppp.domain.model.Habit
import com.example.newapppp.domain.model.HabitType
import com.example.newapppp.data.repository.FilterRepositoryImpl
import com.example.newapppp.domain.model.Filter
import com.example.newapppp.domain.repository.FilterRepository
import com.example.newapppp.presentation.habit_list.state.HabitState
import com.example.newapppp.domain.usecase.DeleteHabitUseCase
import com.example.newapppp.domain.usecase.habit_list.FetchHabitListUseCase
import com.example.newapppp.presentation.util.SingleLiveEvent
import com.example.newapppp.presentation.util.emit
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HabitListViewModel @Inject constructor(
    private val deleteHabitUseCase: DeleteHabitUseCase,
    private val fetchHabitListUseCase: FetchHabitListUseCase,
    private val filterRepository: FilterRepository
) : ViewModel() {

    private val _habitState = MutableStateFlow<HabitState>(HabitState.Loading)

    val habitState: StateFlow<HabitState> = _habitState.asStateFlow()

    private val _showDeleteError = SingleLiveEvent<List<Habit>>()

    val showDeleteError: LiveData<List<Habit>> get() = _showDeleteError


    init {
        filterRepository.filterFlow.onEach { filter ->
            _habitState.update { state ->
                if (state is HabitState.Success) state.copy(filter = filter)
                else state
            }
        }.launchIn(viewModelScope)
    }

    fun fetchHabitList(habitType: HabitType) {
        viewModelScope.launch {
            _habitState.update { HabitState.Loading }
            _habitState.update {
                HabitState.Success(
                    habitList = fetchHabitListUseCase(habitType),
                    filter = filterRepository.valueFilter()
                )
            }
        }
    }

    fun deleteHabit(habit: Habit) {
        viewModelScope.launch {
            deleteHabitUseCase(habit)
            _habitState.update { state ->
                if (state is HabitState.Success) {
                    state.copy( habitList = state.habitList - habit)
                } else {
                    state
                }
            }
        }
    }
}