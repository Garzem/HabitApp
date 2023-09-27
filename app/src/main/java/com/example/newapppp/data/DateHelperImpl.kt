package com.example.newapppp.data

import com.example.newapppp.domain.DateHelper
import com.example.newapppp.domain.model.HabitCount
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.temporal.TemporalAdjusters
import javax.inject.Inject

class DateHelperImpl @Inject constructor(): DateHelper {

    override fun getCurrentDate(): Long {
        return LocalDate.now().toEpochDay()
    }

    override fun getStartDate(date: Long, period: HabitCount): Long {
        val currentDate = LocalDate.ofEpochDay(date)
        return when (period) {
            HabitCount.WEEK ->
                currentDate.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY)).toEpochDay()
            HabitCount.MONTH ->
                currentDate.with(TemporalAdjusters.firstDayOfMonth()).toEpochDay()
            HabitCount.YEAR ->
                currentDate.with(TemporalAdjusters.firstDayOfYear()).toEpochDay()
            else -> throw IllegalStateException("Unsupported habit count: $period")
        }
    }
}