package com.example.prediai.core.di

import com.example.prediai.data.repository.ChatRepositoryImpl
import com.example.prediai.data.repository.UserRepositoryImpl // PASTIKAN IMPORT INI BENAR
import com.example.prediai.domain.repository.ChatRepository
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
        // PASTIKAN NAMA IMPLEMENTASI INI BENAR
        impl: UserRepositoryImpl
    ): UserRepository
}