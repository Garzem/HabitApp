package com.example.newapppp.presentation.filter.state

import com.example.newapppp.abstracts.BaseEvent
import com.example.newapppp.presentation.redactor.state.RedactorEvents

sealed interface FilterEvent: BaseEvent {

    object ShowErrorToast: FilterEvent

    object GoBack: FilterEvent
}