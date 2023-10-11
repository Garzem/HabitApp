package com.example.newapppp.abstracts

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

abstract class BaseViewModel<State: BaseState, Event: BaseEvent>(
    initState: State
): ViewModel() {

    protected val _state = MutableStateFlow(initState)
    val state = _state.asStateFlow()

    protected val _events = MutableStateFlow<Event?>(null)
    val event = _events.asStateFlow()

    fun consumeEvents() {
        _events.update { null }
    }
}