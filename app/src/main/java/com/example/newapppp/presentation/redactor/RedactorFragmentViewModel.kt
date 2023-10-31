package com.example.newapppp.presentation.redactor

import androidx.lifecycle.viewModelScope
import com.example.newapppp.domain.model.Habit
import com.example.newapppp.domain.model.HabitColor
import com.example.newapppp.domain.model.HabitCount
import com.example.newapppp.domain.model.HabitPriority
import com.example.newapppp.domain.model.HabitSave
import com.example.newapppp.domain.model.HabitType
import com.example.newapppp.domain.usecase.DeleteHabitUseCase
import com.example.newapppp.domain.usecase.redactor.GetHabitByIdUseCase
import com.example.newapppp.domain.usecase.redactor.SaveOrUpdateHabitUseCase
import com.example.newapppp.presentation.abstracts.BaseViewModel
import com.example.newapppp.presentation.habit_list.mapper.HabitCountMapper
import com.example.newapppp.presentation.habit_list.mapper.HabitPriorityMapper
import com.example.newapppp.presentation.redactor.state.RedactorEvents
import com.example.newapppp.presentation.redactor.state.UiState
import com.example.newapppp.presentation.util.DateGenerator
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RedactorFragmentViewModel @Inject constructor(
    private val saveOrUpdateHabitUseCase: SaveOrUpdateHabitUseCase,
    private val getHabitByIdUseCase: GetHabitByIdUseCase,
    private val deleteHabitUseCase: DeleteHabitUseCase,
    private val habitCountMapper: HabitCountMapper,
    private val habitPriorityMapper: HabitPriorityMapper,
    private val dateGenerator: DateGenerator
) : BaseViewModel<UiState, RedactorEvents>(
    initState = UiState(
        id = null,
        uid = null,
        title = "",
        description = "",
        color = HabitColor.ORANGE,
        priority = 3,
        selectedPriorityLocalized = habitPriorityMapper.getPriorityName(HabitPriority.values()[3]),
        type = 0,
        frequency = "",
        count = 3,
        selectedCountLocalized = habitCountMapper.getCountName(HabitCount.values()[3]),
        doneDates = emptyList()
    )
) {
    private var creationDate: Long? = null

    fun onAction(action: RedactorAction) {
        when (action) {
            is RedactorAction.OnTitleChanged -> {
                _state.update { state ->
                    state.copy(title = action.title)
                }
            }

            is RedactorAction.OnDescriptionChanged -> {
                _state.update { state ->
                    state.copy(description = action.description)
                }
            }

            is RedactorAction.OnFrequencyChanged -> {
                _state.update { state ->
                    state.copy(frequency = action.frequency)
                }
            }

            is RedactorAction.OnCountChanged -> {
                val habitCount = HabitCount.values().getOrElse(action.countIndex) {
                    HabitCount.CHOOSE
                }
                _state.update { state ->
                    state.copy(
                        selectedCountLocalized = habitCountMapper.getCountName(habitCount),
                        count = action.countIndex
                    )
                }
            }

            is RedactorAction.OnPriorityChanged -> {
                val habitPriority = HabitPriority.values().getOrElse(action.priorityIndex) {
                    HabitPriority.CHOOSE
                }
                _state.update { state ->
                    state.copy(
                        selectedPriorityLocalized = habitPriorityMapper.getPriorityName(
                            habitPriority
                        ),
                        priority = action.priorityIndex
                    )
                }
            }

            is RedactorAction.OnRadioButtonClick -> {
                _state.update { state ->
                    state.copy(
                        type = HabitType.values().indexOf(action.type)
                    )
                }
            }

            is RedactorAction.OnSaveButtonClick -> {
                saveClick()
            }

            is RedactorAction.OnDeleteButtonClick -> {
                deleteHabit()
            }
        }
    }

    fun setHabit(habitId: String?) {
        if (habitId != null) {
            viewModelScope.launch {
                val habit = getHabitByIdUseCase(habitId)
                creationDate = habit.creationDate
                _state.value = UiState(
                    id = habit.id,
                    uid = habit.uid,
                    title = habit.title,
                    description = habit.description,
                    color = habit.color,
                    priority = HabitPriority.values().indexOf(habit.priority),
                    selectedPriorityLocalized = habitPriorityMapper.getPriorityName(habit.priority),
                    type = HabitType.values().indexOf(habit.type),
                    frequency = habit.frequency.toString(),
                    count = HabitCount.values().indexOf(habit.count),
                    selectedCountLocalized = habitCountMapper.getCountName(habit.count),
                    doneDates = habit.doneDates
                )
            }
        }
    }

    fun setType(habitType: HabitType) {
        _state.update { state ->
            state.copy(type = HabitType.values().indexOf(habitType))
        }
    }

    fun saveColor(color: HabitColor) {
        _state.update { state ->
            state.copy(color = color)
        }
    }

    fun deleteHabit() {
        viewModelScope.launch {
            _state.value.apply {
                val habit = Habit(
                    id = id ?: return@launch,
                    uid = uid,
                    color = color,
                    creationDate = creationDate ?: dateGenerator.getCurrentDate(),
                    description = description,
                    frequency = frequency.toInt(),
                    priority = HabitPriority.values().getOrNull(priority)
                        ?: HabitPriority.CHOOSE,
                    title = title,
                    type = HabitType.values().getOrNull(type) ?: HabitType.GOOD,
                    count = HabitCount.values().getOrNull(count)
                        ?: HabitCount.WEEK,
                    doneDates = doneDates
                )
                deleteHabitUseCase(habit)
                _events.update {
                    RedactorEvents.GoBack
                }
            }
        }
    }

    fun saveClick() {
        val uiState = _state.value
        if (validation()) {
            viewModelScope.launch {
                val habitId = uiState.id
                val habitSave = HabitSave(
                    color = uiState.color,
                    creationDate = creationDate ?: dateGenerator.getCurrentDate(),
                    description = uiState.description,
                    frequency = uiState.frequency.toInt(),
                    priority = HabitPriority.values().getOrNull(uiState.priority)
                        ?: HabitPriority.CHOOSE,
                    title = uiState.title,
                    type = HabitType.values().getOrNull(uiState.type) ?: HabitType.GOOD,
                    count = HabitCount.values().getOrNull(uiState.count)
                        ?: HabitCount.WEEK,
                    doneDates = uiState.doneDates
                )
                saveOrUpdateHabitUseCase(habitSave, habitId)
                _events.update {
                    RedactorEvents.GoBack
                }
            }
        } else {
            _events.update {
                RedactorEvents.ShowValidationError
            }
        }
    }

    private fun validation(): Boolean {
        return _state.value.run {
            title.isNotBlank()
                    && description.isNotBlank()
                    && priority != HabitPriority.CHOOSE.ordinal
                    && frequency.isNotBlank() && frequency != "0"
                    && count != HabitCount.CHOOSE.ordinal
        }
    }
}
