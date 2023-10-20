package com.example.newapppp.presentation.filter

import androidx.lifecycle.viewModelScope
import com.example.newapppp.presentation.abstracts.BaseViewModel
import com.example.newapppp.domain.model.Filter
import com.example.newapppp.domain.model.HabitPriority
import com.example.newapppp.domain.repository.FilterRepository
import com.example.newapppp.presentation.filter.state.FilterEvent
import com.example.newapppp.presentation.filter.state.FilterState
import com.example.newapppp.presentation.habit_list.mapper.HabitPriorityMapper
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class BottomFilterViewModel @Inject constructor(
    private val filterRepository: FilterRepository,
    private val habitPriorityMapper: HabitPriorityMapper
) : BaseViewModel<FilterState, FilterEvent>(
    initState = FilterState(
        selectedTitle = "",
        selectedPriority = HabitPriority.CHOOSE.ordinal
    )
) {

    init {
        filterRepository.filterFlow.onEach { filter ->
            _state.update { state ->
                state.copy(
                    selectedTitle = filter.filterByTitle,
                    selectedPriority = filter.filterByPriority.ordinal
                )
            }
        }.launchIn(viewModelScope)
    }

    fun onAction(action: FilterAction) {
        when (action) {
            is FilterAction.OnTitleFilterChanged -> {
                _state.update { state ->
                    state.copy(selectedTitle = action.title)
                }
            }

            is FilterAction.OnPriorityFilterChanged -> {
                _state.update { state ->
                    state.copy(selectedPriority = action.priority)
                }
            }

            is FilterAction.OnFilterButtonClick -> {
                    if (_state.value.isFilterApplied) {
                        filterRepository.updateFilter { filter ->
                            filter.copy(
                                filterByTitle = _state.value.selectedTitle,
                                filterByPriority = HabitPriority.values()[_state.value.selectedPriority]
                            )
                        }
                        _events.update {
                            FilterEvent.GoBack
                        }
                    } else {
                        _events.update {
                            FilterEvent.ShowErrorToast
                        }
                    }
                }

            is FilterAction.OnCancelClick -> {
                filterRepository.updateFilter {
                    Filter(
                        filterByTitle = "",
                        filterByPriority = HabitPriority.CHOOSE
                    )
                }
                _events.update {
                    FilterEvent.GoBack
                }
            }
        }
    }

    fun priorityIntToString(selectedPriority: Int): String {
        return habitPriorityMapper.getPriorityName(HabitPriority.values()[selectedPriority])
    }
}