package com.example.newapppp.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase


@Database(
    entities = [Habit::class],
    version = 1
)
abstract class AppDataBase : RoomDatabase() {
    abstract fun habitDao(): HabitDao
}