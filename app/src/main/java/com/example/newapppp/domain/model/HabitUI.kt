package com.example.newapppp.domain.model

data class HabitUI(
    val id: String,
    val uid: String?,
    val title: String,
    val description: String,
    val creationDate: String,
    val color: HabitColor,
    val priority: HabitPriority,
    val type: HabitType,
    val frequency: Int
)
