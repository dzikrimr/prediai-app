package com.example.prediai.core.di

import com.example.prediai.data.repository.ChatRepositoryImpl
import com.example.prediai.data.repository.HistoryRepositoryImpl
import com.example.prediai.data.repository.PlacesRepositoryImpl
import com.example.prediai.data.repository.UserRepositoryImpl // PASTIKAN IMPORT INI BENAR
import com.example.prediai.domain.repository.ChatRepository
import com.example.prediai.domain.repository.HistoryRepository
import com.example.prediai.domain.repository.PlacesRepository
import com.example.prediai.domain.repository.UserRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindChatRepository(
        impl: ChatRepositoryImpl
    ): ChatRepository

    @Binds
    @Singleton
    abstract fun bindUserRepository(
        impl: UserRepositoryImpl
    ): UserRepository

    @Binds
    @Singleton
    abstract fun bindHistoryRepository(
        historyRepositoryImpl: HistoryRepositoryImpl
    ): HistoryRepository

    @Binds
    @Singleton
    abstract fun bindPlacesRepository(
        placesRepositoryImpl: PlacesRepositoryImpl
    ): PlacesRepository
}