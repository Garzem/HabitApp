package com.example.newapppp.data.remote.habit

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class HabitServer(
    val color: Int,
    val count: Int,
    @SerialName(value = "date")
    val creationDate: Int,
    val description: String,
    @SerialName(value = "done_dates")
    val doneDates: List<Int>,
    val frequency: Int,
    val priority: Int,
    val title: String,
    val type: Int,
    @SerialName(value = "uid")
    val id: String
)