package com.example.newapppp.presentation.habit_list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.newapppp.domain.model.Habit
import com.example.newapppp.domain.model.HabitType
import com.example.newapppp.domain.repository.FilterRepository
import com.example.newapppp.presentation.habit_list.state.HabitState
import com.example.newapppp.domain.usecase.DeleteHabitUseCase
import com.example.newapppp.domain.usecase.habit_list.FetchHabitListUseCase
import com.example.newapppp.domain.usecase.habit_list.GetHabitListUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HabitListViewModel @Inject constructor(
    private val deleteHabitUseCase: DeleteHabitUseCase,
    private val getHabitListUseCase: GetHabitListUseCase,
    private val filterRepository: FilterRepository
) : ViewModel() {

    private val _habitState = MutableStateFlow<HabitState>(HabitState.Loading)

    val habitState: StateFlow<HabitState> = _habitState.asStateFlow()


    init {
        filterRepository.filterFlow.onEach { filter ->
            _habitState.update { state ->
                if (state is HabitState.Success) state.copy(filter = filter)
                else state
            }
        }.launchIn(viewModelScope)
    }

    fun getAndRefreshHabitList(habitType: HabitType) {
        viewModelScope.launch {
            _habitState.update { HabitState.Loading }
            getHabitListUseCase(habitType).collect { habitList ->
                _habitState.update {
                    HabitState.Success(
                        habitList = habitList,
                        filter = filterRepository.valueFilter()
                    )
                }
            }
        }
    }

    fun deleteHabit(habit: Habit) {
        viewModelScope.launch {
            deleteHabitUseCase(habit)
        }
    }
}