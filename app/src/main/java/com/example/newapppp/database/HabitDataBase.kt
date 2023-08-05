package com.example.newapppp.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.newapppp.data.HabitEntity


@Database(
    entities = [HabitEntity::class],
    version = 1
)
abstract class HabitDataBase : RoomDatabase() {
    abstract fun habitDao(): HabitDao
}