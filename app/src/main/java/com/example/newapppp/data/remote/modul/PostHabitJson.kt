package com.example.newapppp.data.remote.modul

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PostHabitJson(
    @SerialName(value = "date")
    val creationDate: Int,
    @SerialName(value = "habit_uid")
    val uid: String
)
