package com.example.newapppp.habit_repository

import com.example.newapppp.HApp
import com.example.newapppp.data.Constants.TOKEN
import com.example.newapppp.data.habit_local.Habit
import com.example.newapppp.data.habit_local.HabitColor
import com.example.newapppp.data.habit_local.HabitPriority
import com.example.newapppp.data.habit_local.HabitType
import com.example.newapppp.data.remote.habit.HabitSave
import com.example.newapppp.data.remote.callWithRetry
import com.example.newapppp.data.remote.habit.DeleteHabitJson
import com.example.newapppp.data.remote.habit.HabitIdJson
import com.example.newapppp.data.remote.habit.PutHabitJson
import com.example.newapppp.data.remote.habit.GetHabitJson
import com.example.newapppp.database.AppHabitDataBase
import com.example.newapppp.database.HabitEntity
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import java.util.UUID


class HabitRepository {
    suspend fun saveHabit(habitSave: HabitSave) = coroutineScope {
        val habitUidAsync = async {
            putHabitWithRetry(TOKEN, toHabitJson(habitSave))
        }
        val habitEntityAsync = async {
            val habitEntity = toHabitEntity(habitSave)
            AppHabitDataBase.habitDao.upsertHabit(habitEntity)
            habitEntity
        }
        val habitUid = habitUidAsync.await()
        val habitEntity = habitEntityAsync.await()

        val habitEntityWithUid = habitEntity.copy(
            uid = habitUid.uid
        )

        AppHabitDataBase.habitDao.upsertHabit(habitEntityWithUid)
    }

    suspend fun getHabitList(): List<Habit> {
        val habitJsonList = getHabitWithRetry(TOKEN)
        val habitLocalList = AppHabitDataBase.habitDao.getAllHabits()
        val habitList = toHabitList(habitJsonList, habitLocalList)
        val convertedHabitList = habitList.map { habit ->
            convertHabitToHabitEntity(habit)
        }
        AppHabitDataBase.habitDao.upsertHabitList(convertedHabitList)
        return habitList
    }

    suspend fun deleteHabit(id: String) {
        val deleteRequest = DeleteHabitJson(id)
        deleteHabitWithRetry(deleteRequest)
        AppHabitDataBase.habitDao.deleteHabitById(id)
    }

    suspend fun deleteAllHabits() {
        val stringList = AppHabitDataBase.habitDao.getAllHabitsId()
        stringList.map { id ->
            deleteHabitWithRetry(DeleteHabitJson(id))
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

    private suspend fun putHabitWithRetry(token: String, putHabitJson: PutHabitJson): HabitIdJson {
        return callWithRetry {
            HApp.habitApi.putHabit(token, putHabitJson)
        }
    }

    private suspend fun getHabitWithRetry(token: String): List<GetHabitJson> {
        return callWithRetry {
            HApp.habitApi.getHabitList(token)
        }
    }

    private suspend fun deleteHabitWithRetry(deleteRequest: DeleteHabitJson) {
        callWithRetry {
            HApp.habitApi.deleteHabit(TOKEN, deleteRequest)
        }
    }

//    private suspend fun postHabitWithRetry(postHabitJson: PostHabitJson) {
//        callWithRetry {
//            HApp.habitApi.postHabit(TOKEN, postHabitJson)
//        }
//    }

    private fun toHabitJson(saveHabit: HabitSave): PutHabitJson {
        return with(saveHabit) {
            PutHabitJson(
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

    private fun toHabitEntity(habit: HabitSave): HabitEntity {
        return HabitEntity(
            id = UUID.randomUUID().toString(),
            uid = null,
            title = habit.title,
            description = habit.description,
            creationDate = habit.creationDate,
            color = habit.color,
            priority = habit.priority,
            type = habit.type,
            frequency = habit.frequency
        )
    }

    private fun toHabitList(
        getHabitJsonList: List<GetHabitJson>, habitLocalList: List<HabitEntity>
    ): List<Habit> {
        val habitLocalListWithUid = habitLocalList.filter { it.uid != null }

        return getHabitJsonList.map {
            val localHabit = habitLocalListWithUid.find { habit ->
                habit.uid == it.uid
            }
            Habit(
                id = localHabit?.id ?: UUID.randomUUID().toString(),
                uid = it.uid,
                title = it.title,
                description = it.description,
                creationDate = it.creationDate,
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
            uid = habit.uid,
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
            uid = habitEntity.uid,
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