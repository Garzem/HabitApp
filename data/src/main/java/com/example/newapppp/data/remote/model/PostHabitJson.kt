package com.example.newapppp.data.remote.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PostHabitJson(
    @SerialName(value = "date")
    val doneDate: Long,
    @SerialName(value = "habit_uid")
    val uid: String
)
