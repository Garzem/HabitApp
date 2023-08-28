package com.example.newapppp.data

data class SaveHabit(
    val title : String,
    val description : String,
    val creationDate : Long,
    val color : HabitColor,
    val priority : HabitPriority,
    val type : HabitType,
    val frequency : Int
)