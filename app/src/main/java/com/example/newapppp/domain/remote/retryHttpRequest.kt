package com.example.newapppp.domain.remote

import kotlinx.coroutines.delay
import retrofit2.HttpException

suspend inline fun <T> callWithRetry(block: () -> T): Result<T> {
    var currentRetryCount = 0

    while (true) {
        try {
            Result.success(block())
        } catch (e: HttpException) {
            if (currentRetryCount > 3) {
                Result.failure<T>(e)
            }
            currentRetryCount++
            delay(currentRetryCount * 1_000L)
        }
    }
}