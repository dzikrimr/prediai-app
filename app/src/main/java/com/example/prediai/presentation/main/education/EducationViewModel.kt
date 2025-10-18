package com.example.prediai.presentation.main.education

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.prediai.domain.model.EducationVideo // <-- IMPORT BARU
import com.example.prediai.domain.model.VideoCategories // <-- IMPORT BARU
import com.example.prediai.domain.repository.EducationRepository // <-- IMPORT BARU
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn // <-- IMPORT BARU
import kotlinx.coroutines.flow.onEach // <-- IMPORT BARU
import kotlinx.coroutines.flow.update
import javax.inject.Inject

// HAPUS data class EducationVideo dari sini
// HAPUS object VideoCategories dari sini
// HAPUS allVideosList (dummy list) dari sini

data class EducationUiState(
    val categories: List<String> = listOf(
        VideoCategories.SEMUA,
        VideoCategories.NUTRISI,
        VideoCategories.OLAHRAGA,
        VideoCategories.PERAWATAN,
        VideoCategories.GEJALA
    ),
    val selectedCategory: String = VideoCategories.SEMUA,
    // Kita akan mengisi 'videos' dari repository
    val videos: List<EducationVideo> = emptyList() // <-- Mulai dengan list kosong
)

@HiltViewModel
class EducationViewModel @Inject constructor(
    // 1. INJECT REPOSITORY INTERFACE
    private val educationRepository: EducationRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(EducationUiState())
    val uiState = _uiState.asStateFlow()

    // 2. Buat variabel privat untuk menyimpan SEMUA video
    private var allVideosList: List<EducationVideo> = emptyList()

    init {
        // 3. Muat semua video saat ViewModel dibuat
        loadAllVideos()
    }

    private fun loadAllVideos() {
        educationRepository.getAllEducationVideos()
            .onEach { videos ->
                // 4. Simpan ke daftar master DAN perbarui UI
                allVideosList = videos
                _uiState.update {
                    it.copy(
                        videos = videos,
                        selectedCategory = VideoCategories.SEMUA // Reset filter
                    )
                }
            }
            .launchIn(viewModelScope)
    }

    // 5. Fungsi filter ini SEKARANG bekerja pada 'allVideosList'
    fun selectCategory(category: String) {
        _uiState.update { it.copy(selectedCategory = category) }

        val filteredVideos = if (category == VideoCategories.SEMUA) {
            allVideosList // <-- Gunakan daftar master
        } else {
            allVideosList.filter { it.category == category } // <-- Gunakan daftar master
        }

        _uiState.update { it.copy(videos = filteredVideos) }
    }
}