package com.example.newapppp.domain

import kotlinx.coroutines.flow.Flow

interface INetworkUtil {

    fun observeIsOnline(): Flow<Boolean>

}