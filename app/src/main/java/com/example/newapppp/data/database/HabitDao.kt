package com.example.newapppp.data.database

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.example.newapppp.data.database.habit_local.HabitEntity
import kotlinx.coroutines.flow.Flow

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

    @Query("SELECT * FROM habit WHERE deleted = 0")
    fun getAllHabitsFlow(): Flow<List<HabitEntity>>

    @Query("SELECT * FROM habit WHERE deleted = 0")
    suspend fun getAllHabits(): List<HabitEntity>

    @Query("SELECT * FROM habit WHERE deleted = 1")
    suspend fun getAllDeletedHabits(): List<HabitEntity>
}