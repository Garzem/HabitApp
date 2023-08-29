package com.example.newapppp.database

import androidx.room.Database
import androidx.room.RoomDatabase


@Database(
    entities = [HabitEntity::class],
    version = 5
)
abstract class HabitDataBase : RoomDatabase() {
    abstract fun habitDao(): HabitDao
}