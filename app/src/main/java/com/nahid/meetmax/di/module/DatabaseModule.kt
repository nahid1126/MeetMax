package com.nahid.meetmax.di.module

import android.content.Context
import androidx.room.Room
import com.nahid.meetmax.model.local.LocalDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class DatabaseModule {
    @Singleton
    @Provides
    fun provideLocalDatabase(@ApplicationContext context: Context): LocalDatabase {
        return Room.databaseBuilder(
            context.applicationContext,
            LocalDatabase::class.java,
            "MeetMax_DB"
        ).fallbackToDestructiveMigration().build()
    }
}