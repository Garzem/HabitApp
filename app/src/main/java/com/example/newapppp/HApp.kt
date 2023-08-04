package com.example.newapppp

import android.app.Application
import com.example.newapppp.data.AppHabitDataBase

class HApp: Application() {

    override fun onCreate() {
        super.onCreate()

        AppHabitDataBase.init(applicationContext)
    }
}