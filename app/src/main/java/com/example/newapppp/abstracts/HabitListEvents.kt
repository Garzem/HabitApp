package com.example.newapppp.abstracts

import com.example.newapppp.domain.model.Message

sealed interface HabitListEvents: BaseEvent {

    data class ShowMessage(val message: Message) : HabitListEvents

}