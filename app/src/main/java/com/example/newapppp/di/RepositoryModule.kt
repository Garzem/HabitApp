package com.example.newapppp.di

import com.example.newapppp.data.repository.FilterRepositoryImpl
import com.example.newapppp.data.repository.HabitRepositoryImpl
import com.example.newapppp.domain.repository.FilterRepository
import com.example.newapppp.domain.repository.HabitRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface RepositoryModule {

    @Binds
    @Singleton
    fun provideHabitRepository(impl: HabitRepositoryImpl): HabitRepository

    @Binds
    @Singleton
    fun provideFilterRepository(impl: FilterRepositoryImpl): FilterRepository
}
