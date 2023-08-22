package com.example.newapppp.data.remote.quest

import retrofit2.http.GET

interface HabitApi {

    @GET("/api/habit")
    suspend fun getHabitList(): HabitResponse
}