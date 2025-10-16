package com.example.prediai.presentation.main.history

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

// Enum untuk merepresentasikan status/level risiko
enum class HistoryStatus { SEMUA, NORMAL, PERINGATAN, TINGGI }

// Model data untuk satu item riwayat
data class ScanHistoryItem(
    val title: String,
    val date: String,
    val description: String = "Scan kuku & lidah",
    val timeAgo: String,
    val percentage: Int,
    val status: HistoryStatus
)

// State untuk seluruh UI HistoryScreen
data class HistoryUiState(
    val totalScans: Int = 16,
    val selectedFilter: HistoryStatus = HistoryStatus.SEMUA,
    val historyItems: List<ScanHistoryItem> = listOf(
        ScanHistoryItem("Risiko Tinggi", "Hari ini, 14:30", timeAgo = "2 menit yang lalu", percentage = 85, status = HistoryStatus.TINGGI),
        ScanHistoryItem("Peringatan", "Kemarin, 09:15", timeAgo = "1 hari yang lalu", percentage = 65, status = HistoryStatus.PERINGATAN),
        ScanHistoryItem("Normal", "2 hari lalu, 16:45", timeAgo = "2 hari yang lalu", percentage = 15, status = HistoryStatus.NORMAL)
    )
)

@HiltViewModel
class HistoryViewModel @Inject constructor() : ViewModel() {
    private val _uiState = MutableStateFlow(HistoryUiState())
    val uiState = _uiState.asStateFlow()

    fun onFilterChange(newFilter: HistoryStatus) {
        _uiState.update { it.copy(selectedFilter = newFilter) }
        // TODO: Di sini nanti Anda akan memuat data dari repository berdasarkan filter yang dipilih
    }
}