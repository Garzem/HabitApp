package com.example.newapppp.data

import androidx.room.Database
import androidx.room.RoomDatabase


@Database(
    entities = [Habit::class],
    version = 1
)
abstract class HabitDataBase : RoomDatabase() {
    abstract fun habitDao(): HabitDao
}