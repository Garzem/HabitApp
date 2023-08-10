package com.example.newapppp.ui.filter

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.newapppp.data.Habit
import com.example.newapppp.data.HabitPriority
import com.example.newapppp.habit_repository.HabitRepository
import com.example.newapppp.ui.redactor.SingleLiveEvent
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class BottomFilterViewModel : ViewModel() {

    private val _filteredHabitList = MutableStateFlow(
        FilterState(
            title = null,
            priority = 0
        )
    )
    val filteredHabitList: StateFlow<FilterState> = _filteredHabitList.asStateFlow()

    private val _goBack = SingleLiveEvent<Unit>()

    val goBack: LiveData<Unit> get() = _goBack

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

    fun filterHabit() {
        viewModelScope.launch {
            val filteredList = HabitRepository().getFilteredHabitByTitle(title)
            val filteredList = HabitRepository().getFilteredHabitListByPriority(priority)
            val filteredList =
                HabitRepository().getHabitListByTitleAndPriority(title, priority)
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