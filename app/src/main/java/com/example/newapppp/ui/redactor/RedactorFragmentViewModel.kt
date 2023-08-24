package com.example.newapppp.ui.redactor

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.newapppp.HApp
import com.example.newapppp.data.Constants.TOKEN
import com.example.newapppp.data.Habit
import com.example.newapppp.data.HabitColor
import com.example.newapppp.data.HabitPriority
import com.example.newapppp.data.HabitType
import com.example.newapppp.data.remote.habit.HabitRequest
import com.example.newapppp.habit_repository.HabitRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import java.util.UUID

class RedactorFragmentViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(
        UiState(
            id = null,
            title = "",
            titleCursorPosition = 0,
            description = "",
            descriptionCursorPosition = 0,
            creationDate = null,
            color = HabitColor.ORANGE,
            priority = 0,
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
                    creationDate = habit.creationDate,
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
                HabitRepository().deleteHabitById(id)
                _goBack.emit()
            }
        }
    }

    fun saveOrUpdateHabitToServer() {
        val uiState = _uiState.value
        if (validation()) {
            val habit = uiState.run {
                Habit(
                    id = id ?: UUID.randomUUID().toString(),
                    title = title,
                    description = description,
                    creationDate = creationDate ?: getCurrentDate(),
                    color = color,
                    priority = getChosenPriority(priority),
                    type = getChosenType(type),
                    frequency = frequency.toInt()
                )
            }
            viewModelScope.launch {
                val habitRequest = HabitRequest(
                    color = HabitColor.values().indexOf(habit.color),
                    count = 0,
                    creationDate = convertDateToInt(habit.creationDate),
                    description = habit.description,
                    done_dates = emptyList(),
                    frequency = habit.frequency,
                    priority = HabitPriority.values().indexOf(habit.priority),
                    title = habit.title,
                    type = HabitType.values().indexOf(habit.type),
                    id = habit.id
                )
                try {
                    HabitRepository().saveHabit(habit)
                    HApp.habitApi.putHabit(TOKEN, habitRequest)
                    _goBack.emit()
                } catch (e: Exception) {
                    _showSendingError.emit()
                    Log.e("wrongSending", "An error occurred: ${e.message}")
                    Log.d("wrongSending", "$habitRequest")
                }
            }
        } else {
            _showValidationError.emit()
        }
    }

//    private fun saveOrUpdateHabitToServer(habit: Habit) {
//        viewModelScope.launch {
//            val habitRequest = HabitRequest(
//                color = HabitColor.values().indexOf(habit.color),
//                count = 0,
//                creationDate = convertDateToInt(habit.creationDate),
//                description = habit.description,
//                done_dates = emptyList(),
//                frequency = habit.frequency,
//                priority = HabitPriority.values().indexOf(habit.priority),
//                title = habit.title,
//                type = HabitType.values().indexOf(habit.type),
//                id = habit.id
//            )
//            try {
//                HabitRepository().saveHabit(habit)
//                HApp.habitApi.putHabit(TOKEN, habitRequest)
//                _goBack.emit()
//            } catch (e: Exception) {
//                _showSendingError.emit()
//                Log.e("wrongSending", "An error occurred: ${e.message}")
//                Log.d("wrongSending", "$habitRequest")
//            }
//        }
//    }

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

    private fun getCurrentDate(): String {
        val currentDate = Calendar.getInstance().time
        val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        return dateFormat.format(currentDate)
    }

    private fun convertDateToInt(dateString: String): Int {
        val parts = dateString.split("/")
        if (parts.size == 3) {
            val day = parts[0]
            val month = parts[1]
            val year = parts[2]

            val dateInt = "$day$month$year".toIntOrNull()
            if (dateInt != null) {
                return dateInt
            }
        }
        return 20122020
    }
}