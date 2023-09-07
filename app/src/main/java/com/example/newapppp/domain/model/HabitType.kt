package com.example.newapppp.domain.model

import android.os.Parcelable
import kotlinx.parcelize.IgnoredOnParcel
import kotlinx.parcelize.Parcelize

@Parcelize
enum class HabitType : Parcelable {
    GOOD,
    BAD;

    @IgnoredOnParcel
    val index: Int = values().indexOf(this)
}