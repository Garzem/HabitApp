package com.example.newapppp.data

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase


object AppHabitDataBase {
    private lateinit var instance: RoomDatabase

    fun init(context: Context) {
        instance = Room.databaseBuilder(
            context.applicationContext,
            AppDataBase::class.java, "database-habit"
        ).build()
    }

    fun getDatabase(): RoomDatabase {
        if (!::instance.isInitialized) {
            throw UninitializedPropertyAccessException("AppDataBase must be initialized fisrt")
        }
        return instance
    }
}