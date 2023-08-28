package com.example.newapppp.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.newapppp.data.HabitColor
import com.example.newapppp.data.HabitPriority
import com.example.newapppp.data.HabitType


@Entity(tableName = "habit")
data class HabitEntity(
    @PrimaryKey
    val id: String,
    val title: String,
    val description: String,
    val creationDate: String,
    val color: HabitColor,
    val priority: HabitPriority,
    val type: HabitType,
    val frequency: Int
)
