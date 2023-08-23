package com.example.newapppp.data.remote.habit

import com.google.gson.annotations.SerializedName

data class HabitRequest(
    val color: Int,
    val count: Int,
    @SerializedName("date")
    val creationDate: Int,
    val description: String,
    val done_dates: List<Int>,
    val frequency: Int,
    val priority: Int,
    val title: String,
    val type: Int,
    @SerializedName("uid")
    val id: String
)