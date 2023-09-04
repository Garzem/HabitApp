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
    suspend fun saveOrUpdateHabit(habitSave: HabitSave, habitId: String?) = coroutineScope {
        val habitUidAsync = async {
            putHabitWithRetry(TOKEN, toHabitJson(habitSave))
        }
        val habitEntityAsync = async {
            val habitEntity = toHabitEntity(habitSave, habitId)
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

    suspend fun getHabitList(): List<Habit> = coroutineScope {
        val habitJsonListAsync = async {
            getHabitWithRetry(TOKEN)
        }
        val habitLocalListAsync = async {
            AppHabitDataBase.habitDao.getAllHabits()
        }
        val habitJsonList = habitJsonListAsync.await()
        val habitEntityList = habitLocalListAsync.await()

        val notSavedHabitJsonList = habitJsonList.filter { getHabitJson ->
            habitEntityList.none { habitEntity ->
                habitEntity.uid == getHabitJson.uid
            }
        }

        val notSavedHabitEntityList = notSavedHabitJsonList.map(this@HabitRepository::toHabitEntity)
        AppHabitDataBase.habitDao.upsertHabitList(notSavedHabitEntityList)

        (notSavedHabitEntityList + habitEntityList).map(::toHabit).sortedBy { habit ->
            habit.creationDate
        }
    }

    suspend fun deleteHabit(habit: Habit) {
        val response = habit.uid?.let { deleteHabitWithRetry(DeleteHabitJson(habit.uid)) }
        if (response == null || response.isSuccess) {
            AppHabitDataBase.habitDao.deleteHabitById(habit.id)
        } else {
            val habitEntity = toHabitEntity(habit).copy(deleted = true)
            AppHabitDataBase.habitDao.upsertHabit(habitEntity)
        }
    }

    suspend fun deleteAllHabits() {
        val filteredStringList = AppHabitDataBase.habitDao.getAllHabitsId().filterNotNull()
        filteredStringList.map { uid ->
            deleteHabitWithRetry(DeleteHabitJson(uid))
        }
        AppHabitDataBase.habitDao.deleteAllHabits()
    }

    suspend fun getHabitById(habitId: String): Habit {
        return toHabit(AppHabitDataBase.habitDao.getHabitById(habitId))
    }

    suspend fun getHabitListByType(type: HabitType): List<Habit> {
        val habitListByType = AppHabitDataBase.habitDao.getHabitListByType(type)
        return habitListByType.map {
            toHabit(it)
        }
    }

    private suspend fun putHabitWithRetry(token: String, putHabitJson: PutHabitJson): HabitIdJson {
        return callWithRetry {
            HApp.habitApi.putHabit(token, putHabitJson)
        }.getOrThrow()
    }

    private suspend fun getHabitWithRetry(token: String): List<GetHabitJson> {
        return callWithRetry {
            HApp.habitApi.getHabitList(token)
        }.getOrThrow()
    }

    private suspend fun deleteHabitWithRetry(deleteRequest: DeleteHabitJson): Result<Unit> {
        return callWithRetry {
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
                uid = null,
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

    private fun toHabitEntity(habit: HabitSave, habitId: String?): HabitEntity {
        return HabitEntity(
            id = habitId ?: UUID.randomUUID().toString(),
            uid = null,
            title = habit.title,
            description = habit.description,
            creationDate = habit.creationDate,
            color = habit.color,
            priority = habit.priority,
            type = habit.type,
            frequency = habit.frequency,
            deleted = false
        )
    }

    private fun toHabitEntity(habit: GetHabitJson): HabitEntity {
        return with(habit) {
            HabitEntity(
                id = UUID.randomUUID().toString(),
                uid = uid,
                title = title,
                description = description,
                creationDate = creationDate,
                color = HabitColor.values().getOrNull(color) ?: HabitColor.ORANGE,
                priority = HabitPriority.values().getOrNull(priority) ?: HabitPriority.CHOOSE,
                type = HabitType.values().getOrNull(type) ?: HabitType.GOOD,
                frequency = frequency,
                deleted = false
            )
        }
    }

    private fun toHabitEntity(habit: Habit): HabitEntity {
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
            deleted = false
        )
    }

    private fun toHabit(habitEntity: HabitEntity): Habit {
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