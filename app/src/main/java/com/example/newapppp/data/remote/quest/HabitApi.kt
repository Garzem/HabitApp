package com.example.newapppp.data.remote.quest

import kotlinx.coroutines.Deferred
import retrofit2.http.GET

interface HabitApi {

    @GET("https://droid-test-server.doubletapp.ru/api/habit")
    suspend fun getHabitList(): Deferred<List<HabitListResponse>> {
        TODO()
    }
}