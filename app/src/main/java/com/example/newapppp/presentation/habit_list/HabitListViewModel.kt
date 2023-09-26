package com.example.newapppp.presentation.habit_list

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.newapppp.domain.model.Habit
import com.example.newapppp.domain.model.HabitCount
import com.example.newapppp.domain.model.HabitType
import com.example.newapppp.domain.repository.FilterRepository
import com.example.newapppp.domain.usecase.DeleteHabitUseCase
import com.example.newapppp.domain.usecase.done_dates.SaveOrUpdateSelectedDatesUseCase
import com.example.newapppp.domain.usecase.habit_list.GetHabitListUseCase
import com.example.newapppp.domain.usecase.redactor.GetHabitByIdUseCase
import com.example.newapppp.presentation.habit_list.state.HabitState
import com.example.newapppp.presentation.util.SingleLiveEvent
import com.example.newapppp.presentation.util.emit
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.lang.Exception
import java.lang.IllegalStateException
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.temporal.TemporalAdjusters
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

    private val _timesLeftForBadHabit = SingleLiveEvent<Int>()
    val timesLeftForBadHabit: LiveData<Int> get() = _timesLeftForBadHabit

    private val _stopDoingBadHabit = SingleLiveEvent<Unit>()
    val stopDoingBadHabit: LiveData<Unit> get() = _stopDoingBadHabit

    private val _timesLeftForGoodHabit = SingleLiveEvent<Int>()
    val timesLeftForGoodHabit: LiveData<Int> get() = _timesLeftForGoodHabit

    private val _metOrExceededGoodHabit = SingleLiveEvent<Unit>()
    val metOrExceededGoodHabit: LiveData<Unit> get() = _metOrExceededGoodHabit

    private val _showError = SingleLiveEvent<Unit>()
    val showError: LiveData<Unit> get() = _showError


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

    @RequiresApi(Build.VERSION_CODES.O)
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
            getToastMessage(habitId)
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun getToastMessage(habitId: String) {
        viewModelScope.launch {
            val habit = getHabitByIdUseCase(habitId)
            val currentDate = LocalDate.now()
            val doneDates = habit.doneDates.map { LocalDate.ofEpochDay(it) }

            val periodStart = when (habit.count) {
                HabitCount.WEEK -> currentDate.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY))
                HabitCount.MONTH -> currentDate.with(TemporalAdjusters.firstDayOfMonth())
                HabitCount.YEAR -> currentDate.with(TemporalAdjusters.firstDayOfYear())
                else -> throw IllegalStateException("Unsupported habit count: ${habit.count}")
            }

            val executedInPeriod =
                doneDates.count {
                    it.isEqual(periodStart) ||
                            (it.isAfter(periodStart) && it.isEqual(currentDate))
                            || (it.isAfter(periodStart) && it.isBefore(currentDate))
                }
            val remainingExecutions = habit.frequency - executedInPeriod

            when {
                remainingExecutions > 0 && habit.type == HabitType.BAD ->
                    _timesLeftForBadHabit.emit(remainingExecutions)

                remainingExecutions == 0 && habit.type == HabitType.BAD ->
                    _stopDoingBadHabit.emit()

                remainingExecutions > 0 && habit.type == HabitType.GOOD ->
                    _timesLeftForGoodHabit.emit(remainingExecutions)

                remainingExecutions == 0 && habit.type == HabitType.GOOD ->
                    _metOrExceededGoodHabit.emit()

                else -> _showError.emit()
            }
        }
    }
}