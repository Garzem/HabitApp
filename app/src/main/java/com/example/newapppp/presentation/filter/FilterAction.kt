package com.example.newapppp.presentation.filter

sealed interface FilterAction {

    data class OnTitleFilterChanged(val title: String): FilterAction

    data class OnPriorityFilterChanged(val priority: Int): FilterAction

    object OnFilterButtonClick: FilterAction

    object OnCancelClick: FilterAction

}