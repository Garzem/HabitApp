package com.example.newapppp.ui.habit_list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.newapppp.data.Constants.TOKEN
import com.example.newapppp.data.Habit
import com.example.newapppp.data.HabitColor
import com.example.newapppp.data.HabitPriority
import com.example.newapppp.data.HabitType
import com.example.newapppp.data.remote.habit.HabitApi
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

    private val _habitState = MutableStateFlow<HabitState>(HabitState.Loading)

    val habitState: StateFlow<HabitState> = _habitState.asStateFlow()

    init {
        FilterRepository.filterFlow.onEach { filter ->
            _habitState.update { state ->
                if (state is HabitState.Success) state.copy(filter = filter)
                else state
            }
        }.launchIn(viewModelScope)
    }

    fun fetchHabitList(habitApi: HabitApi, habitType: HabitType) {
        viewModelScope.launch {
            _habitState.update { HabitState.Loading }
            try {
                val habitListResponse = habitApi.getHabitList(TOKEN)
                val habitListRemote = habitListResponse.map { item ->
                    Habit(
                        id = item.id,
                        title = item.title,
                        description = item.description,
                        creationDate = convertIntToDate(item.creationDate),
                        color = HabitColor.values().getOrNull(item.color) ?: HabitColor.ORANGE,
                        priority = HabitPriority.values().getOrNull(item.priority)
                            ?: HabitPriority.CHOOSE,
                        type = HabitType.values().getOrNull(item.type) ?: HabitType.GOOD,
                        frequency = item.frequency
                    )
                }
                HabitRepository().sendHabitList(habitListRemote)
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

    private fun convertIntToDate(dateInt: Int): String {
        val dateString = dateInt.toString()
        return if (dateString.length == 8) {
            val day = dateString.substring(0, 2)
            val month = dateString.substring(2, 4)
            val year = dateString.substring(4, 8)

            "$day/$month/$year"
        } else {
            ""
        }
    }

    fun deleteHabit(id: String) {
        viewModelScope.launch {
            HabitRepository().deleteHabitById(id)
        }
    }
}