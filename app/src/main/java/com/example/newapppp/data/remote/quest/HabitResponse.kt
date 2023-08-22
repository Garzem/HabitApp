package com.example.newapppp.data.remote.quest

import com.google.gson.annotations.SerializedName

data class HabitResponse(val items: List<HabitItem>)

data class HabitItem(
    val color: Int,
    val count: Int,
    val date: Int,
    val description: String,
    val done_dates: List<Int>,
    val frequency: Int,
    val priority: Int,
    val title: String,
    val type: Int,
    @SerializedName("uid")
    val id: String
)