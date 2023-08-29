package com.example.newapppp.habit_repository

import com.example.newapppp.HApp
import com.example.newapppp.data.Constants.TOKEN
import com.example.newapppp.data.habit_local.Habit
import com.example.newapppp.data.habit_local.HabitColor
import com.example.newapppp.data.habit_local.HabitPriority
import com.example.newapppp.data.habit_local.HabitType
import com.example.newapppp.data.remote.habit.HabitSave
import com.example.newapppp.data.TimeConverter.toDate
import com.example.newapppp.data.TimeConverter.toLong
import com.example.newapppp.data.remote.callWithRetry
import com.example.newapppp.data.remote.habit.HabitDeleteRequest
import com.example.newapppp.data.remote.habit.HabitDone
import com.example.newapppp.data.remote.habit.HabitIdJson
import com.example.newapppp.data.remote.habit.HabitJson
import com.example.newapppp.data.remote.habit.HabitServer
import com.example.newapppp.database.AppHabitDataBase
import com.example.newapppp.database.HabitEntity
import kotlinx.coroutines.delay
import retrofit2.HttpException


class HabitRepository {
    suspend fun saveAndPostHabit(habitSave: HabitSave) {
        val habitId = putHabitWithRetry(TOKEN, toHabitJson(habitSave))
        postHabitWithRetry(
            HabitDone(
                id = habitId.id, creationDate = habitSave.creationDate.toInt()
            )
        )
        val habitEntity = toHabitEntity(habitId.id, habitSave)
        AppHabitDataBase.habitDao.upsertHabit(habitEntity)
    }

    suspend fun getHabitList(): List<Habit> {
        val habitServerList = getHabitWithRetry(TOKEN)
        val habitList = toHabitList(habitServerList)
        val convertedHabitList = habitList.map { habit ->
            convertHabitToHabitEntity(habit)
        }
        AppHabitDataBase.habitDao.upsertHabitList(convertedHabitList)
        return habitList
    }

    suspend fun deleteHabit(id: String) {
        val deleteRequest = HabitDeleteRequest(id)
        deleteHabitWithRetry(deleteRequest)
        AppHabitDataBase.habitDao.deleteHabitById(id)
    }

    suspend fun deleteAllHabits() {
        val stringList = AppHabitDataBase.habitDao.getAllHabitsId()
        stringList.map { id ->
            deleteHabitWithRetry(HabitDeleteRequest(id))
        }
        AppHabitDataBase.habitDao.deleteAllHabits()
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

    private suspend fun getHabitWithRetry(token: String): List<HabitServer> {
        return callWithRetry {
            HApp.habitApi.getHabitList(token)
        }
    }

    private suspend fun deleteHabitWithRetry(deleteRequest: HabitDeleteRequest) {
        callWithRetry {
            HApp.habitApi.deleteHabit(TOKEN, deleteRequest)
        }
    }

    private suspend fun postHabitWithRetry(habitDone: HabitDone) {
        callWithRetry {
            HApp.habitApi.postHabit(TOKEN, habitDone)
        }
    }

    private fun toHabitJson(saveHabit: HabitSave): HabitJson {
        return with(saveHabit) {
            HabitJson(
                title = title,
                description = description,
                creationDate = creationDate.toInt(),
                color = HabitColor.values().indexOf(color),
                priority = HabitPriority.values().indexOf(priority),
                type = HabitType.values().indexOf(type),
                frequency = frequency,
                count = 0
            )
        }
    }

    private fun toHabitEntity(habitId: String, habit: HabitSave): HabitEntity {
        return HabitEntity(
            id = habitId,
            title = habit.title,
            description = habit.description,
            creationDate = toDate(habit.creationDate),
            color = habit.color,
            priority = habit.priority,
            type = habit.type,
            frequency = habit.frequency
        )
    }

    private fun toHabitList(habitServerList: List<HabitServer>): List<Habit> {
        return habitServerList.map {
            Habit(
                id = it.id,
                title = it.title,
                description = it.description,
                creationDate = toDate(it.creationDate.toLong()),
                color = HabitColor.values().getOrNull(it.color) ?: HabitColor.ORANGE,
                priority = HabitPriority.values().getOrNull(it.priority)
                    ?: HabitPriority.CHOOSE,
                type = HabitType.values().getOrNull(it.type) ?: HabitType.GOOD,
                frequency = it.frequency
            )
        }
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