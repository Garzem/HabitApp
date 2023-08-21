package com.example.newapppp

import android.app.Application
import com.example.newapppp.data.remote.quest.HabitApi
import com.example.newapppp.database.AppHabitDataBase
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class HApp: Application() {

    lateinit var habitApi: HabitApi

    override fun onCreate() {
        super.onCreate()
        configureRetrofit()
        AppHabitDataBase.init(applicationContext)
    }

    fun configureRetrofit() {
        val httpLoggingInterceptor = HttpLoggingInterceptor()
        httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BODY

        val okHttpClient = OkHttpClient.Builder()
            .addInterceptor(httpLoggingInterceptor)
            .build()

        val retrofit = Retrofit.Builder()
            .baseUrl("https://droid-test-server.doubletapp.ru/")
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        habitApi = retrofit.create(HabitApi::class.java)
    }
}