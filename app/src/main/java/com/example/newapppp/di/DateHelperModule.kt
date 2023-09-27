package com.example.newapppp.di

import com.example.newapppp.data.DateHelperImpl
import com.example.newapppp.domain.DateHelper
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface DateHelperModule {

    @Binds
    @Singleton
    fun provideDateHelper(impl: com.example.newapppp.data.DateHelperImpl): DateHelper
}