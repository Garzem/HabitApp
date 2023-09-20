package com.example.newapppp.data.database.habit_local

import androidx.room.TypeConverter

class DoneDatesConverter {
    @TypeConverter
    fun fromList(list: List<Int>): String {
        if (list.isEmpty()) {
            return ""
        }
        return list.joinToString(",")
    }

    @TypeConverter
    fun toList(data: String): List<Int> {
        if (data.isEmpty()) {
            return emptyList()
        }
        return data.split(",").map { it.toInt() }
    }
}