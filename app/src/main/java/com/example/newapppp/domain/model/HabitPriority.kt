package com.example.newapppp.domain.model

enum class HabitPriority {
    LOW,
    MEDIUM,
    HIGH,
    CHOOSE;

    val index: Int = values().indexOf(this)
//    override fun toString(): String {
//        return when (this) {
//            LOW -> "Низкий"
//            MEDIUM -> "Средний"
//            HIGH -> "Высокий"
//            CHOOSE -> "Приоритет"
//        }
//    }
}