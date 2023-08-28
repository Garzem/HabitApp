package com.example.newapppp.habit_repository

import com.example.newapppp.HApp
import com.example.newapppp.data.Constants
import com.example.newapppp.data.Habit
import com.example.newapppp.data.HabitColor
import com.example.newapppp.data.HabitPriority
import com.example.newapppp.data.HabitType
import com.example.newapppp.data.SaveHabit
import com.example.newapppp.data.remote.habit.HabitIdJson
import com.example.newapppp.data.remote.habit.HabitJson
import com.example.newapppp.database.AppHabitDataBase
import com.example.newapppp.database.HabitEntity
import kotlinx.coroutines.delay
import retrofit2.HttpException


class HabitRepository {
    suspend fun saveHabit(saveHabit: SaveHabit) {
        val habitId = putHabitWithRetry(Constants.TOKEN, toHabitJson(saveHabit))
        val habitEntity = toHabitEntity(habitId.id, saveHabit)
        AppHabitDataBase.habitDao.upsertHabit(habitEntity)
    }

    suspend fun sendHabitList(habitList: List<Habit>) {
        val convertedHabitList = habitList.map { habit ->
            convertHabitToHabitEntity(habit)
        }
        AppHabitDataBase.habitDao.upsertHabitList(convertedHabitList)
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

    private suspend fun putHabitWithRetry(token: String, habitJson: HabitJson): HabitIdJson {
        return callWithRetry {
            HApp.habitApi.putHabit(token, habitJson)
        }
    }

    private suspend inline fun <T> callWithRetry(block: () -> T): T {
        var currentRetryCount = 0

        while (true) {
            try {
                return block()
            } catch (e: HttpException) {
                if (currentRetryCount > 3) {
                    throw e
                }
                currentRetryCount++
                delay(currentRetryCount * 1_000L)
            }
        }
    }

    private fun toHabitJson(saveHabit: SaveHabit): HabitJson {
        return with(saveHabit) {
            HabitJson(
                title = title,
                description = description,
                creationDate = creationDate,
                color = HabitColor.values().indexOf(color),
                priority = HabitPriority.values().indexOf(priority),
                type = HabitType.values().indexOf(type),
                frequency = frequency,
                count = 0
            )
        }
    }

    private fun toHabitEntity(habitId: String, habit: SaveHabit): HabitEntity {
        return HabitEntity(
            id = habitId,
            title = habit.title,
            description = habit.description,
            creationDate = habit.creationDate,
            color = habit.color,
            priority = habit.priority,
            type = habit.type,
            frequency = habit.frequency
        )
    }

    private fun convertHabitToHabitEntity(habit: Habit): HabitEntity {
        return HabitEntity(
            id = habit.id,
            title = habit.title,
            description = habit.description,
            creationDate = habit.creationDate,
            color = habit.color,
            priority = habit.priority,
            type = habit.type,
            frequency = habit.frequency,
        )
    }

    private fun convertHabitEntityToHabit(habitEntity: HabitEntity): Habit {
        return Habit(
            id = habitEntity.id,
            title = habitEntity.title,
            description = habitEntity.description,
            creationDate = habitEntity.creationDate,
            color = habitEntity.color,
            priority = habitEntity.priority,
            type = habitEntity.type,
            frequency = habitEntity.frequency,
        )
    }
}