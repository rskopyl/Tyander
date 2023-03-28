package com.rskopyl.tyander.di

import com.rskopyl.tyander.data.preferences.AppPreferencesManager
import com.rskopyl.tyander.data.preferences.AppPreferencesManagerImpl
import com.rskopyl.tyander.repository.CharacterRepository
import com.rskopyl.tyander.repository.MatchRepository
import com.rskopyl.tyander.repository.MessageRepository
import com.rskopyl.tyander.repository.impl.CharacterRepositoryImpl
import com.rskopyl.tyander.repository.impl.MatchRepositoryImpl
import com.rskopyl.tyander.repository.impl.MessageRepositoryImpl
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
    fun bindCharacterRepository(
        impl: CharacterRepositoryImpl
    ): CharacterRepository

    @Binds
    @Singleton
    fun bindMatchRepository(
        impl: MatchRepositoryImpl
    ): MatchRepository

    @Binds
    @Singleton
    fun bindMessageRepository(
        impl: MessageRepositoryImpl
    ): MessageRepository

    @Binds
    @Singleton
    fun bindAppPreferencesManager(
        impl: AppPreferencesManagerImpl
    ): AppPreferencesManager
}