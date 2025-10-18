package com.example.prediai.data.repository

import com.example.prediai.data.local.LocalVideoDataSource
import com.example.prediai.domain.model.EducationVideo
import com.example.prediai.domain.repository.EducationRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf // Helper untuk mengubah List menjadi Flow
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Ini adalah "pekerja" yang sebenarnya.
 * Ia tahu cara mendapatkan data (dalam hal ini, dari LocalVideoDataSource).
 */
@Singleton // Kita hanya perlu satu instance dari repository ini
class EducationRepositoryImpl @Inject constructor() : EducationRepository {

    // Mengambil data dari data source lokal
    private val allVideos = LocalVideoDataSource.allVideosList

    override fun getRecommendations(): Flow<List<EducationVideo>> {
        // Logika bisnis: rekomendasi adalah 3 video pertama
        return flowOf(allVideos.take(3))
    }

    override fun getAllEducationVideos(): Flow<List<EducationVideo>> {
        // Logika bisnis: berikan semua video
        return flowOf(allVideos)
    }
}