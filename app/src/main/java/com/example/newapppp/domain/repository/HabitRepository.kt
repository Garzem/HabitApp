package com.example.newapppp.domain.repository

import com.example.newapppp.domain.model.Habit
import com.example.newapppp.data.database.habit_local.HabitEntity
import com.example.newapppp.domain.model.HabitType
import com.example.newapppp.data.remote.habit.DeleteHabitJson
import com.example.newapppp.data.remote.habit.GetHabitJson
import com.example.newapppp.data.remote.habit.HabitIdJson
import com.example.newapppp.data.remote.habit.HabitSave
import com.example.newapppp.data.remote.habit.PutHabitJson


interface HabitRepository {

    suspend fun fetchHabitList(): List<Habit>

    suspend fun saveOrUpdateHabit(habitSave: HabitSave, habitId: String?)

    suspend fun getHabitList(): List<Habit>

    suspend fun deleteHabit(habit: Habit)

    suspend fun deleteAllHabits()

    suspend fun getHabitById(habitId: String): Habit

    suspend fun getHabitListByType(type: HabitType): List<Habit>

    suspend fun putHabitWithRetry(token: String, putHabitJson: PutHabitJson): HabitIdJson

    suspend fun getHabitWithRetry(token: String): List<GetHabitJson>

    suspend fun deleteHabitWithRetry(deleteRequest: DeleteHabitJson): Result<Unit>

    //   fun postHabitWithRetry(postHabitJson: PostHabitJson)

    fun toHabitJson(saveHabit: HabitSave): PutHabitJson

    fun toHabitEntity(habit: HabitSave, habitId: String?): HabitEntity

    fun toHabitEntity(habit: GetHabitJson): HabitEntity

    fun toHabitEntity(habit: Habit): HabitEntity

    fun toHabit(habitEntity: HabitEntity): Habit
}