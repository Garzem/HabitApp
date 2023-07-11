package com.example.newapppp.ui.typeofhabits

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.newapppp.data.Habit
import com.example.newapppp.data.Priority
import com.example.newapppp.data.Type

class ViewPagerViewModel : ViewModel() {
    //?почему это правильно по ООП?
    //хранит изменяющиеся данные типа Habit(используется для хранения текущего состояния привычки)
    private val _habitList = MutableLiveData<List<Habit>>()

    //обеспечивает наблюдаемые данные, т.е предоставляет только чтение habitList
    //??почему это val, если ему при каждой отправке данных присваивается новое значение?
    val habitList: LiveData<List<Habit>> get() = _habitList

//    init {
//        _habitList.value = mutableListOf(
//            Habit(
//                title = "",
//                description = "",
//                period = "",
//                color = 0,
//                priority = Priority.CHOOSE,
//                type = Type.GOOD,
//                quantity = ""
//            )
//        )
//    }

    fun add(habit: Habit) {
        _habitList.value = _habitList.value?.plus(habit)
    }
}