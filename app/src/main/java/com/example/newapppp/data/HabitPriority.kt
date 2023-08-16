package com.example.newapppp.data

enum class HabitPriority {
    CHOOSE,
    LOW,
    MEDIUM,
    HIGH;

    override fun toString(): String {
        return when (this) {
            CHOOSE -> "Приоритет"
            LOW -> "Низкий"
            MEDIUM -> "Средний"
            HIGH -> "Высокий"
        }
    }
}