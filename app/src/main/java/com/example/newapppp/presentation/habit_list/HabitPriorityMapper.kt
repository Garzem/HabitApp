package com.example.newapppp.presentation.habit_list

import android.content.Context
import com.example.newapppp.R
import com.example.newapppp.domain.model.HabitPriority

class HabitPriorityMapper(private val context: Context) {

    fun getPriorityName(priority: HabitPriority): String {
        return when (priority) {
            HabitPriority.CHOOSE -> context.getString(R.string.priority_choose)
            HabitPriority.LOW -> context.getString(R.string.priority_low)
            HabitPriority.MEDIUM -> context.getString(R.string.priority_medium)
            HabitPriority.HIGH -> context.getString(R.string.priority_high)
        }
    }
}