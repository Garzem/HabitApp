package com.example.newapppp.extension

import android.os.Build
import android.os.Bundle
import java.io.Serializable

@Suppress("UNCHECKED_CAST")
fun <T : Serializable> Bundle.serializable(key: String, className: Class<T>): T? {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU)
        getSerializable(key, className)
    else
        getSerializable(key) as? T
}