package com.example.newapppp.ui.typeofhabits

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.map
import com.example.newapppp.data.Habit
import com.example.newapppp.data.Type


class ViewPagerViewModel : ViewModel() {
    //?почему это правильно по ООП?
    //хранит изменяющиеся данные типа Habit(используется для хранения текущего состояния привычки)
    //emptyList() не null, лист с 0 элементов
    private val _habitList = MutableLiveData<List<Habit>>(emptyList())

    //обеспечивает наблюдаемые данные, т.е предоставляет только чтение habitList
    //??почему это val, если ему при каждой отправке данных присваивается новое значение?
    val habitList: LiveData<List<Habit>> get() = _habitList

    fun updateHabitList(habit: Habit) {
        val habitPrevious = _habitList.value?.find { it.id == habit.id }
        if (habitPrevious == null) {
            _habitList.value = _habitList.value?.plus(habit)
        } else
            _habitList.value = habitList.value?.map { habit }
    }


    fun habitFilter(type: Type): LiveData<List<Habit>> {
        return habitList.map {
            it.filter { habit ->
                when (type) {
                    Type.GOOD -> habit.type == Type.GOOD
                    Type.BAD -> habit.type == Type.BAD
                }
            }
        }
    }
}