package com.example.newapppp.presentation.redactor.state

import com.example.newapppp.abstracts.BaseState
import com.example.newapppp.domain.model.HabitColor
import com.example.newapppp.domain.model.HabitCount
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
    val count: Int,
    val doneDates: List<Long>
) : Serializable, BaseState