package com.example.newapppp.data

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "habit")
data class HabitEntity(
    @PrimaryKey
    val id: String?,
    val title: String,
    val description: String,
    val period: String,
    val color: HabitColor,
    val priority: Priority,
    val type: Type,
    val quantity: String
) {
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
