package com.example.newapppp.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import com.example.newapppp.data.HabitType

@Dao
interface HabitDao {

    @Upsert
    suspend fun upsertHabit(habit: HabitEntity)

    @Query("SELECT * FROM habit WHERE id = :habitId")
    suspend fun deleteHabitById(habitId: String) : HabitEntity

    @Query("SELECT * FROM habit WHERE id = :habitId")
    suspend fun getHabitById(habitId: String) : HabitEntity

    @Query("SELECT color FROM habit WHERE type = :type")
    suspend fun getHabitListByType(type: HabitType) : HabitEntity

}