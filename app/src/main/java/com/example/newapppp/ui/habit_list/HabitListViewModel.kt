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
import com.example.newapppp.ui.redactor.UiState
import com.example.newapppp.ui.redactor.emit
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class HabitListViewModel : ViewModel() {

    private val _habitList = MutableStateFlow<List<Habit>>(emptyList())
    val habitList: StateFlow<List<Habit>> = _habitList.asStateFlow()

    private var _habitType: HabitType? = null
    val habitType: HabitType? get() = _habitType

    private var _filterState = FilterState(
        title = "",
        priority = HabitPriority.CHOOSE.toString()
    )
    val filterState get() = _filterState

    private val _isFilterApplied = MutableStateFlow(false)

    val isFilterApplied: StateFlow<Boolean> = _isFilterApplied.asStateFlow()

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
        this._habitType = habitType
        viewModelScope.launch {
            _habitList.value = HabitRepository().getHabitListByType(habitType)
        }
    }

    fun onTitleChanged(title: String) {
        _filterState = _filterState.copy(
            title = title,
        )

    }

    fun onPriorityChanged(priority: Int) {
        _filterState = _filterState.copy(
            priority = HabitPriority.values()[priority].name,
        )
    }

    fun setPriorityInt(priority: String): Int {
        return HabitPriority.values().indexOfFirst { it.name == priority }
    }

    fun getFilteredHabit() {
        val type = _habitType ?: return
        val title = _filterState.title
        val priority = _filterState.priority
        viewModelScope.launch {
            val filteredList = when {
                title != "" && priority != HabitPriority.CHOOSE.toString() -> {
                    HabitRepository().getHabitListByTitleAndPriority(title, priority, type)
                }

                title == "" && priority != HabitPriority.CHOOSE.toString() -> {
                    HabitRepository().getFilteredHabitListByPriority(priority, type)
                }

                title != "" && priority == HabitPriority.CHOOSE.toString() -> {
                    HabitRepository().getFilteredHabitByTitle(title, type)
                }

                else -> {
                    _showErrorToast.emit()
                    return@launch
                }
            }
            _habitList.value = filteredList
            _isFilterApplied.value = true
            _goBack.emit()
        }
    }

    fun cancelFilter() {
        _isFilterApplied.value = false
        _filterState = FilterState(
            title = "",
            priority = HabitPriority.CHOOSE.toString()
        )
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