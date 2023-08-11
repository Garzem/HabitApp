package com.example.newapppp.habit_repository

import com.example.newapppp.data.Habit
import com.example.newapppp.data.HabitType
import com.example.newapppp.database.AppHabitDataBase
import com.example.newapppp.database.HabitEntity

class HabitRepository {
    suspend fun saveHabit(habit: Habit) {
        val convertedHabit = convertHabitToHabitEntity(habit)
        AppHabitDataBase.habitDao.upsertHabit(convertedHabit)
    }

    suspend fun deleteHabitById(habitId: String) {
        AppHabitDataBase.habitDao.deleteHabitById(habitId)
    }

    suspend fun getHabitById(habitId: String): Habit {
        return convertHabitEntityToHabit(AppHabitDataBase.habitDao.getHabitById(habitId))
    }

    suspend fun getHabitListByType(type: HabitType): List<Habit> {
        val habitListByType = AppHabitDataBase.habitDao.getHabitListByType(type)
        return habitListByType.map {
            convertHabitEntityToHabit(it)
        }
    }

    suspend fun getFilteredHabitByTitle(titleFilter: String, type: HabitType): List<Habit> {
        val filteredHabit =
            AppHabitDataBase.habitDao.getFilteredHabitByTitle(titleFilter, type)
        return filteredHabit.map {
            convertHabitEntityToHabit(it)
        }
    }

    suspend fun getFilteredHabitListByPriority(priorityFilter: String, type: HabitType):
            List<Habit> {
        val filteredHabitList =
            AppHabitDataBase.habitDao.getHabitListByPriority(priorityFilter, type)
        return filteredHabitList.map {
            convertHabitEntityToHabit(it)
        }
    }

    suspend fun getHabitListByTitleAndPriority(titleFilter: String, priorityFilter: String, type: HabitType):
            List<Habit> {
        val filteredHabitList =
            AppHabitDataBase.habitDao.getHabitListByTitleAndPriority(titleFilter, priorityFilter, type)
        return filteredHabitList.map {
            convertHabitEntityToHabit(it)
        }
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

    private fun convertHabitEntityToHabit(habitEntity: HabitEntity): Habit {
        return Habit(
            id = habitEntity.id,
            title = habitEntity.title,
            description = habitEntity.description,
            period = habitEntity.period,
            color = habitEntity.color,
            priority = habitEntity.priority,
            type = habitEntity.type,
            quantity = habitEntity.quantity,
        )
    }
}