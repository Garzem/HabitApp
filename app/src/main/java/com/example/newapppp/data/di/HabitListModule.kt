package com.example.newapppp.data.di

import com.example.newapppp.domain.usecase.DeleteHabitRemoteUseCase
import com.example.newapppp.domain.usecase.habit_list.GetLocalHabitListTypeUseCase
import com.example.newapppp.domain.usecase.habit_list.GetLocalHabitListUseCase
import com.example.newapppp.domain.usecase.habit_list.GetRemoteHabitListByTypeUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import javax.inject.Singleton

@Module
@InstallIn(ViewModelComponent::class)
class HabitListModule {

    @Provides
    @Singleton
    fun provideGetLocalHabitListTypeUseCase(): GetLocalHabitListTypeUseCase {
        return GetLocalHabitListTypeUseCase()
    }

    @Provides
    @Singleton
    fun provideGetLocalHabitListUseCase(): GetLocalHabitListUseCase {
        return GetLocalHabitListUseCase()
    }

    @Provides
    @Singleton
    fun provideGetRemoteHabitListByTypeUseCase(): GetRemoteHabitListByTypeUseCase {
        return GetRemoteHabitListByTypeUseCase()
    }

    @Provides
    @Singleton
    fun provideDeleteHabitRemoteUseCase(): DeleteHabitRemoteUseCase {
        return DeleteHabitRemoteUseCase()
    }
}