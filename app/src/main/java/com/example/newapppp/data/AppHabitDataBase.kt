package com.example.newapppp.data

import android.content.Context
import androidx.room.DatabaseConfiguration
import androidx.room.InvalidationTracker
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteOpenHelper


object AppHabitDataBase {
    private var instance: HabitDataBase? = null

    fun init(context: Context) {
        instance = Room.databaseBuilder(
            context.applicationContext,
            HabitDataBase::class.java, "database-habit"
        ).build()
    }

    val habitDao: HabitDao
        get() {
            val database = instance ?: error("AppDataBase must be initialized first")
            return database.habitDao()
        }
}