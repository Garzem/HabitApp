package com.example.newapppp.ui.typeofhabits

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.map
import com.example.newapppp.data.Habit
import com.example.newapppp.data.Type


class ViewPagerViewModel : ViewModel() {
    private val _habitList = MutableLiveData<List<Habit>>(emptyList())
    val habitList: LiveData<List<Habit>> get() = _habitList

    fun updateHabitList(updatedHabit: Habit) {
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

    fun habitFilter(type: Type): LiveData<List<Habit>> {
        return habitList.map {
            it.filter { habit ->
                habit.type == type
            }
        }
    }

    fun deleteById (habitId: String) {
        val currentHabitList = _habitList.value?.toMutableList() ?: return
        val habitToRemove = currentHabitList.find { it.id == habitId }
        habitToRemove?.let {
            currentHabitList.remove(it)
            _habitList.value = currentHabitList
        }
    }
}