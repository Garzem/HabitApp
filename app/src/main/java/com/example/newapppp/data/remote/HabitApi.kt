package com.example.newapppp.data.remote

import com.example.newapppp.data.remote.modul.DeleteHabitJson
import com.example.newapppp.data.remote.modul.GetHabitJson
import com.example.newapppp.data.remote.modul.HabitIdJson
import com.example.newapppp.data.remote.modul.PostHabitJson
import com.example.newapppp.data.remote.modul.PutHabitJson
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.HTTP
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.PUT

interface HabitApi {

    @GET("/api/habit")
    suspend fun getHabitList(
        @Header("Authorization") token: String
    ): List<GetHabitJson>

    @PUT("/api/habit")
    suspend fun putHabit(
        @Header("Authorization") token: String,
        @Body putHabitJson: PutHabitJson
    ) : HabitIdJson

    @HTTP(method = "DELETE", path = "/api/habit", hasBody = true)
    suspend fun deleteHabit(
        @Header("Authorization") token: String,
        @Body id: DeleteHabitJson
    )

    @POST("/api/habit_done")
    suspend fun postHabit(
        @Header("Authorization") token: String,
        @Body postHabitJson: PostHabitJson
    )
}