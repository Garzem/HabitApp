package com.example.newapppp.data.database.habit_local

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.newapppp.data.database.habit_local.HabitColor
import com.example.newapppp.data.database.habit_local.HabitPriority
import com.example.newapppp.data.database.habit_local.HabitType


@Entity(tableName = "habit")
data class HabitEntity(
    @PrimaryKey
    val id: String,
    val uid: String?,
    val title: String,
    val description: String,
    val creationDate: Long,
    val color: HabitColor,
    val priority: HabitPriority,
    val type: HabitType,
    val frequency: Int,
    val deleted: Boolean
)