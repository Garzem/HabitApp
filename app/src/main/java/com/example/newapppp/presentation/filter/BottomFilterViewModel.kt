package com.example.newapppp.presentation.filter

import androidx.lifecycle.viewModelScope
import com.example.newapppp.abstracts.BaseViewModel
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
    private val habitPriorityMapper: HabitPriorityMapper,
    private val filterRepository: FilterRepository
): BaseViewModel<FilterState, FilterEvent>(
    initState = FilterState(null)
) {

    init {
        filterRepository.filterFlow.onEach { filter ->
            _state.update { state ->
                state.copy(filter = filter)
            }
        }.launchIn(viewModelScope)
    }

    private var selectedPriority = HabitPriority.CHOOSE

    fun onPriorityChanged(priorityInt: Int) {
        selectedPriority = HabitPriority.values()[priorityInt]
    }

    fun onFilterClicked(title: String) {
        if (title.isBlank() && selectedPriority == HabitPriority.CHOOSE) {
            _events.update {
                FilterEvent.ShowErrorToast
            }
        } else {
            filterRepository.updateFilter { filter ->
                filter.copy(
                    filterByTitle = title,
                    filterByPriority = selectedPriority
                )
            }
            _events.update {
                FilterEvent.GoBack
            }
        }
    }

    fun cancelFilter() {
        filterRepository.updateFilter {
            Filter(
                filterByTitle = "",
                filterByPriority = HabitPriority.CHOOSE
            )
        }
    }

    fun getList(): List<String> {
        return HabitPriority.values().map {
            habitPriorityMapper.getPriorityName(it)
        }
    }
}