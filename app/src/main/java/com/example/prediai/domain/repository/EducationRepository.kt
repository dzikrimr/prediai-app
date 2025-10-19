package com.example.prediai.domain.repository

import com.example.prediai.domain.model.EducationVideo
import kotlinx.coroutines.flow.Flow

interface EducationRepository {
    // Ubah fungsi ini. Sekarang kita perlu 'query'
    fun getEducationVideos(query: String): Flow<List<EducationVideo>>

    // Kita ubah ini agar mengambil query rekomendasi
    fun getRecommendations(): Flow<List<EducationVideo>>
}