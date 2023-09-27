package com.example.newapppp.di

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
    @Singleton
    fun provideHabitDao(database: com.example.newapppp.data.database.HabitDataBase): com.example.newapppp.data.database.HabitDao {
        return database.habitDao()
    }

    @Provides
    @Singleton
    fun provideDataBase(@ApplicationContext appContext: Context): com.example.newapppp.data.database.HabitDataBase {
        return Room.databaseBuilder(
            appContext,
            com.example.newapppp.data.database.HabitDataBase::class.java, "database-habit"
        )
            .fallbackToDestructiveMigration()
            .build()
    }
}