package com.example.prediai.presentation.main.education

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

// Model data yang sama dengan Recommendation di HomeScreen
data class EducationVideo(
    val id: String,
    val title: String,
    val source: String,
    val views: String,
    val imageUrl: String,
    val youtubeVideoId: String // ID video dari URL YouTube (e.g., "dQw4w9WgXcQ")
)

data class EducationUiState(
    val searchText: String = "",
    val searchHistory: List<String> = listOf("Tips Olahraga", "Rumah Sakit", "Akhir Hayat Mas Amba"),
    val videos: List<EducationVideo> = listOf(
        EducationVideo("1", "Tips agar gula darah tidak naik", "Ilmu Dokter", "91rb x ditonton", "https://picsum.photos/id/10/200", "S5-zKft4-nI"),
        EducationVideo("2", "Kenali gejala diabetesmu melalui tangan", "Ilmu Dokter", "91rb x ditonton", "https://picsum.photos/id/20/200", "9-e_v14-a-Q"),
        EducationVideo("3", "Tips agar gula darah tidak naik", "Ilmu Dokter", "91rb x ditonton", "https://picsum.photos/id/30/200", "S5-zKft4-nI"),
        EducationVideo("4", "Kenali gejala diabetesmu melalui tangan", "Ilmu Dokter", "91rb x ditonton", "https://picsum.photos/id/40/200", "9-e_v14-a-Q")
    )
)

@HiltViewModel
class EducationViewModel @Inject constructor() : ViewModel() {
    private val _uiState = MutableStateFlow(EducationUiState())
    val uiState = _uiState.asStateFlow()

    // Fungsi-fungsi untuk interaksi akan ditambahkan di sini
}