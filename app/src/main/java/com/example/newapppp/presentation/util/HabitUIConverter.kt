package com.example.newapppp.presentation.util

import com.example.newapppp.domain.model.Habit
import com.example.newapppp.domain.model.HabitColor
import com.example.newapppp.domain.model.HabitPriority
import com.example.newapppp.domain.model.HabitType
import com.example.newapppp.domain.model.HabitUI
import javax.inject.Inject

class HabitUIConverter @Inject constructor(
    private val timeConverter: TimeConverter
) {

    fun toHabitUI(habit: Habit): HabitUI {
        with(habit) {
            return HabitUI(
                id = id,
                uid = uid,
                title = title,
                description = description,
                creationDate = timeConverter.toDate(creationDate),
                color = color,
                priority = priority,
                type = type,
                frequency = frequency
            )
        }
    }

    fun toHabit(habitUI: HabitUI): Habit {
        with(habitUI) {
            return Habit(
                id = id,
                uid = uid,
                title = title,
                description = description,
                creationDate = timeConverter.toLong(creationDate),
                color = color,
                priority = priority,
                type = type,
                frequency = frequency
            )
        }
    }
}