package com.example.newapppp.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.newapppp.data.HabitColor


@Entity(tableName = "habit")
data class HabitEntity(
    @PrimaryKey
    val id: String?,
    val title: String,
    val description: String,
    val period: String,
    val color: HabitColor,
    val priority: PriorityEntity,
    val type: TypeEntity,
    val quantity: String
) {
    fun getPriorityName() : String {
        return when (priority) {
            PriorityEntity.CHOOSE -> "Приоритет"
            PriorityEntity.LOW -> "Низкий"
            PriorityEntity.MEDIUM -> "Средний"
            PriorityEntity.HIGH -> "Высокий"
        }
    }
}

enum class PriorityEntity {
    CHOOSE,
    LOW,
    MEDIUM,
    HIGH
}

enum class TypeEntity {
    GOOD,
    BAD
}
