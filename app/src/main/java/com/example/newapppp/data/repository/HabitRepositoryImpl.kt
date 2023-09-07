package com.example.newapppp.data.repository

import com.example.newapppp.data.database.HabitDao
import com.example.newapppp.domain.Constants.TOKEN
import com.example.newapppp.domain.model.Habit
import com.example.newapppp.domain.model.HabitColor
import com.example.newapppp.domain.model.HabitPriority
import com.example.newapppp.domain.model.HabitType
import com.example.newapppp.data.remote.habit.HabitSave
import com.example.newapppp.data.remote.callWithRetry
import com.example.newapppp.data.remote.habit.DeleteHabitJson
import com.example.newapppp.data.remote.habit.HabitIdJson
import com.example.newapppp.data.remote.habit.PutHabitJson
import com.example.newapppp.data.remote.habit.GetHabitJson
import com.example.newapppp.data.database.habit_local.HabitEntity
import com.example.newapppp.data.remote.habit.HabitApi
import com.example.newapppp.domain.repository.HabitRepository
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import java.util.UUID
import javax.inject.Inject


class HabitRepositoryImpl @Inject constructor(
    private val api: HabitApi,
    private val habitDao: HabitDao
): HabitRepository {

    override suspend fun fetchHabitList(): List<Habit> = coroutineScope {
        val habitJsonListResponseAsync = async {
            callWithRetry {
                api.getHabitList(TOKEN)
            }
        }
        val habitLocalListAsync = async {
            habitDao.getAllHabits()
        }
        val habitJsonList = habitJsonListResponseAsync.await().getOrNull()
        val habitEntityList = habitLocalListAsync.await()

        val fullHabitEntityList = if (habitJsonList.isNullOrEmpty()) {
            habitEntityList
        } else {
            val notSavedHabitJsonList = habitJsonList.filter { getHabitJson ->
                habitEntityList.none { habitEntity ->
                    habitEntity.uid == getHabitJson.uid
                }
            }
            val notSavedHabitEntityList = notSavedHabitJsonList.map(this@HabitRepositoryImpl::toHabitEntity)
            habitDao.upsertHabitList(notSavedHabitEntityList)
            notSavedHabitEntityList + habitEntityList
        }

        fullHabitEntityList.map(::toHabit)
    }
    override suspend fun saveOrUpdateHabit(habitSave: HabitSave, habitId: String?) = coroutineScope {
        val habitUidAsync = async {
            putHabitWithRetry(TOKEN, toHabitJson(habitSave))
        }
        val habitEntityAsync = async {
            val habitEntity = toHabitEntity(habitSave, habitId)
            habitDao.upsertHabit(habitEntity)
            habitEntity
        }
        val habitUid = habitUidAsync.await()
        val habitEntity = habitEntityAsync.await()

        val habitEntityWithUid = habitEntity.copy(
            uid = habitUid.uid
        )

        habitDao.upsertHabit(habitEntityWithUid)
    }

    override suspend fun getHabitList(): List<Habit> = coroutineScope {
        val habitJsonListAsync = async {
            getHabitWithRetry(TOKEN)
        }
        val habitLocalListAsync = async {
            habitDao.getAllHabits()
        }
        val habitJsonList = habitJsonListAsync.await()
        val habitEntityList = habitLocalListAsync.await()

        val notSavedHabitJsonList = habitJsonList.filter { getHabitJson ->
            habitEntityList.none { habitEntity ->
                habitEntity.uid == getHabitJson.uid
            }
        }

        val notSavedHabitEntityList = notSavedHabitJsonList.map(this@HabitRepositoryImpl::toHabitEntity)
        habitDao.upsertHabitList(notSavedHabitEntityList)

        (notSavedHabitEntityList + habitEntityList).map(::toHabit).sortedBy { habit ->
            habit.creationDate
        }
    }

    override suspend fun deleteHabit(habit: Habit) {
        val response = habit.uid?.let { deleteHabitWithRetry(DeleteHabitJson(habit.uid)) }
        if (response == null || response.isSuccess) {
            habitDao.deleteHabitById(habit.id)
        } else {
            val habitEntity = toHabitEntity(habit).copy(deleted = true)
            habitDao.upsertHabit(habitEntity)
        }
    }

    override suspend fun deleteAllHabits() {
        val habitList = habitDao.getAllHabits()
        habitList.map { habit ->
            val response = habit.uid?.let { deleteHabitWithRetry(DeleteHabitJson(habit.uid)) }
            if (response == null || response.isSuccess) {
                habitDao.deleteHabitById(habit.id)
            } else {
                val habitDeleted = habit.copy(deleted = true)
                habitDao.upsertHabit(habitDeleted)
            }
        }
    }

    override suspend fun getHabitById(habitId: String): Habit {
        return toHabit(habitDao.getHabitById(habitId))
    }

    override suspend fun getHabitListByType(type: HabitType): List<Habit> {
        val habitListByType = habitDao.getHabitListByType(type)
        return habitListByType.map {
            toHabit(it)
        }
    }

    override suspend fun putHabitWithRetry(token: String, putHabitJson: PutHabitJson): HabitIdJson {
        return callWithRetry {
            api.putHabit(token, putHabitJson)
        }.getOrThrow()
    }

    override suspend fun getHabitWithRetry(token: String): List<GetHabitJson> {
        return callWithRetry {
            api.getHabitList(token)
        }.getOrThrow()
    }

    override suspend fun deleteHabitWithRetry(deleteRequest: DeleteHabitJson): Result<Unit> {
        return callWithRetry {
            api.deleteHabit(TOKEN, deleteRequest)
        }
    }

//    private suspend fun postHabitWithRetry(postHabitJson: PostHabitJson) {
//        callWithRetry {
//            HApp.habitApi.postHabit(TOKEN, postHabitJson)
//        }
//    }

    override fun toHabitJson(saveHabit: HabitSave): PutHabitJson {
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

    override fun toHabitEntity(habit: HabitSave, habitId: String?): HabitEntity {
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

    override fun toHabitEntity(habit: GetHabitJson): HabitEntity {
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

    override fun toHabitEntity(habit: Habit): HabitEntity {
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

    override fun toHabit(habitEntity: HabitEntity): Habit {
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