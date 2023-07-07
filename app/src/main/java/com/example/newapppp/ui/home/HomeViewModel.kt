package com.example.newapppp.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.newapppp.data.Habit

class HomeViewModel : ViewModel() {
    //?почему это правильно по ООП?
    //хранит изменяющиеся данные типа Habit(используется для хранения текущего состояния привычки)
    private val _habit = MutableLiveData<Habit>()
    //обеспечивает наблюдаемые данные, т.е предоставляет только чтение _habit
    //??почему это val, если ему при каждой отправке данных присваивается новое значение?
    val habit: LiveData<Habit> get() = _habit
    //??не совсем понял
    //устанавливает новое значение для _habit
    fun setHabit(habit: Habit) {
        _habit.value = habit
    }
}