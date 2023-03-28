package com.rskopyl.tyander.di

import android.content.Context
import androidx.room.Room
import androidx.room.migration.AutoMigrationSpec
import com.rskopyl.tyander.data.local.AppDatabase
import com.rskopyl.tyander.data.local.dao.MatchDao
import com.rskopyl.tyander.data.local.dao.MessageDao
import com.rskopyl.tyander.util.ROOM_DATABASE_NAME
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideAppDatabase(
        @ApplicationContext context: Context
    ): AppDatabase {
        return Room
            .databaseBuilder(
                context,
                AppDatabase::class.java,
                ROOM_DATABASE_NAME
            )
            .build()
    }

    @Provides
    fun provideMatchDao(appDatabase: AppDatabase): MatchDao =
        appDatabase.matchDao

    @Provides
    fun provideMessageDao(appDatabase: AppDatabase): MessageDao =
        appDatabase.messageDao
}