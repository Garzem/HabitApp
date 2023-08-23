package com.example.newapppp.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Habit(
    val id: String,
    val title: String,
    val description: String,
    val creationDate: String,
    val color: HabitColor,
    val priority: HabitPriority,
    val type: HabitType,
    val frequency: Int
) : Parcelable
