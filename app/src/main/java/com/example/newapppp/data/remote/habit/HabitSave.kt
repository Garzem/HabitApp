package com.example.newapppp.data.remote.habit

import com.example.newapppp.domain.model.HabitColor
import com.example.newapppp.domain.model.HabitPriority
import com.example.newapppp.domain.model.HabitType

data class HabitSave(
    val title : String,
    val description : String,
    val creationDate : Long,
    val color : HabitColor,
    val priority : HabitPriority,
    val type : HabitType,
    val frequency : Int
)