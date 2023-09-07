package com.example.newapppp.data.di

import com.example.newapppp.data.remote.habit.HabitApi
import com.example.newapppp.data.repository.HabitRepositoryImpl
import com.example.newapppp.domain.repository.HabitRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.android.scopes.ActivityScoped
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun provideHabitRepository(impl: HabitRepositoryImpl): HabitRepository
}
