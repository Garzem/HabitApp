package com.example.newapppp.presentation.redactor.state

import com.example.newapppp.domain.model.HabitColor
import java.io.Serializable

data class UiState(
    val id: String?,
    val uid: String?,
    val title: String,
    val titleCursorPosition: Int,
    val description: String,
    val descriptionCursorPosition: Int,
    val color: HabitColor,
    val priority: Int,
    val type: Int,
    val frequency: String,
    val frequencyCursorPosition: Int,
    val doneDates: List<Int>
) : Serializable