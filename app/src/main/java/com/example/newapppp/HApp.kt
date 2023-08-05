package com.example.newapppp

import android.app.Application
import com.example.newapppp.database.AppHabitDataBase

class HApp: Application() {

    override fun onCreate() {
        super.onCreate()

        AppHabitDataBase.init(applicationContext)
    }
}