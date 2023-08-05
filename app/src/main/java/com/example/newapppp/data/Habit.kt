package com.example.newapppp.data

import android.os.Parcelable
import com.example.newapppp.database.TypeEntity
import kotlinx.parcelize.Parcelize

@Parcelize
data class Habit(
    val id: String?,
    val title: String,
    val description: String,
    val period: String,
    val color: HabitColor,
    val priority: Priority,
    val type: TypeEntity,
    val quantity: String
) : Parcelable {
    fun getPriorityName(): String {
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