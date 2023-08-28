package com.example.newapppp.ui.redactor

import com.example.newapppp.data.HabitColor
import java.io.Serializable

data class UiState(
    val id: String?,
    val title: String,
    val titleCursorPosition: Int,
    val description: String,
    val descriptionCursorPosition: Int,
    val creationDate: String?,
    val color: HabitColor,
    val priority: Int,
    val type: Int,
    val frequency: String,
    val frequencyCursorPosition: Int
) : Serializable