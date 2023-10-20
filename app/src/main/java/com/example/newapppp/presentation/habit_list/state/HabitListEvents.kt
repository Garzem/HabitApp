package com.example.newapppp.presentation.habit_list.state

import com.example.newapppp.presentation.abstracts.BaseEvent
import com.example.newapppp.domain.model.Message

sealed interface HabitListEvents: BaseEvent {

    data class ShowMessage(val message: Message) : HabitListEvents

}