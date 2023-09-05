package com.example.newapppp.presentation

import android.app.Application
import com.example.newapppp.domain.Constants.BASE_URL
import com.example.newapppp.data.remote.habit.HabitApi
import com.example.newapppp.data.database.AppHabitDataBase
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import dagger.hilt.android.HiltAndroidApp
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit

@HiltAndroidApp
class HApp: Application() {
    override fun onCreate() {
        super.onCreate()
        AppHabitDataBase.init(applicationContext)
    }
}