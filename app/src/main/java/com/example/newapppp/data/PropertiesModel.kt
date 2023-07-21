package com.example.newapppp.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize


@Parcelize
data class Habit(
    val id: String,
    val title: String,
    val description: String,
    val period: String,
    val color: Int,
    val priority: Priority,
    val type: Type,
    val quantity: String
) : Parcelable {
    //делает перевод на русский
    fun getPriorityName() : String {
        return when (priority) {
            Priority.CHOOSE -> "Приоритет"
            Priority.LOW -> "Низкий"
            Priority.MEDIUM -> "Средний"
            Priority.HIGH -> "Высокий"
        }
    }
}

enum class Priority {
    CHOOSE,
    LOW,
    MEDIUM,
    HIGH
}

enum class Type {
    GOOD,
    BAD
}
