package com.example.newapppp.presentation

import android.app.Application
import com.example.newapppp.domain.Constants.BASE_URL
import com.example.newapppp.domain.remote.habit.HabitApi
import com.example.newapppp.data.database.AppHabitDataBase
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit

class HApp: Application() {

    companion object {
        lateinit var habitApi: HabitApi
    }

    override fun onCreate() {
        super.onCreate()
        configureRetrofit()
        AppHabitDataBase.init(applicationContext)
    }

    private fun configureRetrofit() {
        val httpLoggingInterceptor = HttpLoggingInterceptor()
        httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BODY

        val okHttpClient = OkHttpClient.Builder()
            .addInterceptor(httpLoggingInterceptor)
            .build()

        val json = Json { ignoreUnknownKeys = true }
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
            .build()

        habitApi = retrofit.create(HabitApi::class.java)
    }
}