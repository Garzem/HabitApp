package com.example.newapppp.domain.model

data class HabitSave(
    val title : String,
    val description : String,
    val creationDate : Long,
    val color : HabitColor,
    val priority : HabitPriority,
    val type : HabitType,
    val doneDates: List<Long>,
    val frequency : Int,
    val count: HabitCount
)