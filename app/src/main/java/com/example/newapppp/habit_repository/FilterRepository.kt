package com.example.newapppp.habit_repository

import com.example.newapppp.data.HabitPriority

object FilterRepository {
    var filterByTitle: String = ""
    var filterByPriority: HabitPriority = HabitPriority.CHOOSE
}