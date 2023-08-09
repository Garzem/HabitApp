package com.example.newapppp.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
enum class HabitType : Parcelable {
    GOOD,
    BAD
}