package com.example.newapppp.presentation.util

import android.icu.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.inject.Inject

class TimeConverter @Inject constructor() {

    fun toDate(date: Long): String {
        val simpleDateFormat = SimpleDateFormat("yyyy/MM/dd", Locale.getDefault())
        return simpleDateFormat.format(Date(date))
    }

    fun toLong(dateString: String): Long {
        val simpleDateFormat = SimpleDateFormat("yyyy/MM/dd", Locale.getDefault())
        val date = simpleDateFormat.parse(dateString)
        return date?.time ?: 0
    }
}