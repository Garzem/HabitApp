package com.example.newapppp.ui.habit_list

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.newapppp.data.HabitPriority
import com.example.newapppp.data.HabitType
import com.example.newapppp.habit_repository.FilterRepository
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
            habitList = emptyList(),
            filters = FilterRepository
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

    fun setHabitByType(habitType: HabitType) {
        viewModelScope.launch {
            _habitState.update { state ->
                val habitListByType = HabitRepository().getHabitListByType(habitType)
                state.copy(
                    habitList = habitListByType,
                )
            }
        }
    }

    fun onTitleChanged(title: String) {
        viewModelScope.launch {
            _habitState.update { state ->
                val updatedFilters = FilterRepository.apply {
                    filterByTitle = title
                    filterByPriority = state.filters.filterByPriority
                }
                state.copy(
                    filters = updatedFilters
                )
            }
        }
    }

    fun onPriorityChanged(priorityInt: Int) {
        viewModelScope.launch {
            _habitState.update { state ->
                val updatedFilters = FilterRepository.apply {
                    filterByTitle = state.filters.filterByTitle
                    val priority = HabitPriority.values()[priorityInt]
                    filterByPriority = priority
                }
                state.copy(
                    filters = updatedFilters
                )
            }
        }
    }

    fun getFilteredHabit() {
        if (_habitState.value.isFilterApplied) {
            Log.d("HabitListFragment", "${habitState.value.filteredHabits}")
            _goBack.emit()
            _habitState.update { state ->
                state.copy()
            }
        } else {
            _showErrorToast.emit()
        }
    }

    fun cancelFilter() {
        viewModelScope.launch {
            _habitState.update { state ->
                val canceledFilters = FilterRepository.apply {
                    filterByTitle = ""
                    filterByPriority = HabitPriority.CHOOSE
                }
                state.copy(
                    filters = canceledFilters
                )
            }
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