package com.example.newapppp.data.di

import com.example.newapppp.domain.usecase.GetHabitPriorityListUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import javax.inject.Singleton

@Module
@InstallIn(ViewModelComponent::class)
class FilterModule {

    @Provides
    fun provideGetListUseCase(): GetHabitPriorityListUseCase {
        return GetHabitPriorityListUseCase()
    }
}