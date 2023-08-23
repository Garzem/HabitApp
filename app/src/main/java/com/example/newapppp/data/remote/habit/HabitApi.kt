package com.example.newapppp.data.remote.habit

import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.PUT

interface HabitApi {

    @GET("/api/habit")
    suspend fun getHabitList(
        @Header("Authorization") token: String
    ): List<HabitServer>

    @PUT("/api/habit")
    suspend fun putHabit(
        @Header("Authorization") token: String,
        @Body habitRequest: HabitRequest
    )

    @POST("/api/habit_done")
    suspend fun postHabit(
        @Header("Authorization") token: String,
        @Body habitDone: HabitDone
    )
}