package com.example.newapppp.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import com.example.newapppp.data.HabitEntity
import com.example.newapppp.data.Type

@Dao
interface HabitDao {

    @Upsert
    suspend fun upsertHabit(habit: HabitEntity)

    @Delete
    suspend fun deleteHabit(habit: HabitEntity)

    @Query("SELECT * FROM Habit WHERE id = :habitId")
    suspend fun getHabitById(habitId: Int) : HabitEntity

    @Query("SELECT color FROM Habit WHERE id = :type")
    suspend fun getHabitListById(type: Type) : HabitEntity

}