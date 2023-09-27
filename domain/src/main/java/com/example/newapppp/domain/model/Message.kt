package com.example.newapppp.domain.model

sealed interface Message {

    class TimesLeftForBadHabit(val count: Int) : Message

    class TimesLeftForGoodHabit(val count: Int) : Message

    object StopDoingBadHabit : Message

    object MetOrExceededGoodHabit : Message

    object Error : Message
}