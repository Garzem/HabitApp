package com.example.newapppp.ui.habit_list

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.newapppp.data.Habit
import com.example.newapppp.data.HabitPriority
import com.example.newapppp.data.HabitType
import com.example.newapppp.habit_repository.HabitRepository
import com.example.newapppp.ui.filter.FilterState
import com.example.newapppp.ui.redactor.SingleLiveEvent
import com.example.newapppp.ui.redactor.emit
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class HabitListViewModel : ViewModel() {

    private val _habitList = MutableStateFlow<List<Habit>>(emptyList())
    val habitList: StateFlow<List<Habit>> = _habitList.asStateFlow()

    private val _filteredHabitList = MutableStateFlow(
        FilterState(
            title = "",
            priority = 0,
            type = null
        )
    )

    val filteredHabitList: StateFlow<FilterState> = _filteredHabitList.asStateFlow()

    private val _showErrorToast = SingleLiveEvent<Unit>()
    val showErrorToast: LiveData<Unit> get() = _showErrorToast

    private val _goBack = SingleLiveEvent<Unit>()
    val goBack: LiveData<Unit> get() = _goBack

    fun deleteHabit(id: String) {
        viewModelScope.launch {
            HabitRepository().deleteHabitById(id)
        }
    }

    fun setHabitType(habitType: HabitType) {
        viewModelScope.launch {
            _habitList.value = HabitRepository().getHabitListByType(habitType)
        }
    }

    fun onTitleChanged(title: String) {
        _filteredHabitList.update { state ->
            state.copy(
                title = title,
            )
        }
    }

    fun onPriorityChanged(priority: Int) {
        _filteredHabitList.update { state ->
            state.copy(
                priority = priority,
            )
        }
    }

    fun getType(type: HabitType) {
        _filteredHabitList.update { state ->
          state.copy(
              type = type
          )
        }
    }

    fun getFilteredHabit() {
        viewModelScope.launch {
            val title = filteredHabitList.value.title
            val priority = filteredHabitList.value.priority
            val type = filteredHabitList.value.type ?: return@launch
            val filteredList = when {
                title != "" && priority != 0 -> {
                    HabitRepository().getHabitListByTitleAndPriority(title, priority, type)
                }
                title == "" && priority != 0 -> {
                    HabitRepository().getFilteredHabitListByPriority(priority, type)
                }
                title != "" && priority == 0 -> {
                    HabitRepository().getFilteredHabitByTitle(title, type)
                }
                else -> {
                    _showErrorToast.emit()
                    return@launch
                }
            }
            _habitList.value = filteredList
            _goBack.emit()
        }
    }

    fun getList(): List<String> {
        return HabitPriority.values().map {
//            HabitPriorityMapper().getPriorityName(it)
            when (it) {
                HabitPriority.CHOOSE -> "Приоритет"
                HabitPriority.LOW -> "Низкий"
                HabitPriority.MEDIUM -> "Средний"
                HabitPriority.HIGH -> "Высокий"
            }
        }
    }
}