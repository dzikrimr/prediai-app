package com.example.prediai.domain.repository

import com.example.prediai.domain.model.EducationVideo
import kotlinx.coroutines.flow.Flow

/**
 * Interface ini adalah "kontrak" untuk presentation layer.
 * ViewModel HANYA akan tahu tentang interface ini.
 */
interface EducationRepository {

    /** Mengambil 3 video teratas untuk rekomendasi di home screen */
    fun getRecommendations(): Flow<List<EducationVideo>>

    /** Mengambil SEMUA video untuk education list screen */
    fun getAllEducationVideos(): Flow<List<EducationVideo>>
}