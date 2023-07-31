package com.example.newapppp.ui.redactor

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.newapppp.data.Habit
import com.example.newapppp.data.HabitColor
import com.example.newapppp.data.Priority
import com.example.newapppp.data.Type
import com.example.newapppp.data.UiState
import java.util.UUID

class RedactorFragmentViewModel : ViewModel() {
    private val _uiState = MutableLiveData<UiState>()
    val uiState: LiveData<UiState> get() = _uiState

    private val _showErrorToast = SingleLiveEvent<Unit>()
    val showErrorToast: LiveData<Unit> get() = _showErrorToast

    //?? зачем нам это, если мы в любом случае сохраним
    private val _goBackWithResult = SingleLiveEvent<Habit>()
    val goBackWithResult: LiveData<Habit> get() = _goBackWithResult

    fun setHabit(habit: Habit?) {
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
        } else {
            _uiState.value = UiState(
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
        }
    }

    fun saveColor(color: HabitColor) {
        _uiState.value = _uiState.value?.copy(
            color = color
        )
    }

    fun onTitleChanged(title: String, cursorPosition: Int) {
        if (_uiState.value?.title != title) {
            _uiState.value = _uiState.value?.copy(
                title = title,
                titleCursorPosition = cursorPosition
            )
        }
    }

    fun onDescriptionChanged(description: String, cursorPosition: Int) {
        if (_uiState.value?.description != description) {
            _uiState.value = _uiState.value?.copy(
                description = description,
                descriptionCursorPosition = cursorPosition
            )
        }
    }

    fun onQuantityChanged(quantity: String, cursorPosition: Int) {
        if (_uiState.value?.quantity != quantity) {
            _uiState.value = _uiState.value?.copy(
                quantity = quantity,
                quantityCursorPosition = cursorPosition
            )
        }
    }

    fun onPeriodChanged(period: String, cursorPosition: Int) {
        if (_uiState.value?.period != period) {
            _uiState.value = _uiState.value?.copy(
                period = period,
                periodCursorPosition = cursorPosition
            )
        }
    }

    fun setupType(type: Type) {
        _uiState.value = _uiState.value?.copy(
            type = getPositionType(type)
        )
    }

    fun onNewPrioritySelected(priorityPosition: Int) {
        _uiState.value = _uiState.value?.copy(
            priorityPosition = priorityPosition
        )
    }

    private fun getChosenType(typePosition: Int): Type {
        return when (typePosition) {
            0 -> Type.GOOD
            1 -> Type.BAD
            else -> Type.GOOD
        }
    }

    private fun getPositionType(type: Type): Int {
        return when (type) {
            Type.GOOD -> 0
            Type.BAD -> 1
        }
    }

    //?? обсудить разницу
    //нужно получить выбранное значение для дальнейшей реализации
//    fun getChosenPriority(position: Int): Priority {
//        when (position) {
//            0 -> Priority.CHOOSE
//            1 -> Priority.LOW
//            2 -> Priority.MEDIUM
//            3 -> Priority.HIGH
//        }
//        return Priority.CHOOSE
//    }
    private fun getChosenPriority(priorityPosition: Int): Priority {
        return when (priorityPosition) {
            0 -> Priority.CHOOSE
            1 -> Priority.LOW
            2 -> Priority.MEDIUM
            3 -> Priority.HIGH
            else -> Priority.CHOOSE
        }
    }

    private fun getPositionPriority(priority: Priority): Int {
        return when (priority) {
            Priority.CHOOSE -> 0
            Priority.LOW -> 1
            Priority.MEDIUM -> 2
            Priority.HIGH -> 3
        }
    }

    //возвращает список элементов, которые будут отображаться в выпадающем списке
    //Этот список будет использован адаптером для отображения элементов
    fun getList(): List<String> {
        //values  возвращает массив всех значений (элементов) перечисления Priority
        //map преобразует каждый элемент перечисления Priority в соответствующую строку
        return Priority.values().map {
            //it это Priority
            when (it) {
                Priority.CHOOSE -> "Приоритет"
                Priority.LOW -> "Низкий"
                Priority.MEDIUM -> "Средний"
                Priority.HIGH -> "Высокий"
            }
        }
    }

    fun saveHabit() {
        val uiState = _uiState.value
        //?? почему если написать uiState != null || validation() и поменять местами
        //else, то требует проверку на null??
        if (uiState == null || !validation()) {
            _showErrorToast.emit()
        } else {
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
            _goBackWithResult.emit(habit)
        }
    }

    private fun validation(): Boolean {
        return _uiState.value?.let { currentState ->
            currentState.run {
                title.isNotBlank() && description.isNotBlank() && period.isNotBlank() && priorityPosition != Priority.CHOOSE.ordinal && quantity.isNotBlank()
            }
        } ?: false
    }
}