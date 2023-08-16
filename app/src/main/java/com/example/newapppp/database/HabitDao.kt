package com.example.newapppp.database

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.example.newapppp.data.HabitType

@Dao
interface HabitDao {

    @Upsert
    suspend fun upsertHabit(habit: HabitEntity)

    @Query("DELETE FROM habit WHERE id = :habitId")
    suspend fun deleteHabitById(habitId: String)

    @Query("SELECT * FROM habit WHERE id = :habitId")
    suspend fun getHabitById(habitId: String) : HabitEntity

//    @Query("SELECT * FROM habit WHERE type = :type")
//    suspend fun getHabitListByType(type: HabitType) : List<HabitEntity>

    @Query("SELECT * FROM habit")
    suspend fun getAllHabits() : List<HabitEntity>

    @Query("SELECT * FROM habit WHERE title = :titleFilter AND type = :type")
    suspend fun getFilteredHabitByTitle(titleFilter: String, type: HabitType): List<HabitEntity>

    @Query("SELECT * FROM habit WHERE priority = :priorityFilter AND type = :type")
    suspend fun getHabitListByPriority(priorityFilter: String, type: HabitType) : List<HabitEntity>

    @Query("SELECT * FROM habit WHERE title = :titleFilter AND priority = :priorityFilter AND type = :type")
    suspend fun getHabitListByTitleAndPriority(titleFilter: String, priorityFilter: String, type: HabitType):
            List<HabitEntity>
}