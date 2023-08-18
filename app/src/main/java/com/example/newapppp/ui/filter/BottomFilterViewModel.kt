package com.example.newapppp.ui.filter

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.newapppp.data.Filter
import com.example.newapppp.data.HabitPriority
import com.example.newapppp.habit_repository.FilterRepository
import com.example.newapppp.ui.redactor.SingleLiveEvent
import com.example.newapppp.ui.redactor.emit
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class BottomFilterViewModel: ViewModel() {

    val filterState = FilterRepository.filter.asStateFlow()

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
            FilterRepository.filter.update { filter ->
                filter.copy(
                    filterByTitle = title,
                    filterByPriority = selectedPriority
                )
            }
            _goBack.emit()
        }
    }

    fun cancelFilter() {
        FilterRepository.filter.update {
            Filter(
                filterByTitle = "",
                filterByPriority = HabitPriority.CHOOSE
            )
        }
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
}