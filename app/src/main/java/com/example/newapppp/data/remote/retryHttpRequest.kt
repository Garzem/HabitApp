package com.example.newapppp.data.remote

import kotlinx.coroutines.delay
import retrofit2.HttpException

suspend inline fun <T> callWithRetry(block: () -> T): T {
    var currentRetryCount = 0

    while (true) {
        try {
            return block()
        } catch (e: HttpException) {
            if (currentRetryCount > 3) {
                throw e
            }
            currentRetryCount++
            delay(currentRetryCount * 1_000L)
        }
    }
}