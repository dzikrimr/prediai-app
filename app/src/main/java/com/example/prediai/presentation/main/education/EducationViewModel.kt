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
    // Minta Hilt untuk menyediakan GenerativeModel dari Module Anda
    private val generativeModel: GenerativeModel
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
    fun summarizeVideoFromUrl(videoUrl: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(isSummaryLoading = true, summaryError = null, aiSummary = null) }
            try {
                // --- GUNAKAN PROMPT YANG LEBIH SPESIFIK INI ---
                val prompt = """
                Tolong analisis dan berikan rangkuman **isi utama dan poin-poin penting yang dibicarakan** dalam video YouTube ini. 
                Fokuskan pada informasi **dari narasi atau dialog di video**, serta **aksi visual kunci** yang mendukung penjelasan.
                Berikan rangkuman dalam bahasa Indonesia dengan format **poin-poin bernomor**.
                URL Video: $videoUrl
            """.trimIndent()
                // --- AKHIR PROMPT YANG LEBIH SPESIFIK ---

                val response = generativeModel.generateContent(prompt)

                _uiState.update {
                    it.copy(
                        aiSummary = response.text,
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