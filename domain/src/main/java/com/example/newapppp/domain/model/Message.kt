package com.example.newapppp.domain.model

sealed interface Message {

    data class TimesLeftForBadHabit(val remainingDays: Int) : Message

    data class TimesLeftForGoodHabit(val remainingDays: Int) : Message

    object StopDoingBadHabit : Message

    object MetOrExceededGoodHabit : Message

    object Error : Message
}