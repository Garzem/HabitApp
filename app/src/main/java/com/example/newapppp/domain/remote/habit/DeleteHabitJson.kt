package com.example.newapppp.domain.remote.habit

import kotlinx.serialization.Serializable

@Serializable
data class DeleteHabitJson(
    val uid: String
)
