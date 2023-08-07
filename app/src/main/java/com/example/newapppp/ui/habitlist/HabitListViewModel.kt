package com.example.newapppp.ui.habitlist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.newapppp.data.Habit
import com.example.newapppp.data.HabitType
import com.example.newapppp.habitrepository.HabitRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class HabitListViewModel: ViewModel() {

    private val _habitList = MutableStateFlow<List<Habit>>(emptyList())
    val habitList: StateFlow<List<Habit>> = _habitList.asStateFlow()

    fun deleteHabit(id: String) {
        viewModelScope.launch {
            HabitRepository().deleteHabitById(id)
        }
    }

    suspend fun setupGoodHabits(): List<Habit> {
        return HabitRepository().getHabitListByType(HabitType.GOOD)

    }

    suspend fun setupBadHabits(): List<Habit> {
        return HabitRepository().getHabitListByType(HabitType.BAD)
    }

    fun updateHabitList(updatedHabitList: List<Habit>) {
        val previousHabitList = _habitList.value.toMutableList()
        updatedHabitList.forEach { updatedHabit ->
            val habitToUpdate = previousHabitList.find { it.id == updatedHabit.id }
            habitToUpdate?.let {
                val index = previousHabitList.indexOf(it)
                previousHabitList[index] = updatedHabit
            }
        }

        _habitList.value = previousHabitList
    }
}