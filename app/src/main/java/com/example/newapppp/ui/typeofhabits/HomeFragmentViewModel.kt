package com.example.newapppp.ui.typeofhabits

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.map
import com.example.newapppp.data.HabitType
import com.example.newapppp.database.HabitEntity



class HomeFragmentViewModel : ViewModel() {
    private val _habitList = MutableLiveData<List<HabitEntity>>(emptyList())
    val habitList: LiveData<List<HabitEntity>> get() = _habitList

    fun updateHabitList(updatedHabit: HabitEntity) {
        val isUpdated = _habitList.value?.any { it.id == updatedHabit.id } ?: false
        if (isUpdated) {
            _habitList.value = habitList.value?.map { habit ->
                if (habit.id == updatedHabit.id) {
                    updatedHabit
                } else {
                    habit
                }
            }
        } else {
            _habitList.value = _habitList.value?.plus(updatedHabit)
        }
    }

    fun habitFilter(type: HabitType): LiveData<List<HabitEntity>> {
        return habitList.map {
            it.filter { habit ->
                habit.type == type
            }
        }
    }

//    fun deleteById (habitId: String) {
//        val currentHabitList = _habitList.value?.toMutableList() ?: return
//        val habitToRemove = currentHabitList.find { it.id == habitId }
//        habitToRemove?.let {
//            currentHabitList.remove(it)
//            _habitList.value = currentHabitList
//        }
//    }
}