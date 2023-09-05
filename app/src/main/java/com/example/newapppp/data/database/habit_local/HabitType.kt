package com.example.newapppp.data.database.habit_local

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
enum class HabitType : Parcelable {
    GOOD,
    BAD
}