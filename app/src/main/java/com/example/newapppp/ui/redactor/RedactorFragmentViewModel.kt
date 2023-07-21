package com.example.newapppp.ui.redactor

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.map
import com.example.newapppp.data.Habit
import com.example.newapppp.data.Priority
import com.example.newapppp.data.Type
import java.util.UUID

class RedactorFragmentViewModel : ViewModel() {

    //как??поместить в функцию и сделать проверку на выбранные значения
    val title = MutableLiveData<String>().apply { value = "" }
    val description = MutableLiveData<String>().apply { value = "" }
    val period = MutableLiveData<String>().apply { value = "" }
    //??почему с value = 0 была проблема?
    val color = MutableLiveData<Int>().apply { value = 0xFF9100 }
    private val priority = MutableLiveData<Priority>().apply { value = Priority.CHOOSE }
    val priorityPosition = priority.map { priority ->
        getPositionPriority(priority)
    }
    val type = MutableLiveData<Type>().apply { value = null }
    val quantity = MutableLiveData<String>().apply { value = "" }
    var habitId: String? = null

    fun setHabit(habit: Habit?) {
        if (habit != null) {
            habitId = habit.id
            title.value = habit.title
            description.value = habit.description
            period.value = habit.period
            color.value = habit.color
            priority.value = habit.priority
            type.value = habit.type
            quantity.value = habit.quantity

        }
    }

    fun makeHabit(): Habit {
        return Habit(
            id = habitId ?: UUID.randomUUID().toString(),
            title = title.value!!,
            description = description.value!!,
            period = period.value!!,
            color = color.value!!,
            priority = priority.value!!,
            type = type.value!!,
            quantity = quantity.value!!
        )
    }

    //?? обсудить разницу
    //нужно получить выбранное значение для дальнейшей реализации
//    fun getChosenPriority(position: Int): Priority {
//        when (position) {
//            0 -> Priority.CHOOSE
//            1 -> Priority.LOW
//            2 -> Priority.MEDIUM
//            3 -> Priority.HIGH
//        }
//        return Priority.CHOOSE
//    }
    //просто нужно обновить значение
    //проблема тут
    fun getChosenPriority(position: Int) {
        priority.value = when (position) {
            0 -> Priority.CHOOSE
            1 -> Priority.LOW
            2 -> Priority.MEDIUM
            3 -> Priority.HIGH
            else -> Priority.CHOOSE
        }
    }

    fun getPositionPriority(priority: Priority): Int {
        return when (priority) {
            Priority.CHOOSE -> 0
            Priority.LOW -> 1
            Priority.MEDIUM -> 2
            Priority.HIGH -> 3
        }
    }

    //возвращает список элементов, которые будут отображаться в выпадающем списке
    //Этот список будет использован адаптером для отображения элементов
    fun getList(): List<String> {
        //values  возвращает массив всех значений (элементов) перечисления Priority
        //map преобразует каждый элемент перечисления Priority в соответствующую строку
        return Priority.values().map {
            //it это Priority
            when (it) {
                Priority.CHOOSE -> "Приоритет"
                Priority.LOW -> "Низкий"
                Priority.MEDIUM -> "Средний"
                Priority.HIGH -> "Высокий"
            }
        }
    }


    fun validation(): Boolean {
        if (
            title.value == "" ||
            description.value == "" ||
            period.value == "" ||
            color.value == 0 ||
            priority.value == Priority.CHOOSE ||
            type.value == null ||
            quantity.value == ""
        ) {
            return false
        }
        return true
    }
}