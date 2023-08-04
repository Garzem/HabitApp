package com.example.newapppp.data

import android.content.Context
import androidx.room.DatabaseConfiguration
import androidx.room.InvalidationTracker
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteOpenHelper


object AppHabitDataBase {
    private lateinit var instance: HabitDataBase

    fun init(context: Context) {
        instance = Room.databaseBuilder(
            context.applicationContext,
            HabitDataBase::class.java, "database-habit"
        ).build()
    }

    fun getDatabase(): HabitDataBase {
        if (!::instance.isInitialized) {
            throw UninitializedPropertyAccessException("AppDataBase must be initialized fisrt")
        }
        return instance
    }

}