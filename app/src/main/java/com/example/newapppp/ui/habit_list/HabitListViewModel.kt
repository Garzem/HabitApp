package com.example.newapppp.ui.habit_list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.newapppp.data.Filter
import com.example.newapppp.data.HabitPriority
import com.example.newapppp.data.HabitType
import com.example.newapppp.habit_repository.FilterRepository
import com.example.newapppp.habit_repository.HabitRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class HabitListViewModel : ViewModel() {

    private val _habitState = MutableStateFlow(
        HabitState(
            habitList = emptyList(),
            filter = Filter(
                filterByTitle = "",
                filterByPriority = HabitPriority.CHOOSE
            )
        )
    )

    val habitState: StateFlow<HabitState> = _habitState.asStateFlow()

    init {
        FilterRepository.filter.onEach { filter ->
            _habitState.update { state ->
                state.copy(filter = filter)
            }
        }.launchIn(viewModelScope)
    }

    fun deleteHabit(id: String) {
        viewModelScope.launch {
            HabitRepository().deleteHabitById(id)
        }
    }

    fun setHabitByType(habitType: HabitType) {
        viewModelScope.launch {
            _habitState.update { state ->
                state.copy(
                    habitList = HabitRepository().getHabitListByType(habitType),
                )
            }
        }
    }
}