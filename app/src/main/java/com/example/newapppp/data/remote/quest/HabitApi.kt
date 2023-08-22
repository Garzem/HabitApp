package com.example.newapppp.data.remote.quest

import kotlinx.coroutines.Deferred
import retrofit2.http.GET

interface HabitApi {

    @GET("/api/habit")
    suspend fun getHabitList(): List<HabitListResponse> {
        TODO()
    }
}