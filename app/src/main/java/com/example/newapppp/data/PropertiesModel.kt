package com.example.newapppp.data

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Entity(tableName = "habit")
@Parcelize
data class Habit(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val title: String,
    val description: String,
    val period: String,
    val color: HabitColor,
    val priority: Priority,
    val type: Type,
    val quantity: String
) : Parcelable {
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
