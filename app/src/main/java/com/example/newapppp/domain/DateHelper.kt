package com.example.newapppp.domain

import com.example.newapppp.domain.model.HabitCount

interface DateHelper {

    fun getCurrentDate(): Long

    fun getStartDate(date: Long, period: HabitCount): Long
}