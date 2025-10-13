package com.example.prediai.presentation.main

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

// Data class untuk menampung semua state di HomeScreen
data class MainUiState(
    val userName: String = "Sarah",
    val riskPercentage: Int? = 60, // null jika belum ada data
    val lastCheckDate: String? = "15 Jan 2024", // null jika belum ada data
    val lastCheckResult: String? = "Tidak ada kemungkinan gejala",
    val reminders: List<Reminder> = listOf(
        Reminder("Take a Medicine", "Today at 2:00 PM"),
        Reminder("Take a Exercise", "Today at 2:00 PM"),
        Reminder("Take a Medicine", "Today at 2:00 PM"),
    ),
    val recommendations: List<Recommendation> = listOf(
        Recommendation("Tips agar gula darah tidak naik", "Ilmu Dokter", "91rb x ditonton", "https://picsum.photos/200"),
        Recommendation("Kenali gejala diabetesmu melalui tangan", "Ilmu Dokter", "91rb x ditonton", "https://picsum.photos/201"),
        Recommendation("Tips agar gula darah tidak naik", "Ilmu Dokter", "91rb x ditonton", "https://picsum.photos/202"),
    )
)

// Model data sederhana untuk UI
data class Reminder(val title: String, val time: String)
data class Recommendation(val title: String, val source: String, val views: String, val imageUrl: String)

@HiltViewModel
class MainViewModel @Inject constructor(
    // Nanti inject repository Anda di sini
) : ViewModel() {
    private val _uiState = MutableStateFlow(MainUiState())
    val uiState = _uiState.asStateFlow()

    // Di sini Anda akan memuat data dari repository Anda
    init {
        // Contoh: viewModelScope.launch { ... }
    }
}