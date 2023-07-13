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



    fun add(habit: Habit) {
        //?нужен ли orEmpty
        //any() проверяет, удовлетворяет ли хотя бы один элемент из списка указанному условию.
        //В данном случае, условие - это сравнение id элемента списка с id новой привычки.
        if (!habitList.value.orEmpty().any { it.id == habit.id }) {
            _habitList.value = _habitList.value?.plus(habit)
        }
    }

    fun update(habit: Habit) {

    }

//    fun habitFilter(type: Type): LiveData<List<Habit>> {
//        return habitList.map {
//            it.filter {}
//        }
//    }
}