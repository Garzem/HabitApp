package com.example.newapppp.ui.redactor

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.newapppp.database.HabitEntity
import com.example.newapppp.data.HabitColor
import com.example.newapppp.data.Priority
import com.example.newapppp.database.TypeEntity
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

    private val _goBackWithResult = SingleLiveEvent<HabitEntity>()
    val goBackWithResult: LiveData<HabitEntity> get() = _goBackWithResult

    fun setHabit(habit: HabitEntity?) {
        if (habit != null) {
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

    fun setupType(type: TypeEntity) {
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

    private fun getChosenType(typePosition: Int): TypeEntity {
        return TypeEntity.values()[typePosition]
    }

    private fun getPositionType(type: TypeEntity): Int {
        return TypeEntity.values().indexOf(type)
    }

    private fun getChosenPriority(priorityPosition: Int): Priority {
        return Priority.values()[priorityPosition]
    }

    private fun getPositionPriority(priority: Priority): Int {
        return Priority.values().indexOf(priority)

    }

    fun getList(): List<String> {
        return Priority.values().map {
            when (it) {
                Priority.CHOOSE -> "Приоритет"
                Priority.LOW -> "Низкий"
                Priority.MEDIUM -> "Средний"
                Priority.HIGH -> "Высокий"
            }
        }
    }

    fun deleteHabit() {
        viewModelScope.launch {
//            AppHabitDataBase.habitDao.deleteHabit(_uiState.value)
            _goBackWithResult.emit()
        }
    }

    fun saveHabit() {
        val uiState = _uiState.value
        if (validation()) {
            val habit = uiState.run {
                HabitEntity(
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

                _goBackWithResult.emit(habit)
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
                        && priorityPosition != Priority.CHOOSE.ordinal
                        && quantity.isNotBlank()
            }
        }
    }
}