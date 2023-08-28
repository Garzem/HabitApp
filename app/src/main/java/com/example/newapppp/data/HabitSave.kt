package com.example.newapppp.data

data class HabitSave(
    val title : String,
    val description : String,
    val creationDate : String,
    val color : HabitColor,
    val priority : HabitPriority,
    val type : HabitType,
    val frequency : Int
)