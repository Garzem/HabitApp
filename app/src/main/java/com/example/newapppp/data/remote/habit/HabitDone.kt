package com.example.newapppp.data.remote.habit

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class HabitDone(
    @SerialName(value = "date")
    val creationDate: Int,
    @SerialName(value = "habit_uid")
    val id: String
)
