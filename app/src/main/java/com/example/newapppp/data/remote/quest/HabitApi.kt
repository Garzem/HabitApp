package com.example.newapppp.data.remote.quest

import retrofit2.http.GET
import retrofit2.http.Header

interface HabitApi {

    @GET("/api/habit")
    suspend fun getHabitList(
        @Header("Authorization") token: String
    ): List<HabitServer>
}