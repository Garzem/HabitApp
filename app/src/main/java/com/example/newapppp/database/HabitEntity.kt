package com.example.newapppp.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.newapppp.data.HabitColor
import com.example.newapppp.data.HabitPriority
import com.example.newapppp.data.HabitType


@Entity(tableName = "habit")
data class HabitEntity(
    @PrimaryKey
    val id: String?,
    val title: String,
    val description: String,
    val period: String,
    val color: HabitColor,
    val priority: HabitPriority,
    val type: HabitType,
    val quantity: String
) {
    fun getPriorityName() : String {
        return when (priority) {
            HabitPriority.CHOOSE -> "Приоритет"
            HabitPriority.LOW -> "Низкий"
            HabitPriority.MEDIUM -> "Средний"
            HabitPriority.HIGH -> "Высокий"
        }
    }
}