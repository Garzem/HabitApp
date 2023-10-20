package com.example.newapppp.presentation.habit_list.mapper

import android.content.Context
import com.example.newapppp.R
import com.example.newapppp.domain.model.HabitPriority
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class HabitPriorityMapper @Inject constructor(@ApplicationContext private val context: Context) {

    fun getPriorityName(habitPriority: HabitPriority): String {
        val array = context.resources.getStringArray(R.array.priority_array_string)
        return array.getOrElse(habitPriority.ordinal) {
            array[HabitPriority.CHOOSE.ordinal]
        }
    }
}