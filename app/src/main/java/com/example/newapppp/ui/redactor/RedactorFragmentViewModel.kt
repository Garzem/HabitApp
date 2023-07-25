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

    fun setHabit(habit: Habit?) {
        if (habit != null) {
            _uiState.value = UiState(
                id = habit.id,
                title = habit.title,
                description = habit.description,
                period = habit.period,
                color = getPositionColor(habit.color),
                priorityPosition = getPositionPriority(habit.priority),
                type = getPositionType(habit.type),
                quantity = habit.quantity
            )
        } else {
            _uiState.value = UiState(
                id = "",
                title = "",
                description = "",
                period = "",
                color = getPositionColor(HabitColor.ORANGE),
                priorityPosition = 0,
                type = 0,
                quantity = ""
            )
        }
    }

    fun makeHabit(): Habit {
        val currentState = _uiState.value
        return if (currentState != null && currentState.id.isNotBlank()) {
            currentState.run {
                Habit(
                    id = id,
                    //?? почему только здесь нужен this!!
                    title = title,
                    description = description,
                    period = period,
                    color = getChoosenColor(color),
                    priority = getChosenPriority(priorityPosition),
                    type = getChoosenType(type),
                    quantity = quantity
                )
            }
        } else {
            currentState.run {
                Habit(
                    id = UUID.randomUUID().toString(),
                    //?? почему только здесь нужен this!!
                    title = this?.title ?: "",
                    description = this?.description ?: "",
                    period = this?.period ?: "",
                    color = getChoosenColor(this?.color ?: 4),
                    priority = getChosenPriority(this?.priorityPosition ?: 0),
                    type = getChoosenType(this?.type ?: 1),
                    quantity = this?.quantity ?: ""
                )
            }
        }
    }

    fun getColor(color: HabitColor) {
        _uiState.value = _uiState.value?.copy(
            color = getPositionColor(color)
        )
    }

    fun getTitle(title: String) {
        _uiState.value = _uiState.value?.copy(
            title = title
        )
    }

    fun getDescription(description: String) {
        _uiState.value = _uiState.value?.copy(
            description = description
        )
    }

    fun getQuantity(quantity: String) {
        _uiState.value = _uiState.value?.copy(
            quantity = quantity
        )
    }

    fun getPeriod(period: String) {
        _uiState.value = _uiState.value?.copy(
            period = period
        )
    }

    fun getType(type: Type) {
        _uiState.value = _uiState.value?.copy(
            type = getPositionType(type)
        )
    }

    fun getPriority(priorityPosition: Int) {
        _uiState.value = _uiState.value?.copy(
            priorityPosition = priorityPosition
        )
    }

    fun getChoosenColor(colorPosition: Int): HabitColor {
        return when (colorPosition) {
            0 -> HabitColor.PINK
            1 -> HabitColor.RED
            2 -> HabitColor.DEEPORANGE
            3 -> HabitColor.ORANGE
            4 -> HabitColor.AMBER
            5 -> HabitColor.YELLOW
            6 -> HabitColor.LIME
            7 -> HabitColor.LIGHTGREEN
            8 -> HabitColor.GREEN
            9 -> HabitColor.TEAL
            10 -> HabitColor.CYAN
            11 -> HabitColor.LIGHTBLUE
            12 -> HabitColor.BLUE
            13 -> HabitColor.DARKBLUE
            14 -> HabitColor.PURPLE
            15 -> HabitColor.DEEPPURPLE
            16 -> HabitColor.DEEPPURPLEDARK
            else -> HabitColor.ORANGE
        }
    }

    fun getPositionColor(color: HabitColor): Int {
        return when (color) {
            HabitColor.PINK -> 0
            HabitColor.RED -> 1
            HabitColor.DEEPORANGE -> 2
            HabitColor.ORANGE -> 3
            HabitColor.AMBER -> 4
            HabitColor.YELLOW -> 5
            HabitColor.LIME -> 6
            HabitColor.LIGHTGREEN -> 7
            HabitColor.GREEN -> 8
            HabitColor.TEAL -> 9
            HabitColor.CYAN -> 10
            HabitColor.LIGHTBLUE -> 11
            HabitColor.BLUE -> 12
            HabitColor.DARKBLUE -> 13
            HabitColor.PURPLE -> 14
            HabitColor.DEEPPURPLE -> 15
            HabitColor.DEEPPURPLEDARK -> 16
        }
    }

//    fun getColorInt(color: HabitColor): Int {
//        return when (color) {
//            HabitColor.PINK -> ContextCompat.getColor(R.color.orange)
//            HabitColor.RED -> 1
//            HabitColor.DEEPORANGE -> 2
//            HabitColor.ORANGE -> 3
//            HabitColor.AMBER -> 4
//            HabitColor.YELLOW -> 5
//            HabitColor.LIME -> 6
//            HabitColor.LIGHTGREEN -> 7
//            HabitColor.GREEN -> 8
//            HabitColor.TEAL -> 9
//            HabitColor.CYAN -> 10
//            HabitColor.LIGHTBLUE -> 11
//            HabitColor.BLUE -> 12
//            HabitColor.DARKBLUE -> 13
//            HabitColor.PURPLE -> 14
//            HabitColor.DEEPPURPLE -> 15
//            HabitColor.DEEPPURPLEDARK -> 16
//        }
//    }

    fun getChoosenType(typePosition: Int): Type {
        return when (typePosition) {
            1 -> Type.GOOD
            2 -> Type.BAD
            else -> Type.GOOD
        }
    }

    fun getPositionType(type: Type): Int {
        return when (type) {
            Type.GOOD -> 1
            Type.BAD -> 2
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
    //просто нужно обновить значение
    //проблема тут
    fun getChosenPriority(priorityPosition: Int): Priority {
        return when (priorityPosition) {
            0 -> Priority.CHOOSE
            1 -> Priority.LOW
            2 -> Priority.MEDIUM
            3 -> Priority.HIGH
            else -> Priority.CHOOSE
        }
    }

    fun getPositionPriority(priority: Priority): Int {
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


    fun validation(): Boolean {
        return _uiState.value?.let { currentState ->
            currentState.run {
                id.isBlank() && title.isNotBlank() && description.isNotBlank() && period.isNotBlank() && priorityPosition != Priority.CHOOSE.ordinal && quantity.isNotBlank()
            }
        } ?: false
    }
}