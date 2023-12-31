package com.example.newapppp.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Habit(
    val id: String,
    val uid: String?,
    val title: String,
    val description: String,
    val creationDate: Long,
    val color: HabitColor,
    val priority: HabitPriority,
    val type: HabitType,
    val doneDates: List<Long>,
    val count: HabitCount,
    val frequency: Int
) : Parcelable
