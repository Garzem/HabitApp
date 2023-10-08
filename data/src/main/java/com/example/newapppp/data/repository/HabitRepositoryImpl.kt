package com.example.newapppp.data.repository

import com.example.newapppp.data.UuidGenerator
import com.example.newapppp.data.database.HabitDao
import com.example.newapppp.data.database.habit_local.HabitEntity
import com.example.newapppp.data.remote.HabitApi
import com.example.newapppp.data.remote.NetworkRetry
import com.example.newapppp.data.remote.model.DeleteHabitJson
import com.example.newapppp.data.remote.model.GetHabitJson
import com.example.newapppp.data.remote.model.PostHabitJson
import com.example.newapppp.data.remote.model.PutHabitJson
import com.example.newapppp.domain.Constants.TOKEN
import com.example.newapppp.domain.model.Habit
import com.example.newapppp.domain.model.HabitColor
import com.example.newapppp.domain.model.HabitCount
import com.example.newapppp.domain.model.HabitPriority
import com.example.newapppp.domain.model.HabitSave
import com.example.newapppp.domain.model.HabitType
import com.example.newapppp.domain.repository.HabitRepository
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject


class HabitRepositoryImpl @Inject constructor(
    private val api: HabitApi,
    private val habitDao: HabitDao,
    private val networkRetry: NetworkRetry,
    private val uuidGenerator: UuidGenerator
) : HabitRepository {

    override fun getHabitListFlow(): Flow<List<Habit>> {
        return habitDao.getAllHabitsFlow().map { habitEntityList ->
            habitEntityList.map(::toHabit)
        }
    }

    override suspend fun saveOrUpdateHabit(habitSave: HabitSave, habitId: String?) =
        coroutineScope {
            val habitEntity = toHabitEntity(habitSave, habitId)
            launch(IO) {
                habitDao.upsertHabit(habitEntity)
            }
            launch(IO) {
                try {
                    val habitUidJson = api.putHabit(TOKEN, toHabitJson(habitSave))
                    val habitEntityWithUid = habitEntity.copy(uid = habitUidJson.uid)
                    habitDao.upsertHabit(habitEntityWithUid)
                } catch (e: Exception) {
                    return@launch
                }
            }
        }

    override suspend fun deleteHabit(habit: Habit) {
        withContext(IO) {
            if (habit.uid == null) {
                habitDao.deleteHabitById(habit.id)
            } else {
                val habitDeleted = toHabitEntity(habit).copy(deleted = true)
                habitDao.upsertHabit(habitDeleted)
                try {
                    habitDeleted.uid?.let {
                        api.deleteHabit(TOKEN, DeleteHabitJson(habitDeleted.uid))
                    }
                    habitDao.deleteHabitById(habitDeleted.id)
                } catch (e: Exception) {
                    return@withContext
                }
            }
        }
    }


    override suspend fun deleteAllHabits() {
        withContext(IO) {
            val habitListFlow = habitDao.getAllHabits()
            habitListFlow.forEach { habitEntity ->
                if (habitEntity.uid == null) {
                    habitDao.deleteHabitById(habitEntity.id)
                } else {
                    val habitDeleted = habitEntity.copy(deleted = true)
                    habitDao.upsertHabit(habitDeleted)
                    habitDeleted.uid?.let {
                        try {
                            api.deleteHabit(TOKEN, DeleteHabitJson(habitDeleted.uid))
                            habitDao.deleteHabitById(habitDeleted.id)
                        } catch (e: Exception) {
                            return@withContext
                        }
                    }
                }
            }
        }
    }


    override suspend fun updateHabitDates(habitId: String, date: Long): Habit {
        return coroutineScope {
            val habitEntity = habitDao.getHabitById(habitId)
            val updatedHabit = habitEntity.copy(
                doneDates = habitEntity.doneDates + date
            )
            launch {
                habitDao.upsertHabit(updatedHabit)
            }
            launch {
                habitEntity.uid?.let { uid ->
                    try {
                        api.postHabit(TOKEN, PostHabitJson(date, uid))
                    } catch (e: Exception) {
                        return@launch
                    }
                }
            }
            toHabit(updatedHabit)
        }
    }

    override suspend fun fetchHabitList() = coroutineScope {
        val habitJsonListResponseAsync =
            async {
                networkRetry.commonRetrying(null) {
                    api.getHabitList(TOKEN)
                }
            }
        val habitLocalListAsync = async {
            habitDao.getAllHabits()
        }
        val habitJsonList = habitJsonListResponseAsync.await()
        val habitEntityList = habitLocalListAsync.await()

        if (!habitJsonList.isNullOrEmpty()) {
            val notSavedHabitJsonList = habitJsonList.filter { getHabitJson ->
                habitEntityList.none { habitEntity ->
                    habitEntity.uid == getHabitJson.uid
                           // || !habitEntity.deleted
                }
            }
            val notSavedHabitEntityList =
                notSavedHabitJsonList.map(this@HabitRepositoryImpl::toHabitEntity)
            habitDao.upsertHabitList(notSavedHabitEntityList)
        }
    }

    override suspend fun deleteOfflineDeletedHabits() {
        val deletedOfflineHabitList = habitDao.getAllDeletedHabits()
        deletedOfflineHabitList.forEach { deletedHabitEntity ->
            val response = deletedHabitEntity.uid?.let {
                networkRetry.commonRetrying(null) {
                    api.deleteHabit(TOKEN, DeleteHabitJson(deletedHabitEntity.uid))
                }
            }
            if (response != null) {
                habitDao.deleteHabitById(deletedHabitEntity.id)
            }
        }
    }

    override suspend fun putOfflineHabitList() {
        val habitJsonList = networkRetry.commonRetrying(null) {
            api.getHabitList(TOKEN)
        }
        if (habitJsonList != null) {
            val localHabitEntityList = habitDao.getAllHabits()
            val notSavedOfflineHabitList = localHabitEntityList.filter { habitEntity ->
                habitJsonList.none { getHabitJson ->
                    getHabitJson.uid == habitEntity.uid
                }
            }
            notSavedOfflineHabitList.forEach { habitEntity ->
                val habitUid = networkRetry.commonRetrying(null) {
                    api.putHabit(TOKEN, toHabitJson(habitEntity))
                }
                if (habitUid != null) {
                    val habitEntityWithUid = habitEntity.copy(
                        uid = habitUid.uid
                    )
                    habitDao.upsertHabit(habitEntityWithUid)
                }
            }
        }
    }


    override suspend fun getHabitById(habitId: String): Habit {
        return toHabit(habitDao.getHabitById(habitId))
    }


    override suspend fun postOfflineHabitList() {
        val habitEntityList = habitDao.getAllHabits()
        val getHabitJsonList = networkRetry.commonRetrying(null) {
            api.getHabitList(TOKEN)
        }
        if (getHabitJsonList != null) {
            habitEntityList.flatMap { habitEntity ->
                val habitJson = getHabitJsonList.find { it.uid == habitEntity.uid }
                if (habitJson != null) {
                    habitEntity.doneDates.filter { localDoneDate ->
                        !habitJson.doneDates.contains(localDoneDate)
                    }.map { doneDate ->
                        PostHabitJson(
                            doneDate = doneDate,
                            uid = habitJson.uid
                        )
                    }
                } else {
                    emptyList()
                }
            }.forEach {
                networkRetry.commonRetrying(null) {
                    api.postHabit(TOKEN, it)
                }
            }
        }
    }


    private fun toHabitJson(habitSave: HabitSave): PutHabitJson {
        return with(habitSave) {
            PutHabitJson(
                uid = null,
                title = title,
                description = description,
                creationDate = creationDate,
                color = HabitColor.values().indexOf(color),
                priority = HabitPriority.values().indexOf(priority),
                type = HabitType.values().indexOf(type),
                frequency = frequency,
                count = 0,
                doneDates = doneDates
            )
        }
    }

    private fun toHabitJson(habit: HabitEntity): PutHabitJson {
        return with(habit) {
            PutHabitJson(
                uid = null,
                title = title,
                description = description,
                creationDate = creationDate,
                color = HabitColor.values().indexOf(color),
                priority = HabitPriority.values().indexOf(priority),
                type = HabitType.values().indexOf(type),
                frequency = frequency,
                count = 0,
                doneDates = doneDates
            )
        }
    }

    private fun toHabitEntity(habit: HabitSave, habitId: String?): HabitEntity {
        return with(habit) {
            HabitEntity(
                id = habitId ?: uuidGenerator.generateUuid(),
                uid = null,
                title = title,
                description = description,
                creationDate = creationDate,
                color = color,
                priority = priority,
                type = type,
                frequency = frequency,
                doneDates = doneDates,
                count = count,
                deleted = false
            )
        }
    }

    private fun toHabitEntity(habit: GetHabitJson): HabitEntity {
        return with(habit) {
            HabitEntity(
                id = uuidGenerator.generateUuid(),
                uid = uid,
                title = title,
                description = description,
                creationDate = creationDate,
                color = HabitColor.values()[color],
                priority = HabitPriority.values()[priority],
                type = HabitType.values()[type],
                frequency = frequency,
                doneDates = doneDates,
                count = HabitCount.values()[count],
                deleted = false
            )
        }
    }

    private fun toHabitEntity(habit: Habit): HabitEntity {
        return with(habit) {
            HabitEntity(
                id = id,
                uid = uid,
                title = title,
                description = description,
                creationDate = creationDate,
                color = color,
                priority = priority,
                type = type,
                frequency = frequency,
                doneDates = doneDates,
                count = count,
                deleted = false
            )
        }
    }

    private fun toHabit(habitEntity: HabitEntity): Habit {
        return with(habitEntity) {
            Habit(
                id = id,
                uid = uid,
                title = title,
                description = description,
                creationDate = creationDate,
                color = color,
                priority = priority,
                type = type,
                frequency = frequency,
                count = count,
                doneDates = doneDates
            )
        }
    }
}