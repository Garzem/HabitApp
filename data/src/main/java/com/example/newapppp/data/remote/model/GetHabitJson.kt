package com.example.newapppp.data.remote.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GetHabitJson(
    val color: Int,
    val count: Int,
    @SerialName(value = "date")
    val creationDate: Long,
    val description: String,
    val frequency: Int,
    val priority: Int,
    val title: String,
    val type: Int,
    @SerialName(value = "done_dates")
    val doneDates: List<Long>,
    val uid: String
)