package com.example.newapppp.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.newapppp.domain.habit_local.HabitEntity


@Database(
    entities = [HabitEntity::class],
    version = 5
)
abstract class HabitDataBase : RoomDatabase() {
    abstract fun habitDao(): HabitDao
}