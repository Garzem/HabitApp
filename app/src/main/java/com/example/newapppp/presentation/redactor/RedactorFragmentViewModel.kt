package com.example.newapppp.presentation.redactor

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.newapppp.domain.model.HabitColor
import com.example.newapppp.domain.model.HabitPriority
import com.example.newapppp.domain.model.HabitType
import com.example.newapppp.domain.model.HabitSave
import com.example.newapppp.domain.model.Habit
import com.example.newapppp.presentation.redactor.state.UiState
import com.example.newapppp.domain.usecase.DeleteHabitUseCase
import com.example.newapppp.domain.usecase.GetHabitPriorityListUseCase
import com.example.newapppp.domain.usecase.redactor.GetHabitByIdUseCase
import com.example.newapppp.domain.usecase.redactor.SaveOrUpdateHabitUseCase
import com.example.newapppp.presentation.util.SingleLiveEvent
import com.example.newapppp.presentation.util.emit
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RedactorFragmentViewModel @Inject constructor(
    private val getHabitPriorityListUseCase: GetHabitPriorityListUseCase,
    private val saveOrUpdateHabitUseCase: SaveOrUpdateHabitUseCase,
    private val getHabitByIdUseCase: GetHabitByIdUseCase,
    private val deleteHabitUseCase: DeleteHabitUseCase
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
            frequencyCursorPosition = 0,
            doneDates = emptyList()
        )
    )
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    private val _showValidationError = SingleLiveEvent<Unit>()
    val showValidationError: LiveData<Unit> get() = _showValidationError

    private val _goBack = SingleLiveEvent<Unit>()
    val goBack: LiveData<Unit> get() = _goBack

    private var creationDate: Long? = null

    fun setHabit(habitId: String?) {
        if (habitId != null) {
            viewModelScope.launch {
                val habit = getHabitByIdUseCase(habitId)
                creationDate = habit.creationDate
                _uiState.value = UiState(
                    id = habit.id,
                    uid = habit.uid,
                    title = habit.title,
                    titleCursorPosition = 0,
                    description = habit.description,
                    descriptionCursorPosition = 0,
                    color = habit.color,
                    priority = HabitPriority.values().indexOf(habit.priority),
                    type = HabitType.values().indexOf(habit.type),
                    frequency = habit.frequency.toString(),
                    frequencyCursorPosition = 0,
                    doneDates = habit.doneDates
                )
            }
        }
    }

    fun setType(habitType: HabitType) {
        _uiState.update { state ->
            state.copy(type = HabitType.values().indexOf(habitType))
        }
    }

    fun saveColor(color: HabitColor) {
        _uiState.update { state ->
            state.copy(color = color)
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
            state.copy(type = HabitType.values().indexOf(type))
        }
    }

    fun onNewPrioritySelected(priorityPosition: Int) {
        _uiState.update { state ->
            state.copy(priority = priorityPosition)
        }
    }


    fun getList(): List<String> {
        return getHabitPriorityListUseCase()
    }

    fun deleteHabit() {
        viewModelScope.launch {
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
                    type = HabitType.values().getOrNull(it.type) ?: HabitType.GOOD,
                    doneDates = it.doneDates
                )
                deleteHabitUseCase(habit)
                _goBack.emit()
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
                    priority = HabitPriority.values().getOrNull(uiState.priority)
                        ?: HabitPriority.CHOOSE,
                    title = uiState.title,
                    type = HabitType.values().getOrNull(uiState.type) ?: HabitType.GOOD,
                    doneDates = uiState.doneDates
                )
                saveOrUpdateHabitUseCase(saveHabit, habitId)
                _goBack.emit()
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