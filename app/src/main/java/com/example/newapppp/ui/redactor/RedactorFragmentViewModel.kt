package com.example.newapppp.ui.redactor

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.newapppp.data.habit_local.HabitColor
import com.example.newapppp.data.habit_local.HabitPriority
import com.example.newapppp.data.habit_local.HabitType
import com.example.newapppp.data.remote.habit.HabitSave
import com.example.newapppp.habit_repository.HabitRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class RedactorFragmentViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(
        UiState(
            id = null,
            title = "",
            titleCursorPosition = 0,
            description = "",
            descriptionCursorPosition = 0,
            color = HabitColor.ORANGE,
            priority = 3,
            type = 0,
            frequency = "",
            frequencyCursorPosition = 0
        )
    )
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    private val _showValidationError = SingleLiveEvent<Unit>()
    val showValidationError: LiveData<Unit> get() = _showValidationError

    private val _showSendingError = SingleLiveEvent<Unit>()

    val showSendingError: LiveData<Unit> get() = _showSendingError

    private val _goBack = SingleLiveEvent<Unit>()
    val goBack: LiveData<Unit> get() = _goBack

    private var creationDate: Long? = null

    fun setHabit(habitId: String?) {
        if (habitId != null) {
            viewModelScope.launch {
                val habit = HabitRepository().getHabitById(habitId)
                creationDate = habit.creationDate
                _uiState.value = UiState(
                    id = habit.id,
                    title = habit.title,
                    titleCursorPosition = 0,
                    description = habit.description,
                    descriptionCursorPosition = 0,
                    color = habit.color,
                    priority = getPositionPriority(habit.priority),
                    type = getPositionType(habit.type),
                    frequency = habit.frequency.toString(),
                    frequencyCursorPosition = 0
                )
            }
        }
    }

    fun setType(habitType: HabitType) {
        val type = HabitType.values().indexOf(habitType)
        _uiState.update { state ->
            state.copy(
                type = type
            )
        }
    }

    fun saveColor(color: HabitColor) {
        _uiState.update { state ->
            state.copy(
                color = color
            )
        }
    }

    fun onTitleChanged(title: String, cursorPosition: Int) {
        _uiState.update { state ->
            state.copy(
                title = title,
                titleCursorPosition = cursorPosition
            )
        }
    }

    fun onDescriptionChanged(description: String, cursorPosition: Int) {
        _uiState.update { state ->
            state.copy(
                description = description,
                descriptionCursorPosition = cursorPosition
            )
        }
    }

    fun onFrequencyChanged(frequency: String, cursorPosition: Int) {
        _uiState.update { state ->
            state.copy(
                frequency = frequency,
                frequencyCursorPosition = cursorPosition
            )
        }
    }

    fun setupType(type: HabitType) {
        _uiState.update { state ->
            state.copy(
                type = getPositionType(type)
            )
        }
    }

    fun onNewPrioritySelected(priorityPosition: Int) {
        _uiState.update { state ->
            state.copy(
                priority = priorityPosition
            )
        }
    }

    private fun getPositionType(type: HabitType): Int {
        return HabitType.values().indexOf(type)
    }

    private fun getPositionPriority(priority: HabitPriority): Int {
        return HabitPriority.values().indexOf(priority)

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

    fun deleteHabit() {
        viewModelScope.launch {
            _uiState.value.id?.let { id ->
                HabitRepository().deleteHabit(id)
                _goBack.emit()
            }
        }
    }

    fun saveOrUpdateHabitToServer() {
        val uiState = _uiState.value
        if (validation()) {
            viewModelScope.launch {
                val saveHabit = HabitSave(
                    color = uiState.color,
                    creationDate = creationDate ?: System.currentTimeMillis(),
                    description = uiState.description,
                    frequency = uiState.frequency.toInt(),
                    priority = HabitPriority.values().getOrNull(uiState.priority) ?: HabitPriority.CHOOSE,
                    title = uiState.title,
                    type = HabitType.values().getOrNull(uiState.type) ?: HabitType.GOOD
                )
                try {
                    HabitRepository().saveHabit(saveHabit)
                    _goBack.emit()
                } catch (e: Exception) {
                    _showSendingError.emit()
                }
            }
        } else {
            _showValidationError.emit()
        }
    }

    private fun validation(): Boolean {
        return _uiState.value.let { currentState ->
            currentState.run {
                title.isNotBlank()
                        && description.isNotBlank()
                        && priority != HabitPriority.CHOOSE.ordinal
                        && frequency != ""
            }
        }
    }
}