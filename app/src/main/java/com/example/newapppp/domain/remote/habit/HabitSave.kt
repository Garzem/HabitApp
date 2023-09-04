package com.example.newapppp.domain.remote.habit

import com.example.newapppp.domain.habit_local.HabitColor
import com.example.newapppp.domain.habit_local.HabitPriority
import com.example.newapppp.domain.habit_local.HabitType

data class HabitSave(
    val title : String,
    val description : String,
    val creationDate : Long,
    val color : HabitColor,
    val priority : HabitPriority,
    val type : HabitType,
    val frequency : Int
)