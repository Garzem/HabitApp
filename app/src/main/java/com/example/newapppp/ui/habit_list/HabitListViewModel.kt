package com.example.newapppp.ui.habit_list

import androidx.lifecycle.ViewModel
import com.example.newapppp.data.Filter
import com.example.newapppp.data.Habit
import com.example.newapppp.data.HabitColor
import com.example.newapppp.data.HabitPriority
import com.example.newapppp.data.HabitType
import com.example.newapppp.data.remote.quest.HabitApi
import com.example.newapppp.data.remote.status.StatusUiState
import com.example.newapppp.habit_repository.FilterRepository
import com.example.newapppp.habit_repository.HabitRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class HabitListViewModel : ViewModel() {

    private val job = Job()

    private val _habitState = MutableStateFlow(
        HabitState(
            habitList = emptyList(),
            filter = Filter(
                filterByTitle = "",
                filterByPriority = HabitPriority.CHOOSE,
            ),
            status = StatusUiState.Loading
        )
    )

    val habitState: StateFlow<HabitState> = _habitState.asStateFlow()

    private val viewModelScope = CoroutineScope(Dispatchers.IO + job)

    init {
        FilterRepository.filter.onEach { filter ->
            _habitState.update { state ->
                state.copy(filter = filter)
            }
        }.launchIn(viewModelScope)
    }

    fun fetchHabitList(habitApi: HabitApi, habitType: HabitType) {
        viewModelScope.launch {
            try {
                val habitListResponse = habitApi.getHabitList()
                val habitListRemote = habitListResponse.items.map { item ->
                    Habit(
                        id = item.id,
                        title = item.title,
                        description = item.description,
                        period = item.date.toString(),
                        color = HabitColor.values().getOrNull(item.color) ?: HabitColor.ORANGE,
                        priority = HabitPriority.values().getOrNull(item.priority) ?: HabitPriority.CHOOSE,
                        type = HabitType.values().getOrNull(item.type) ?: HabitType.GOOD,
                        quantity = item.frequency
                    )
                }
                val filteredByType = habitListRemote.filter { habit ->
                    habit.type == habitType
                }
                _habitState.update { state ->
                    state.copy(
                        habitList = filteredByType,
                        status = StatusUiState.Success
                    )
                }
            } catch (e: Exception) {
                    _habitState.update { state ->
                        state.copy(
                            habitList = HabitRepository().getHabitListByType(habitType),
                            status = StatusUiState.Error
                        )
                }
            }
        }
    }

    fun deleteHabit(id: String) {
        viewModelScope.launch {
            HabitRepository().deleteHabitById(id)
        }
    }

//    fun setHabitByType(habitType: HabitType) {
//        viewModelScope.launch {
//            _habitState.update { state ->
//                state.copy(
//                    habitList = HabitRepository().getHabitListByType(habitType),
//                )
//            }
//        }
//    }

    override fun onCleared() {
        super.onCleared()
        job.cancel()
    }
}