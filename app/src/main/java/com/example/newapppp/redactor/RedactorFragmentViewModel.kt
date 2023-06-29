package com.example.newapppp.redactor

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.newapppp.ui.data.Habit
import com.example.newapppp.ui.data.Priority
import com.example.newapppp.ui.data.Type

class RedactorFragmentViewModel : ViewModel() {

    //как??поместить в функцию и сделать проверку на выбранные значения
    val title = MutableLiveData<String>().apply { value = "" }
    val description = MutableLiveData<String>().apply { value = "" }
    val period = MutableLiveData<String>().apply { value = "" }
    val color = MutableLiveData<Int>().apply { value = 0 }
    val priority = MutableLiveData<Priority>().apply { value = Priority.CHOOSE}
    val type = MutableLiveData<Type>().apply { value = null}
    val quantity = MutableLiveData<String>().apply { value = "" }

    fun makeHabit(): Habit {
        return Habit(
            title = title.value!!,
            description = description.value!!,
            period = period.value!!,
            color = color.value!!,
            priority = priority.value!!,
            type = type.value!!,
            quantity = quantity.value!!
        )
    }

    fun getChosenPriority(position: Int): Priority {
        when (position) {
            0 -> Priority.CHOOSE
            1 -> Priority.LOW
            2 -> Priority.MEDIUM
            3 -> Priority.HIGH
        }
        return Priority.CHOOSE
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
//
//    private fun getPriorityToString(priority: Priority): String = when (priority) {
//        Priority.CHOOSE -> "Приоритет"
//        Priority.LOW -> "Низкий"
//        Priority.MEDIUM -> "Средний"
//        Priority.HIGH -> "Высокий"
//    }

//    fun getChosenPriority(position: Int): Pair<Priority, String> {
//        return when (position) {
//            0 -> Priority.CHOOSE to "Приоритет"
//            1 -> Priority.LOW to "Низкий"
//            2 -> Priority.MEDIUM to "Средний"
//            3 -> Priority.HIGH to "Высокий"
//            else -> Priority.CHOOSE to "Приоритет"
//        }
//    }
}