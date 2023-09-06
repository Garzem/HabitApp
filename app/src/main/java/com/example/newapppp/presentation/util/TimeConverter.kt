package com.example.newapppp.presentation.util

import android.icu.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object TimeConverter {

    fun toDate(date: Long): String {
        val simpleDateFormat = SimpleDateFormat("yyyy/MM/dd", Locale.getDefault())
        return simpleDateFormat.format(Date(date))
    }
}