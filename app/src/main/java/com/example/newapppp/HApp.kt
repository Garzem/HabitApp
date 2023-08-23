package com.example.newapppp

import android.app.Application
import com.example.newapppp.data.Constants.BASE_URL
import com.example.newapppp.data.remote.habit.HabitApi
import com.example.newapppp.database.AppHabitDataBase
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class HApp: Application() {

    companion object {
        lateinit var habitApi: HabitApi
    }

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
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        habitApi = retrofit.create(HabitApi::class.java)
    }
}