package com.example.newapppp.di

import com.example.newapppp.data.remote.NetworkUtil
import com.example.newapppp.domain.INetworkUtil
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface NetworkModule {

    @Binds
    @Singleton
    fun provideNetworkUtil(networkUtil: NetworkUtil): INetworkUtil

}