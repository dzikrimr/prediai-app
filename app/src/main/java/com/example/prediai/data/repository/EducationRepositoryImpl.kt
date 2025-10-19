package com.example.prediai.data.repository

import com.example.prediai.data.remote.youtube.YoutubeApiService
import com.example.prediai.data.remote.youtube.YoutubeSearchItem
import com.example.prediai.domain.model.EducationVideo
import com.example.prediai.domain.model.VideoCategories
import com.example.prediai.domain.repository.EducationRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class EducationRepositoryImpl @Inject constructor(
    // Hapus LocalVideoDataSource
    // Tambahkan YoutubeApiService
    private val apiService: YoutubeApiService
) : EducationRepository {

    // Definisikan query pencarian untuk setiap kategori
    private val categoryQueryMap = mapOf(
        VideoCategories.SEMUA to "diabetes",
        VideoCategories.NUTRISI to "nutrisi diabetes",
        VideoCategories.OLAHRAGA to "olahraga diabetes",
        VideoCategories.PERAWATAN to "perawatan luka diabetes",
        VideoCategories.GEJALA to "gejala diabetes"
    )

    override fun getEducationVideos(query: String): Flow<List<EducationVideo>> = flow {
        try {
            // 1. Ambil query dari map
            val searchQuery = categoryQueryMap[query] ?: "diabetes"

            // 2. Panggil API
            val response = apiService.searchVideos(query = searchQuery)

            // 3. Ubah (Map) data response API menjadi data domain
            val domainVideos = response.items.map { it.toEducationVideo() }
            emit(domainVideos)

        } catch (e: Exception) {
            // Jika error, kirim list kosong
            emit(emptyList())
        }
    }

    override fun getRecommendations(): Flow<List<EducationVideo>> {
        // Rekomendasi = Panggil API dengan query khusus "rekomendasi diabetes"
        // (Ini akan menghabiskan 100 kuota setiap kali Home dibuka)
        return flow {
            try {
                val response = apiService.searchVideos(query = "tips kesehatan diabetes", maxResults = 3)
                emit(response.items.map { it.toEducationVideo() })
            } catch (e: Exception) {
                emit(emptyList())
            }
        }
    }
}

// Helper function untuk mengubah (map) model data
private fun YoutubeSearchItem.toEducationVideo(): EducationVideo {
    return EducationVideo(
        id = this.id.videoId, // Gunakan ID dari YouTube
        title = this.snippet.title,
        source = this.snippet.channelTitle,
        views = "", // API Search tidak mengembalikan view count (perlu panggilan API lain)
        imageUrl = this.snippet.thumbnails.medium.url,
        youtubeVideoId = this.id.videoId,
        category = "" // Kategori tidak lagi relevan dari API
    )
}