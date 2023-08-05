package com.example.newapppp.database

import android.content.Context
import androidx.room.Room


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