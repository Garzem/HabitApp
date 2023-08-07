package com.example.newapppp.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Habit(
    val id: String,
    val title: String,
    val description: String,
    val period: String,
    val color: HabitColor,
    val priority: HabitPriority,
    val type: HabitType,
    val quantity: String
) : Parcelable {
    fun getPriorityName(): String {
        return when (priority) {
            HabitPriority.CHOOSE -> "Приоритет"
            HabitPriority.LOW -> "Низкий"
            HabitPriority.MEDIUM -> "Средний"
            HabitPriority.HIGH -> "Высокий"
        }
    }
}