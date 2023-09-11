package com.example.newapppp.presentation.filter

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.newapppp.domain.model.Filter
import com.example.newapppp.domain.model.HabitPriority
import com.example.newapppp.data.repository.FilterRepositoryImpl
import com.example.newapppp.domain.repository.FilterRepository
import com.example.newapppp.domain.usecase.GetHabitPriorityListUseCase
import com.example.newapppp.presentation.util.SingleLiveEvent
import com.example.newapppp.presentation.util.emit
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class BottomFilterViewModel @Inject constructor(
    private val getHabitPriorityListUseCase: GetHabitPriorityListUseCase,
    private val filterRepository: FilterRepository
): ViewModel() {

    val filterState: StateFlow<Filter?> = filterRepository.filterFlow.stateIn(
        viewModelScope, SharingStarted.Eagerly, null
    )

    private var selectedPriority = HabitPriority.CHOOSE

    private val _showErrorToast = SingleLiveEvent<Unit>()
    val showErrorToast: LiveData<Unit> get() = _showErrorToast

    private val _goBack = SingleLiveEvent<Unit>()
    val goBack: LiveData<Unit> get() = _goBack

    fun onPriorityChanged(priorityInt: Int) {
        selectedPriority = HabitPriority.values()[priorityInt]
    }

    fun onFilterClicked(title: String) {
        if (title.isBlank() && selectedPriority == HabitPriority.CHOOSE) {
            _showErrorToast.emit()
        } else {
            filterRepository.updateFilter { filter ->
                filter.copy(
                    filterByTitle = title,
                    filterByPriority = selectedPriority
                )
            }
            _goBack.emit()
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
        return getHabitPriorityListUseCase()
    }
}