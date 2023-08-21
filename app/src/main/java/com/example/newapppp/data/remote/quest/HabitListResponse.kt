package com.example.newapppp.data.remote.quest

import com.google.gson.annotations.SerializedName

data class HabitListResponse(val items: List<HabitListItem>)

data class HabitListItem(
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