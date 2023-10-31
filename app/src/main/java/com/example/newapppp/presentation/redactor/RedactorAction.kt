package com.example.newapppp.presentation.redactor

import com.example.newapppp.domain.model.HabitType

sealed interface RedactorAction {

    data class OnTitleChanged(val title: String): RedactorAction

    data class OnDescriptionChanged(val description: String): RedactorAction

    data class OnFrequencyChanged(val frequency: String): RedactorAction

    data class OnPriorityChanged(val priorityIndex: Int): RedactorAction

    data class OnCountChanged(val countIndex: Int): RedactorAction

    data class OnRadioButtonClick(val type: HabitType): RedactorAction

    object OnSaveButtonClick: RedactorAction

    object OnDeleteButtonClick: RedactorAction

}