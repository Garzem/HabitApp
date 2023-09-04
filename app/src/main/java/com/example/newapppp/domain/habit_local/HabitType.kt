package com.example.newapppp.domain.habit_local

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
enum class HabitType : Parcelable {
    GOOD,
    BAD
}