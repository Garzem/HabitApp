package com.example.newapppp.data.remote.habit

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class HabitIdGetter(
    @SerialName(value = "uid")
    val id: String
)