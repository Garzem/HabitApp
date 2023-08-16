package com.example.newapppp.ui.habit_list

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.newapppp.data.HabitPriority
import com.example.newapppp.data.HabitType
import com.example.newapppp.habit_repository.HabitRepository
import com.example.newapppp.ui.redactor.SingleLiveEvent
import com.example.newapppp.ui.redactor.emit
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class HabitListViewModel : ViewModel() {

    private val _habitState = MutableStateFlow(
        HabitState(
            allHabits = emptyList(),
            filterByType = null,
            filterByTitle = "",
            filterByPriority = HabitPriority.CHOOSE
        )
    )

    val habitState: StateFlow<HabitState> = _habitState.asStateFlow()

    private val _showErrorToast = SingleLiveEvent<Unit>()
    val showErrorToast: LiveData<Unit> get() = _showErrorToast

    private val _goBack = SingleLiveEvent<Unit>()
    val goBack: LiveData<Unit> get() = _goBack

    fun deleteHabit(id: String) {
        viewModelScope.launch {
            HabitRepository().deleteHabitById(id)
        }
    }

    fun setHabitByType() {
        viewModelScope.launch {
            _habitState.update { state ->
                state.copy(
                    allHabits = HabitRepository().getAllHabits()
                )
            }
        }
    }

    fun onTypeChanged(habitType: HabitType) {
        _habitState.update { state ->
            state.copy(
                filterByType = habitType
            )
        }
    }

    fun onTitleChanged(title: String) {
        _habitState.update { state ->
            state.copy(
                filterByTitle = title
            )
        }
    }

    fun onPriorityChanged(priority: Int) {
        _habitState.update { state ->
            state.copy(
                filterByPriority = HabitPriority.values()[priority]
            )
        }
    }

    fun getFilteredHabit() {
        if (_habitState.value.isFilterApplied) {
            val filteredHabits = _habitState.value.filteredHabits
            _habitState.update { state ->
                state.copy(
                    allHabits = filteredHabits
                )
            }
            _goBack.emit()
        } else {
            _showErrorToast.emit()
        }
    }

    fun cancelFilter() {
        _habitState.update { state ->
            state.copy(
             filterByTitle = "",
             filterByPriority = HabitPriority.CHOOSE
            )
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