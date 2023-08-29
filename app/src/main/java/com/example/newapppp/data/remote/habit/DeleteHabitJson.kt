package com.example.newapppp.data.remote.habit

import kotlinx.serialization.Serializable

@Serializable
data class DeleteHabitJson(
    val uid: String
)
