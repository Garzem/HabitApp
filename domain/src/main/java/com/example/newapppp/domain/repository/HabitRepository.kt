package com.example.newapppp.domain.repository

import com.example.newapppp.domain.model.Habit
import com.example.newapppp.domain.model.HabitSave
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow


interface HabitRepository {

    fun getHabitListFlow(): Flow<List<Habit>>

    suspend fun saveOrUpdateHabit(habitSave: HabitSave, habitId: String?): Job

    suspend fun deleteHabit(habit: Habit)

    suspend fun deleteAllHabits()

    suspend fun fetchHabitList()

    suspend fun deleteOfflineDeletedHabits()

    suspend fun putOfflineHabitList()

    suspend fun getHabitById(habitId: String): Habit

    suspend fun updateHabitDates(habitId: String, date: Long): Habit

    suspend fun postOfflineHabitList()
}