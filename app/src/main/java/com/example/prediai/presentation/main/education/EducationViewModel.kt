package com.example.prediai.presentation.main.education

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.prediai.domain.model.EducationVideo
import com.example.prediai.domain.model.VideoCategories
import com.example.prediai.domain.repository.EducationRepository
import com.google.ai.client.generativeai.GenerativeModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

data class EducationUiState(
    // State untuk List Screen
    val categories: List<String> = listOf(
        VideoCategories.SEMUA, VideoCategories.NUTRISI, VideoCategories.OLAHRAGA,
        VideoCategories.PERAWATAN, VideoCategories.GEJALA
    ),
    val selectedCategory: String = VideoCategories.SEMUA,
    val videos: List<EducationVideo> = emptyList(),
    val isLoading: Boolean = false,

    // State untuk Detail Screen
    val selectedVideo: EducationVideo? = null,
    val aiSummary: String? = null,
    val isSummaryLoading: Boolean = false,
    val summaryError: String? = null
)

@HiltViewModel
class EducationViewModel @Inject constructor(
    private val educationRepository: EducationRepository,
    private val generativeModel: GenerativeModel,
) : ViewModel() {

    private val _uiState = MutableStateFlow(EducationUiState())
    val uiState = _uiState.asStateFlow()

    init {
        selectCategory(VideoCategories.SEMUA)
    }

    fun selectCategory(category: String) {
        _uiState.update { it.copy(selectedCategory = category, isLoading = true) }
        educationRepository.getEducationVideos(category)
            .onEach { videos ->
                _uiState.update { it.copy(videos = videos, isLoading = false) }
            }
            .launchIn(viewModelScope)
    }

    // FUNGSI INI SEKARANG JAUH LEBIH SEDERHANA
    fun loadVideoById(videoId: String) {
        Log.d("VideoDebug", "EducationViewModel: Loading video by ID -> $videoId")
        viewModelScope.launch {
            educationRepository.getVideoById(videoId)
                .onEach { videoFromRepo ->
                    _uiState.update { it.copy(selectedVideo = videoFromRepo) }
                    Log.d("VideoDebug", "EducationViewModel: Loaded video from repo -> ${videoFromRepo?.id}, youtubeId -> ${videoFromRepo?.youtubeVideoId}")
                }
                .launchIn(viewModelScope)
        }
    }

    // FUNGSI UTAMA YANG BARU UNTUK MERANGKUM VIDEO
    fun summarizeFromMetadata(video: EducationVideo) {
        viewModelScope.launch {
            _uiState.update { it.copy(isSummaryLoading = true, summaryError = null, aiSummary = null) }
            try {
                // --- GANTI PROMPT LAMA DENGAN VERSI BARU INI ---
                val prompt = """
                    Tugas: Buat rangkuman informatif dari data video YouTube berikut dalam format poin-poin.

                    Data Input:
                    Judul: ${video.title}
                    Deskripsi: ${video.description}

                    Aturan & Alur Logika:
                    1.  Buat rangkuman utama berdasarkan Judul dan Deskripsi yang diberikan.
                    2.  **Analisis hasil rangkumanmu.** Jika rangkuman tersebut terasa terlalu singkat, umum, atau kurang informatif karena deskripsinya kosong atau tidak lengkap, tambahkan penjelasan tambahan".
                    3.  Penjelasan tambahan berisi 2-3 poin informasi umum atau fakta penting terkait topik utama video (berdasarkan judulnya) menggunakan pengetahuanmu sendiri.
                    4.  Jangan gunakan markdown (*) dan jangan sertakan kalimat pembuka atau penutup.
                """.trimIndent()

                val geminiResponse = generativeModel.generateContent(prompt)

                // Membersihkan teks dari markdown yang mungkin masih tersisa
                val cleanText = geminiResponse.text?.replace(Regex("[*]"), "")?.trim()

                _uiState.update {
                    it.copy(
                        aiSummary = cleanText, // Gunakan teks yang sudah dibersihkan
                        isSummaryLoading = false
                    )
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        summaryError = "Gagal membuat rangkuman: ${e.message}",
                        isSummaryLoading = false
                    )
                }
            }
        }
    }

    fun clearSelectedVideo() {
        _uiState.update { it.copy(selectedVideo = null, aiSummary = null, summaryError = null) }
    }
}