package com.example.newapppp.habitrepository

import com.example.newapppp.data.Habit
import com.example.newapppp.data.HabitEntity
import com.example.newapppp.data.Type
import com.example.newapppp.database.AppHabitDataBase

class HabitRepository {
    fun saveHabit(habit: Habit) {
        AppHabitDataBase.habitDao.upsertHabit(habit)
    }

    fun deleteHabit(habit: Habit) {
        AppHabitDataBase.habitDao.deleteHabit(habit)
    }

    fun getHabitById(habitId: String): HabitEntity {
        AppHabitDataBase.habitDao.getHabitById(habitId)
    }

    fun getHabitListById(type: Type) : HabitEntity {
        AppHabitDataBase.habitDao.getHabitListByType()
    }

    fun setupHabitFromDataBase() {

    }
}