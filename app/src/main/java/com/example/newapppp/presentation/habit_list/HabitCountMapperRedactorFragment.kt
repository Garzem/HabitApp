package com.example.newapppp.presentation.habit_list

import android.content.Context
import com.example.newapppp.R
import com.example.newapppp.domain.model.HabitCount
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class HabitCountMapperRedactorFragment @Inject constructor(@ApplicationContext private val context: Context) {

    fun getCountName(count: HabitCount): String {
        return when (count) {
            HabitCount.CHOOSE -> context.getString(R.string.count_choose)
            HabitCount.WEEK -> context.getString(R.string.count_week)
            HabitCount.MONTH -> context.getString(R.string.count_month)
            HabitCount.YEAR -> context.getString(R.string.count_year)
        }
    }
}