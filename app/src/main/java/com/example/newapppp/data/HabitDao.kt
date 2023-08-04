package com.example.newapppp.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert

@Dao
interface HabitDao {

    @Upsert
    suspend fun upsertHabit(habit: Habit)

    @Delete
    suspend fun deleteHabit(habit: Habit)

    @Query("SELECT * FROM Habit WHERE id = :habitId")
    suspend fun getHabitById(habitId: Int) : Habit

    @Query("SELECT color FROM Habit WHERE id = :type")
    suspend fun getHabitListById(type: Type) : Habit

}