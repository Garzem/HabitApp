package com.example.newapppp.ui.redactor

import com.example.newapppp.data.HabitColor
import java.io.Serializable

data class UiState(
    val id: String,
    val title: String,
    val titleCursorPosition: Int,
    val description: String,
    val descriptionCursorPosition: Int,
    val period: String,
    val periodCursorPosition: Int,
    val color: HabitColor,
    val priorityPosition: Int,
    val type: Int,
    val quantity: String,
    val quantityCursorPosition: Int
) : Serializable