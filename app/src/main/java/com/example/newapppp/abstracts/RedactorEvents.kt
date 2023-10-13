package com.example.newapppp.abstracts

sealed interface RedactorEvents: BaseEvent {

    object ShowValidationError: RedactorEvents

    object GoBack: RedactorEvents
}