package com.example.newapppp.presentation.habit_list.mapper

import android.content.Context
import com.example.newapppp.R
import com.example.newapppp.domain.model.HabitPriority
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class HabitPriorityMapper @Inject constructor(@ApplicationContext private val context: Context) {

    fun getPriorityName(priority: HabitPriority): String {
        return when (priority) {
            HabitPriority.CHOOSE -> context.getString(R.string.priority_choose)
            HabitPriority.LOW -> context.getString(R.string.priority_low)
            HabitPriority.MEDIUM -> context.getString(R.string.priority_medium)
            HabitPriority.HIGH -> context.getString(R.string.priority_high)
        }
    }
}