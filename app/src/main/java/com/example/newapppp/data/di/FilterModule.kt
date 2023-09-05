package com.example.newapppp.data.di

import com.example.newapppp.domain.usecase.GetListUseCase
import com.example.newapppp.domain.usecase.filter.GetPriorityUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import javax.inject.Singleton

@Module
@InstallIn(ViewModelComponent::class)
class FilterModule {

    @Provides
    @Singleton
    fun provideGetPriorityUseCase(): GetPriorityUseCase {
        return GetPriorityUseCase()
    }

    @Provides
    fun provideGetListUseCase(): GetListUseCase {
        return GetListUseCase()
    }
}