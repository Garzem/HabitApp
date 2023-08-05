package com.example.newapppp.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert

@Dao
interface HabitDao {

    @Upsert
    suspend fun upsertHabit(habit: HabitEntity)

    @Delete
    suspend fun deleteHabit(habit: HabitEntity)

    @Query("SELECT * FROM Habit WHERE id = :habitId")
    suspend fun getHabitById(habitId: String) : HabitEntity

    @Query("SELECT color FROM Habit WHERE type = :type")
    suspend fun getHabitListByType(type: TypeEntity) : HabitEntity

}