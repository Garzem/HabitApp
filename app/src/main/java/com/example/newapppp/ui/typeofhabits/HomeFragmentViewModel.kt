package com.example.newapppp.ui.typeofhabits

import androidx.lifecycle.ViewModel
import com.example.newapppp.data.Habit
import com.example.newapppp.data.HabitType
import com.example.newapppp.habitrepository.HabitRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow


class HomeFragmentViewModel : ViewModel() {
//    private val _homeHabitList = MutableStateFlow<List<Habit>>(emptyList())
//    val homeHabitList: StateFlow<List<Habit>> = _homeHabitList.asStateFlow()

//    fun updateHabitList(updatedHabitList: List<Habit>) {
//        val previousHabitList = _homeHabitList.value.toMutableList()
//        updatedHabitList.forEach { updatedHabit ->
//            val habitToUpdate = previousHabitList.find { it.id == updatedHabit.id }
//            habitToUpdate?.let {
//                val index = previousHabitList.indexOf(it)
//                previousHabitList[index] = updatedHabit
//            }
//        }
//
//        _homeHabitList.value = previousHabitList
//    }
//
//    suspend fun setupGoodHabits(): List<Habit> {
//        return HabitRepository().getHabitListByType(HabitType.GOOD)
//
//    }
//
//    suspend fun setupBadHabits(): List<Habit> {
//        return HabitRepository().getHabitListByType(HabitType.BAD)
//    }
//        val isUpdated = _habitList.value?.any { it.id == updatedHabit.id } ?: false
//        if (isUpdated) {
//            _habitList.value = habitList.value?.map { habit ->
//                if (habit.id == updatedHabit.id) {
//                    updatedHabit
//                } else {
//                    habit
//                }
//            }
//        } else {
//            _habitList.value = _habitList.value?.plus(updatedHabit)
//        }
//}

//    fun habitFilter(type: HabitType): LiveData<List<Habit>> {
//        return habitList.map {
//            it.filter { habit ->
//                habit.type == type
//            }
//        }
//    }

//    fun deleteById (habitId: String) {
//        val currentHabitList = _habitList.value?.toMutableList() ?: return
//        val habitToRemove = currentHabitList.find { it.id == habitId }
//        habitToRemove?.let {
//            currentHabitList.remove(it)
//            _habitList.value = currentHabitList
//        }
    }
