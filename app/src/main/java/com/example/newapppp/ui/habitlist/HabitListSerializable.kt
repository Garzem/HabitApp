package com.example.newapppp.ui.habitlist

import android.os.Build
import android.os.Bundle
import java.io.Serializable

class HabitListSerializable {
    companion object {
        @Suppress("UNCHECKED_CAST")
        fun <T : Serializable> Bundle.serializable(key: String, className: Class<T>): T? {
            return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU)
                getSerializable(key, className)
            else
                getSerializable(key) as? T
        }
    }
}