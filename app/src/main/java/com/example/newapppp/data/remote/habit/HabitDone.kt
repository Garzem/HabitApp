package com.example.newapppp.data.remote.habit

import com.google.gson.annotations.SerializedName

data class HabitDone(
    @SerializedName("date")
    val creationDate: Int,
    @SerializedName("habit_uid")
    val id: String
)
