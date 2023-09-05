package com.example.newapppp.presentation.redactor

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.newapppp.data.database.habit_local.HabitColor
import com.example.newapppp.data.database.habit_local.HabitPriority
import com.example.newapppp.data.database.habit_local.HabitType
import com.example.newapppp.data.remote.habit.HabitSave
import com.example.newapppp.domain.SingleLiveEvent
import com.example.newapppp.domain.emit
import com.example.newapppp.data.database.habit_local.Habit
import com.example.newapppp.domain.state.UiState
import com.example.newapppp.domain.usecase.DeleteHabitRemoteUseCase
import com.example.newapppp.domain.usecase.GetListUseCase
import com.example.newapppp.domain.usecase.redactor.GetHabitByIdUseCase
import com.example.newapppp.domain.usecase.redactor.GetPriorityIntUseCase
import com.example.newapppp.domain.usecase.redactor.GetTypeIntUseCase
import com.example.newapppp.domain.usecase.redactor.SaveOrUpdateHabitUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class RedactorFragmentViewModel(
    private val getListUseCase: GetListUseCase,
    private val saveOrUpdateHabitUseCase: SaveOrUpdateHabitUseCase,
    private val getTypeIntUseCase: GetTypeIntUseCase,
    private val getPriorityIntUseCase: GetPriorityIntUseCase,
    private val getHabitByIdUseCase: GetHabitByIdUseCase,
    private val deleteHabitRemoteUseCase: DeleteHabitRemoteUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(
        UiState(
            id = null,
            uid = null,
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

    private val _showDeleteError = SingleLiveEvent<Unit>()

    val showDeleteError: LiveData<Unit> get() = _showDeleteError

    private val _goBack = SingleLiveEvent<Unit>()
    val goBack: LiveData<Unit> get() = _goBack

    private var creationDate: Long? = null

    fun setHabit(habitId: String?) {
        if (habitId != null) {
            viewModelScope.launch {
                val habit = getHabitByIdUseCase.execute(habitId)
                creationDate = habit.creationDate
                _uiState.value = UiState(
                    id = habit.id,
                    uid = habit.uid,
                    title = habit.title,
                    titleCursorPosition = 0,
                    description = habit.description,
                    descriptionCursorPosition = 0,
                    color = habit.color,
                    priority = getPriorityIntUseCase.execute(habit.priority),
                    type = getTypeIntUseCase.execute(habit.type),
                    frequency = habit.frequency.toString(),
                    frequencyCursorPosition = 0
                )
            }
        }
    }

    fun setType(habitType: HabitType) {
        val type = getTypeIntUseCase.execute(habitType)
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
                type = getTypeIntUseCase.execute(type)
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


    fun getList(): List<String> {
        return getListUseCase.execute()
    }

    fun deleteHabit() {
        viewModelScope.launch {
            try {
                _uiState.value.let {
                    val habit = Habit(
                        id = it.id ?: return@launch,
                        uid = it.uid,
                        color = it.color,
                        creationDate = creationDate ?: System.currentTimeMillis(),
                        description = it.description,
                        frequency = it.frequency.toInt(),
                        priority = HabitPriority.values().getOrNull(it.priority)
                            ?: HabitPriority.CHOOSE,
                        title = it.title,
                        type = HabitType.values().getOrNull(it.type) ?: HabitType.GOOD
                    )
                    deleteHabitRemoteUseCase.execute(habit)
                    _goBack.emit()
                }
            } catch (e: Exception) {
                _showDeleteError.emit()
            }
        }
    }

    fun saveOrUpdateHabitToServer() {
        val uiState = _uiState.value
        if (validation()) {
            viewModelScope.launch {
                val habitId = uiState.id
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
                    saveOrUpdateHabitUseCase.execute(saveHabit, habitId)
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