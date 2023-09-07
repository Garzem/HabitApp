package com.example.newapppp.data.di

import android.content.Context
import androidx.room.Room
import com.example.newapppp.data.database.HabitDao
import com.example.newapppp.data.database.HabitDataBase
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataBaseModule {

    @Provides
    fun provideHabitDao(database: HabitDataBase): HabitDao {
        return database.habitDao()
    }

    @Provides
    @Singleton
    fun provideDataBase(@ApplicationContext appContext: Context): HabitDataBase {
        return Room.databaseBuilder(
            appContext,
            HabitDataBase::class.java, "database-habit"
        )
            .fallbackToDestructiveMigration()
            .build()
    }
}