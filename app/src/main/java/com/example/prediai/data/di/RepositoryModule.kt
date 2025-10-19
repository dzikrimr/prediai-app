package com.example.prediai.data.di

// ... import-import lain ...
import com.example.prediai.data.repository.EducationRepositoryImpl
import com.example.prediai.data.repository.ScheduleRepositoryImpl
import com.example.prediai.domain.repository.EducationRepository
import com.example.prediai.domain.repository.ScheduleRepository
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
    abstract fun bindEducationRepository(
        educationRepositoryImpl: EducationRepositoryImpl
    ): EducationRepository

    // ... (binding repository lain) ...

    // TAMBAHKAN BINDING BARU INI
    @Binds
    @Singleton
    abstract fun bindScheduleRepository(
        scheduleRepositoryImpl: ScheduleRepositoryImpl
    ): ScheduleRepository
}