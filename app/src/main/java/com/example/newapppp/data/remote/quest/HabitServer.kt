package com.example.newapppp.data.remote.quest

import com.google.gson.annotations.SerializedName

data class HabitServer(
    val color: Int,
    val count: Int,
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