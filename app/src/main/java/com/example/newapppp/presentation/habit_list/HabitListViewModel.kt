package com.example.newapppp.presentation.habit_list

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.newapppp.abstracts.BaseViewModel
import com.example.newapppp.abstracts.HabitListEvents
import com.example.newapppp.domain.model.Habit
import com.example.newapppp.domain.model.HabitType
import com.example.newapppp.domain.model.Message
import com.example.newapppp.domain.repository.FilterRepository
import com.example.newapppp.domain.usecase.DeleteHabitUseCase
import com.example.newapppp.domain.usecase.habit_list.GetCurrentDateUseCase
import com.example.newapppp.domain.usecase.habit_list.GetHabitListUseCase
import com.example.newapppp.domain.usecase.habit_list.GetHabitUpdatedMessageUseCase
import com.example.newapppp.domain.usecase.habit_list.UpdateHabitDatesUseCase
import com.example.newapppp.presentation.habit_list.state.HabitState
import com.example.newapppp.presentation.util.SingleLiveEvent
import com.example.newapppp.presentation.util.emit
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
    private val getHabitUpdatedMessageUseCase: GetHabitUpdatedMessageUseCase,
    private val updateHabitDatesUseCase: UpdateHabitDatesUseCase,
    private val getCurrentDateUseCase: GetCurrentDateUseCase,
    private val filterRepository: FilterRepository
) : BaseViewModel<HabitState, HabitListEvents>(
    initState = HabitState.Loading
) {
    init {
        filterRepository.filterFlow.onEach { filter ->
            _state.update { state ->
                if (state is HabitState.Success) state.copy(filter = filter)
                else state
            }
        }.launchIn(viewModelScope)
    }

    fun getAndRefreshHabitList(habitType: HabitType) {
        viewModelScope.launch {
            _state.update { HabitState.Loading }
            getHabitListUseCase(habitType).collect { habitList ->
                _state.update {
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

    fun getCurrentDate(): Long {
        return getCurrentDateUseCase()
    }

    fun saveDoneDatesForHabit(habitId: String, newDoneDate: Long) {
        viewModelScope.launch {
            val habit = updateHabitDatesUseCase(habitId, newDoneDate)
            val message = getHabitUpdatedMessageUseCase(habit, newDoneDate)
            _events.update {
                HabitListEvents.ShowMessage(message)
            }
        }
    }
}