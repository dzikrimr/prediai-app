package com.example.prediai.data.repository

import com.example.prediai.data.remote.youtube.YoutubeApiService
import com.example.prediai.data.remote.youtube.YoutubeSearchItem
import com.example.prediai.data.remote.youtube.YoutubeVideoItem // <-- IMPORT BARU
import com.example.prediai.domain.model.EducationVideo
import com.example.prediai.domain.model.VideoCategories
import com.example.prediai.domain.repository.EducationRepository
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class EducationRepositoryImpl @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val apiService: YoutubeApiService
) : EducationRepository {

    // ... (kode categoryQueryMap dan getEducationVideos tidak berubah) ...
    private val categoryQueryMap = mapOf(
        VideoCategories.SEMUA to "diabetes",
        VideoCategories.NUTRISI to "nutrisi diabetes",
        VideoCategories.OLAHRAGA to "olahraga diabetes",
        VideoCategories.PERAWATAN to "perawatan luka diabetes",
        VideoCategories.GEJALA to "gejala diabetes"
    )

    override fun getEducationVideos(query: String): Flow<List<EducationVideo>> = flow {
        try {
            val searchQuery = categoryQueryMap[query] ?: "diabetes"
            val response = apiService.searchVideos(query = searchQuery)
            val domainVideos = response.items.map { it.toEducationVideo() }
            emit(domainVideos)
        } catch (e: Exception) {
            emit(emptyList())
        }
    }


    // --- GANTI TOTAL FUNGSI INI ---
    override fun getVideoById(videoId: String): Flow<EducationVideo?> = flow {
        try {
            // Panggil API untuk mendapatkan detail spesifik dari satu video
            val response = apiService.getVideoDetails(videoId = videoId)

            // Ambil item pertama dari hasil (seharusnya hanya ada satu)
            val videoItem = response.items.firstOrNull()

            // Ubah (map) data response menjadi data domain menggunakan mapper baru
            val domainVideo = videoItem?.toEducationVideo()

            emit(domainVideo) // Kirim video yang sudah ditemukan
        } catch (e: Exception) {
            // Jika ada error saat memanggil API, kirim null
            emit(null)
        }
    }
    // --- AKHIR PERUBAHAN ---

    override fun getRecommendations(): Flow<List<EducationVideo>> {
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

// Mapper untuk search (tetap ada)
private fun YoutubeSearchItem.toEducationVideo(): EducationVideo {
    return EducationVideo(
        id = this.id.videoId,
        title = this.snippet.title,
        source = this.snippet.channelTitle,
        views = "",
        imageUrl = this.snippet.thumbnails.medium.url,
        youtubeVideoId = this.id.videoId,
        category = ""
    )
}

// --- TAMBAHKAN HELPER FUNCTION BARU DI BAWAH INI ---
private fun YoutubeVideoItem.toEducationVideo(): EducationVideo {
    return EducationVideo(
        id = this.id, // ID dari objek detail
        title = this.snippet.title,
        source = this.snippet.channelTitle,
        // View count sekarang tersedia!
        views = this.statistics.viewCount,
        imageUrl = this.snippet.thumbnails.medium.url,
        youtubeVideoId = this.id,
        category = this.snippet.tags?.firstOrNull() ?: ""
    )
}
// --- AKHIR BAGIAN BARU ---