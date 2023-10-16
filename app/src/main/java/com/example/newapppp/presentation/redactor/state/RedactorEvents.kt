package com.example.newapppp.presentation.redactor.state

import com.example.newapppp.abstracts.BaseEvent

sealed interface RedactorEvents: BaseEvent {

    object ShowValidationError: RedactorEvents

    object GoBack: RedactorEvents
}