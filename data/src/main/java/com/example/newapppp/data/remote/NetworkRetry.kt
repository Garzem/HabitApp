package com.example.newapppp.data.remote

import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.delay
import retrofit2.HttpException
import javax.inject.Inject


class NetworkRetry @Inject constructor() {
    private suspend fun <T> retrying(
        fallbackValue: T?,
        tryCnt: Int,
        intervalMillis: (attempt: Int) -> Long,
        retryCheck: (Throwable) -> Boolean,
        block: suspend () -> T,
    ): T? {
        try {
            val retryCnt = tryCnt - 1
            repeat(retryCnt) { attempt ->
                try {
                    return block()
                } catch (e: Exception) {
                    if (e !is CancellationException && retryCheck(e)) {
                        delay(intervalMillis(attempt + 1))
                    } else {
                        throw e
                    }
                }
            }
            return block()
        } catch (e: Exception) {
            if (e is CancellationException) {
                throw e
            }
            return fallbackValue
        }
    }

    suspend fun <T> commonRetrying(
        fallbackValue: T?,
        block: suspend () -> T,
    ): T? {
        return retrying(fallbackValue, 3, { 2000L * it }, networkRetryCheck, block)
    }

    private val networkRetryCheck: (Throwable) -> Boolean = {
        val shouldRetry = when {
            it is HttpException && it.code() in 400 until 500 -> false
            else -> true
        }
        shouldRetry
    }
}