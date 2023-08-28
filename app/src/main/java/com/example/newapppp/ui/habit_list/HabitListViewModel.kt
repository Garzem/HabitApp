package com.example.newapppp.ui.habit_list

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.newapppp.HApp
import com.example.newapppp.data.Constants.TOKEN
import com.example.newapppp.data.Habit
import com.example.newapppp.data.HabitColor
import com.example.newapppp.data.HabitPriority
import com.example.newapppp.data.HabitType
import com.example.newapppp.data.remote.habit.HabitApi
import com.example.newapppp.data.remote.habit.HabitDeleteRequest
import com.example.newapppp.habit_repository.FilterRepository
import com.example.newapppp.habit_repository.HabitRepository
import com.example.newapppp.ui.redactor.SingleLiveEvent
import com.example.newapppp.ui.redactor.emit
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class HabitListViewModel : ViewModel() {

    private val _habitState = MutableStateFlow<HabitState>(HabitState.Loading)

    val habitState: StateFlow<HabitState> = _habitState.asStateFlow()

    private val _showDeleteError = SingleLiveEvent<List<Habit>>()

    val showDeleteError: LiveData<List<Habit>> get() = _showDeleteError


    init {
        FilterRepository.filterFlow.onEach { filter ->
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
                val habitListRemote = HabitRepository().getHabitList()
                val filteredByType = habitListRemote.filter { habit ->
                    habit.type == habitType
                }
                _habitState.update {
                    HabitState.Success(
                        habitList = filteredByType,
                        filter = FilterRepository.filterFlow.value
                    )
                }
            } catch (e: Exception) {
                _habitState.update {
                    HabitState.Success(
                        habitList = HabitRepository().getHabitListByType(habitType),
                        filter = FilterRepository.filterFlow.value
                    )
                }
            }
        }
    }

    fun deleteHabit(id: String) {
        viewModelScope.launch {
            try {
                HabitRepository().deleteHabit(id)
            } catch (e: Exception) {
                (_habitState.value as? HabitState.Success)?.let {
                    _showDeleteError.emit(it.filteredHabits)
                }
                Log.e("wrongSendingHVM", "An error occurred: ${e.message}")
            }
        }
    }
}