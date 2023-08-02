package com.example.newapppp.ui.home

import android.os.Build
import android.os.Bundle
import java.io.Serializable

class HomeSerializable {
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