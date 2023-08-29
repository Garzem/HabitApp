package com.example.newapppp.data.remote

suspend inline fun <T> callWithRetry(block: () -> T): T {
    return block()
}