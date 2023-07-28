package com.example.newapppp.data

import java.io.Serializable

data class UiState(
    val id: String?,
    val title: String,
    val titleCursorPosition: Int,
    val description: String,
    val descriptionCursorPosition: Int,
    val period: String,
    val periodCursorPosition: Int,
    val color: Int,
    val priorityPosition: Int,
    val type: Int,
    val quantity: String,
    val quantityCursorPosition: Int
) : Serializable