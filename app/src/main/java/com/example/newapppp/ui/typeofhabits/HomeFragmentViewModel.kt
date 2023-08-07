package com.example.newapppp.ui.typeofhabits

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.map
import com.example.newapppp.data.Habit
import com.example.newapppp.data.HabitType
import com.example.newapppp.database.HabitEntity
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow


class HomeFragmentViewModel : ViewModel() {
    private val _habitList = MutableStateFlow<List<Habit>>(emptyList())
    val habitList: StateFlow<List<Habit>> = _habitList.asStateFlow()

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
}

//    fun habitFilter(type: HabitType): LiveData<List<HabitEntity>> {
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
//    }
}