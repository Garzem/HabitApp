package com.example.newapppp.data.remote.habit

import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface HabitApi {

    @GET("/api/habit")
    suspend fun getHabitList(
        @Header("Authorization") token: String
    ): List<HabitServer>

    @Headers(
        "Content-Type: application/json"
    )
    @PUT("/api/habit")
    suspend fun putHabit(
        @Header("Authorization") token: String,
        @Body habitRequest: HabitRequest
    ) : HabitIdGetter

    @Headers(
        "Content-Type: application/json"
    )
    @DELETE("/api/habit/{uid}")
    suspend fun deleteHabit(
        @Header("Authorization") token: String,
        @Path("uid") id: HabitDeleteRequest
    )

    @Headers(
        "Content-Type: application/json"
    )
    @POST("/api/habit_done")
    suspend fun postHabit(
        @Header("Authorization") token: String,
        @Body habitDone: HabitDone
    )
}