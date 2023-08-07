package com.example.newapppp.habitrepository

import com.example.newapppp.data.Habit
import com.example.newapppp.data.HabitType
import com.example.newapppp.database.HabitEntity
import com.example.newapppp.database.AppHabitDataBase

class HabitRepository {
    suspend fun saveHabit(habit: Habit) {
        val convertedHabit = convertHabitToHabitEntity(habit)
        AppHabitDataBase.habitDao.upsertHabit(convertedHabit)
    }

    suspend fun deleteHabitById(habitId: String) {
        AppHabitDataBase.habitDao.deleteHabitById(habitId)
    }

    fun getHabitById(habitId: String): Habit {
        AppHabitDataBase.habitDao.getHabitById(habitId)
    }

    fun getHabitListByType(type: HabitType): List<Habit> {
        AppHabitDataBase.habitDao.getHabitListByType()
    }

    fun setupHabitFromDataBase(habit: Habit) {

    }

    private fun convertHabitToHabitEntity(habit: Habit): HabitEntity {
        return HabitEntity(
            id = habit.id,
            title = habit.title,
            description = habit.description,
            period = habit.period,
            color = habit.color,
            priority = habit.priority,
            type = habit.type,
            quantity = habit.quantity,
        )
    }

//    private fun convertHabitEntityToHabit(habitEntity: HabitEntity): Habit {
//        return Habit(
//            id = habitEntity.id,
//            title = habitEntity.title,
//            description = habitEntity.description,
//            period = habitEntity.period,
//            color = habitEntity.color,
//            priority = habitEntity.priority,
//            type = habitEntity.type,
//            quantity = habitEntity.quantity,
//        )
//    }
}