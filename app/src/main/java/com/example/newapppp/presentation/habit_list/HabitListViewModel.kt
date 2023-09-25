package com.example.newapppp.presentation.habit_list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.newapppp.domain.model.Habit
import com.example.newapppp.domain.model.HabitType
import com.example.newapppp.domain.repository.FilterRepository
import com.example.newapppp.domain.usecase.DeleteHabitUseCase
import com.example.newapppp.domain.usecase.done_dates.SaveOrUpdateSelectedDatesUseCase
import com.example.newapppp.domain.usecase.habit_list.GetHabitListUseCase
import com.example.newapppp.domain.usecase.redactor.GetHabitByIdUseCase
import com.example.newapppp.presentation.habit_list.state.HabitState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HabitListViewModel @Inject constructor(
    private val deleteHabitUseCase: DeleteHabitUseCase,
    private val getHabitListUseCase: GetHabitListUseCase,
    private val getHabitByIdUseCase: GetHabitByIdUseCase,
    private val saveOrUpdateSelectedDatesUseCase: SaveOrUpdateSelectedDatesUseCase,
    private val filterRepository: FilterRepository
) : ViewModel() {

    private val _habitState = MutableStateFlow<HabitState>(HabitState.Loading)

    val habitState: StateFlow<HabitState> = _habitState.asStateFlow()

    init {
        filterRepository.filterFlow.onEach { filter ->
            _habitState.update { state ->
                if (state is HabitState.Success) state.copy(filter = filter)
                else state
            }
        }.launchIn(viewModelScope)
    }

    fun getAndRefreshHabitList(habitType: HabitType) {
        viewModelScope.launch {
            _habitState.update { HabitState.Loading }
            getHabitListUseCase(habitType).collect { habitList ->
                _habitState.update {
                    HabitState.Success(
                        habitList = habitList,
                        filter = filterRepository.valueFilter()
                    )
                }
            }
        }
    }

    fun deleteHabit(habit: Habit) {
        viewModelScope.launch {
            deleteHabitUseCase(habit)
        }
    }

    fun saveDoneDatesForHabit(habitId: String, newDoneDate: Long) {
        viewModelScope.launch {
            val habit = getHabitByIdUseCase(habitId)
//            val updatedDoneDates = if (habit.doneDates.isNotEmpty()) {
//                if (habit.doneDates.last() != newDoneDate) {
//                    habit.doneDates.toMutableList().apply {
//                        add(newDoneDate)
//                    }
//                } else {
//                    habit.doneDates
//                }
//            } else {
//                listOf(newDoneDate)
//            }
            val updatedHabit = habit.copy(
                doneDates = habit.doneDates + newDoneDate
            )
            saveOrUpdateSelectedDatesUseCase(updatedHabit)
        }
    }
}