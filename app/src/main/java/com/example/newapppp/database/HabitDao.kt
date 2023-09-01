package com.example.newapppp.database

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.example.newapppp.data.habit_local.HabitType

@Dao
interface HabitDao {

    @Upsert
    suspend fun upsertHabit(habit: HabitEntity)

    @Upsert
    suspend fun upsertHabitList(habitList: List<HabitEntity>)

    @Query("DELETE FROM habit WHERE id = :habitId")
    suspend fun deleteHabitById(habitId: String)

    @Query("SELECT * FROM habit WHERE id = :habitId")
    suspend fun getHabitById(habitId: String) : HabitEntity

    @Query("SELECT * FROM habit WHERE type = :type")
    suspend fun getHabitListByType(type: HabitType) : List<HabitEntity>

    @Query("SELECT * FROM habit")
    suspend fun getAllHabits(): List<HabitEntity>

    @Query("SELECT uid FROM habit ")
    suspend fun getAllHabitsId() : List<String?>

    @Query("DELETE FROM habit")
    suspend fun deleteAllHabits()
}