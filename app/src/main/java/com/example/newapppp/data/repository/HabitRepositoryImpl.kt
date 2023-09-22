package com.example.newapppp.data.repository

import com.example.newapppp.data.database.HabitDao
import com.example.newapppp.data.database.habit_local.HabitEntity
import com.example.newapppp.data.remote.modul.DeleteHabitJson
import com.example.newapppp.data.remote.modul.GetHabitJson
import com.example.newapppp.data.remote.HabitApi
import com.example.newapppp.domain.model.HabitSave
import com.example.newapppp.data.remote.modul.PutHabitJson
import com.example.newapppp.data.remote.NetworkRetry
import com.example.newapppp.data.remote.modul.PostHabitJson
import com.example.newapppp.domain.Constants.TOKEN
import com.example.newapppp.domain.model.Habit
import com.example.newapppp.domain.model.HabitColor
import com.example.newapppp.domain.model.HabitCount
import com.example.newapppp.domain.model.HabitPriority
import com.example.newapppp.domain.model.HabitType
import com.example.newapppp.domain.repository.HabitRepository
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.util.UUID
import javax.inject.Inject


class HabitRepositoryImpl @Inject constructor(
    private val api: HabitApi,
    private val habitDao: HabitDao,
    private val networkRetry: NetworkRetry
) : HabitRepository {

    override fun getHabitListFlow(): Flow<List<Habit>> {
        return habitDao.getAllHabitsFlow().map { habitEntityList ->
            habitEntityList.map(::toHabit)
        }
    }

    override suspend fun saveOrUpdateHabit(habitSave: HabitSave, habitId: String?) {
        val habitEntity = toHabitEntity(habitSave, habitId)
        habitDao.upsertHabit(habitEntity)
    }

    override suspend fun deleteHabit(habit: Habit) {
        if (habit.uid == null) {
            habitDao.deleteHabitById(habit.id)
        } else {
            val habitDeleted = toHabitEntity(habit).copy(deleted = true)
            habitDao.upsertHabit(habitDeleted)
        }
    }


    override suspend fun deleteAllHabits() {
        val habitListFlow = habitDao.getAllHabits()
        habitListFlow.forEach { habitEntity ->
            if (habitEntity.uid == null) {
                habitDao.deleteHabitById(habitEntity.id)
            } else {
                val habitDeleted = habitEntity.copy(deleted = true)
                habitDao.upsertHabit(habitDeleted)
            }
        }
    }

    override suspend fun fetchHabitList() = coroutineScope {
        val habitJsonListResponseAsync =
            async {
                networkRetry.commonRetrying(null) { api.getHabitList(TOKEN) }
            }
        val habitLocalListAsync = async {
            habitDao.getAllHabits()
        }
        val habitJsonList = habitJsonListResponseAsync.await()
        val habitEntityList = habitLocalListAsync.await()

        if (!habitJsonList.isNullOrEmpty()) {
            val notSavedHabitJsonList = habitJsonList.filter { getHabitJson ->
                habitEntityList.none { habitEntity ->
                    habitEntity.uid == getHabitJson.uid || !habitEntity.deleted
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
            val notSavedOfflineHabitListFlow = localHabitEntityList.filter { habitEntity ->
                habitJsonList.none { getHabitJson ->
                    getHabitJson.uid == habitEntity.uid
                }
            }
            notSavedOfflineHabitListFlow.forEach { habitEntity ->
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

    override suspend fun saveOrUpdateSelectedDates(habit: Habit) {
        habitDao.upsertHabit(toHabitEntity(habit))
    }

    override suspend fun postOfflineHabit() {
        val habitList = habitDao.getAllHabits()
        val getHabitJsonList = networkRetry.commonRetrying(null) {
            api.getHabitList(TOKEN)
        }
        if (getHabitJsonList != null) {
            val habitListToPost = habitList.filter { habitEntity ->
                val habitJson = getHabitJsonList.find { it.uid == habitEntity.uid }
                return@filter (habitJson != null && habitEntity.doneDates.isNotEmpty()
                        && habitEntity.doneDates.last() != habitJson.doneDates.lastOrNull())
            }
            val postHabitJsonList = habitListToPost.map { habitEntity ->
                val lastDoneDate = habitEntity.doneDates.last()
                PostHabitJson(
                    doneDate = lastDoneDate,
                    uid = habitEntity.uid ?: return
                )
            }
            postHabitJsonList.forEach { postHabitJson ->
                networkRetry.commonRetrying(null) {
                    api.postHabit(TOKEN, postHabitJson)
                }
            }
        }
    }


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
                count = 0,
                doneDates = doneDates
            )
        }
    }

    override fun toHabitJson(habit: HabitEntity): PutHabitJson {
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

    override fun toHabitEntity(habit: HabitSave, habitId: String?): HabitEntity {
        return with(habit) {
            HabitEntity(
                id = habitId ?: UUID.randomUUID().toString(),
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

    override fun toHabitEntity(habit: GetHabitJson): HabitEntity {
        return with(habit) {
            HabitEntity(
                id = UUID.randomUUID().toString(),
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

    override fun toHabitEntity(habit: Habit): HabitEntity {
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

    override fun toHabit(habitEntity: HabitEntity): Habit {
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