package com.example.newapppp.presentation.habit_list.mapper

import android.content.Context
import com.example.newapppp.R
import com.example.newapppp.domain.model.HabitCount
import com.example.newapppp.domain.model.HabitPriority
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class HabitCountMapper @Inject constructor(@ApplicationContext private val context: Context) {

    fun getCountName(count: HabitCount): String {
        val array = context.resources.getStringArray(R.array.count_array_string)
        return array.getOrElse(count.ordinal) {
            array[HabitCount.CHOOSE.ordinal]
        }
    }
    fun getCountNameHabitFragment(count: HabitCount): String {
        return when (count) {
            HabitCount.CHOOSE -> context.getString(R.string.count_choose_ui)
            HabitCount.WEEK -> context.getString(R.string.count_week_ui)
            HabitCount.MONTH -> context.getString(R.string.count_month_ui)
            HabitCount.YEAR -> context.getString(R.string.count_year_ui)
        }
    }
}