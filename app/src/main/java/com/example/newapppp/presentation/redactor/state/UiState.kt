package com.example.newapppp.presentation.redactor.state

import com.example.newapppp.presentation.abstracts.BaseState
import com.example.newapppp.domain.model.HabitColor
import com.example.newapppp.domain.model.HabitCount
import java.io.Serializable

data class UiState(
    val id: String?,
    val uid: String?,
    val title: String,
    val description: String,
    val color: HabitColor,
    val priority: Int,
    val selectedPriorityLocalized: String,
    val type: Int,
    val frequency: String,
    val count: Int,
    val selectedCountLocalized: String,
    val doneDates: List<Long>
) : Serializable, BaseState