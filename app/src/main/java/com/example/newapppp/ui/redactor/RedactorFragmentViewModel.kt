package com.example.newapppp.ui.redactor

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.newapppp.data.Habit
import com.example.newapppp.data.HabitColor
import com.example.newapppp.data.HabitPriority
import com.example.newapppp.data.HabitType
import com.example.newapppp.habitrepository.HabitRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.UUID

class RedactorFragmentViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(
        UiState(
            id = null,
            title = "",
            titleCursorPosition = 0,
            description = "",
            descriptionCursorPosition = 0,
            period = "",
            periodCursorPosition = 0,
            color = HabitColor.ORANGE,
            priorityPosition = 0,
            type = 0,
            quantity = "",
            quantityCursorPosition = 0
        )
    )
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    private val _showErrorToast = SingleLiveEvent<Unit>()
    val showErrorToast: LiveData<Unit> get() = _showErrorToast

    private val _goBack = SingleLiveEvent<Unit>()
    val goBack: LiveData<Unit> get() = _goBack

    fun setHabit(habitId: String?) {
        if (habitId != null) {
            viewModelScope.launch {
                val habit = HabitRepository().getHabitById(habitId)
                _uiState.value = UiState(
                    id = habit.id,
                    title = habit.title,
                    titleCursorPosition = 0,
                    description = habit.description,
                    descriptionCursorPosition = 0,
                    period = habit.period,
                    periodCursorPosition = 0,
                    color = habit.color,
                    priorityPosition = getPositionPriority(habit.priority),
                    type = getPositionType(habit.type),
                    quantity = habit.quantity,
                    quantityCursorPosition = 0
                )
            }
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

    fun onQuantityChanged(quantity: String, cursorPosition: Int) {
        _uiState.update { state ->
            state.copy(
                quantity = quantity,
                quantityCursorPosition = cursorPosition
            )
        }
    }

    fun onPeriodChanged(period: String, cursorPosition: Int) {
        _uiState.update { state ->
            state.copy(
                period = period,
                periodCursorPosition = cursorPosition
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
                priorityPosition = priorityPosition
            )
        }
    }

    private fun getChosenType(typePosition: Int): HabitType {
        return HabitType.values()[typePosition]
    }

    private fun getPositionType(type: HabitType): Int {
        return HabitType.values().indexOf(type)
    }

    private fun getChosenPriority(priorityPosition: Int): HabitPriority {
        return HabitPriority.values()[priorityPosition]
    }

    private fun getPositionPriority(priority: HabitPriority): Int {
        return HabitPriority.values().indexOf(priority)

    }

    fun getList(): List<String> {
        return HabitPriority.values().map {
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
                HabitRepository().deleteHabitById(id)
                _goBack.emit()
            }
        }
    }

    fun saveHabit() {
        val uiState = _uiState.value
        if (validation()) {
            val habit = uiState.run {
                Habit(
                    id = id ?: UUID.randomUUID().toString(),
                    title = title,
                    description = description,
                    period = period,
                    color = color,
                    priority = getChosenPriority(priorityPosition),
                    type = getChosenType(type),
                    quantity = quantity
                )
            }
            viewModelScope.launch {
                HabitRepository().saveHabit(habit)
                _goBack.emit()
            }
        } else {
            _showErrorToast.emit()
        }
    }

    private fun validation(): Boolean {
        return _uiState.value.let { currentState ->
            currentState.run {
                title.isNotBlank()
                        && description.isNotBlank()
                        && period.isNotBlank()
                        && priorityPosition != HabitPriority.CHOOSE.ordinal
                        && quantity.isNotBlank()
            }
        }
    }
}