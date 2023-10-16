package com.example.newapppp.presentation.util

import javax.inject.Inject

class DateGenerator @Inject constructor() {

    fun getCurrentDate(): Long {
        return System.currentTimeMillis()
    }
}