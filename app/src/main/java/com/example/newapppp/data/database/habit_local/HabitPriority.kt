package com.example.newapppp.data.database.habit_local

enum class HabitPriority {
    LOW,
    MEDIUM,
    HIGH,
    CHOOSE;

    override fun toString(): String {
        return when (this) {
            LOW -> "Низкий"
            MEDIUM -> "Средний"
            HIGH -> "Высокий"
            CHOOSE -> "Приоритет"
        }
    }
}