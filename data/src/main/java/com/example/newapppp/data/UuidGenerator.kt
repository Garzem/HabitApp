package com.example.newapppp.data

import java.util.UUID
import javax.inject.Inject

class UuidGenerator @Inject constructor() {

    fun generateUuid(): String {
        return UUID.randomUUID().toString()
    }
}