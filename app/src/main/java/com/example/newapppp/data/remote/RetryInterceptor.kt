package com.example.newapppp.data.remote

import com.example.newapppp.domain.Constants.BASE_URL
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Protocol
import okhttp3.Request
import okhttp3.Response
import java.io.IOException

class RetryInterceptor(private val maxRetries: Int, private val retryDelayMillis: Long): Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val request: Request = chain.request()
        var response: Response? = null
        var tryCount = 0

        runBlocking {
            while (tryCount < maxRetries) {
                try {
                    response = chain.proceed(request)
                    response?.let {
                        if (it.isSuccessful) {
                            return@runBlocking response
                        }
                    }
                } catch (e: IOException) {
                    e.printStackTrace()
                }
                delay(retryDelayMillis)
                tryCount++
            }
        }
        return response ?: createFakeErrorResponse()
    }

    private fun createFakeErrorResponse(): Response {
        return Response.Builder()
            .code(400)
            .message("Network Error")
            .protocol(Protocol.HTTP_1_1)
            .request(Request.Builder().url(BASE_URL).build())
            .build()
    }
}