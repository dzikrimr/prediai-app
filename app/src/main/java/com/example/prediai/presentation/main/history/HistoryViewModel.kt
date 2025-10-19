package com.example.prediai.presentation.main.history

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.prediai.domain.model.ScanHistoryRecord
import com.example.prediai.domain.repository.HistoryRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.concurrent.TimeUnit
import javax.inject.Inject

// Enum untuk merepresentasikan status/level risiko
enum class HistoryStatus { SEMUA, NORMAL, PERINGATAN, TINGGI }

// Model data untuk satu item riwayat
data class ScanHistoryItem(
    val id: String,
    val title: String,
    val date: String,
    val description: String = "Scan kuku & lidah",
    val timeAgo: String,
    val percentage: Int,
    val status: HistoryStatus
)

// State untuk seluruh UI HistoryScreen
data class HistoryUiState(
    val isLoading: Boolean = true, // Mulai dengan loading
    val totalScans: Int = 0,
    val selectedFilter: HistoryStatus = HistoryStatus.SEMUA,
    val historyItems: List<ScanHistoryItem> = emptyList() // Mulai dengan list kosong
)

@HiltViewModel
class HistoryViewModel @Inject constructor(
    private val historyRepository: HistoryRepository // <-- 1. Inject Repository
) : ViewModel() {
    private val _uiState = MutableStateFlow(HistoryUiState())
    val uiState = _uiState.asStateFlow()

    private var allHistoryItems: List<ScanHistoryItem> = emptyList()

    init {
        loadHistory() // <-- 2. Panggil fungsi pemuatan data saat ViewModel dibuat
    }

    private fun loadHistory() {
        viewModelScope.launch {
            historyRepository.getScanHistory().collect { result ->
                _uiState.update { it.copy(isLoading = true) }
                result.onSuccess { records ->
                    // 3. Konversi data dari Firestore (ScanHistoryRecord) ke data untuk UI (ScanHistoryItem)
                    allHistoryItems = records.map { record ->
                        mapRecordToItem(record)
                    }
                    _uiState.update { it.copy(totalScans = allHistoryItems.size) }
                    filterAndSortItems() // Terapkan filter awal
                }.onFailure {
                    // Handle error di sini jika perlu
                }
                _uiState.update { it.copy(isLoading = false) }
            }
        }
    }

    fun onFilterChange(newFilter: HistoryStatus) {
        _uiState.update { it.copy(selectedFilter = newFilter) }
        filterAndSortItems()
    }

    private fun filterAndSortItems() {
        val filteredList = if (_uiState.value.selectedFilter == HistoryStatus.SEMUA) {
            allHistoryItems
        } else {
            allHistoryItems.filter { it.status == _uiState.value.selectedFilter }
        }
        _uiState.update { it.copy(historyItems = filteredList) }
    }

    private fun mapRecordToItem(record: ScanHistoryRecord): ScanHistoryItem {
        val status = when {
            record.riskPercentage > 70 -> HistoryStatus.TINGGI
            record.riskPercentage > 50 -> HistoryStatus.PERINGATAN
            else -> HistoryStatus.NORMAL
        }
        return ScanHistoryItem(
            id = record.id,
            title = "Risiko ${record.riskLevel}",
            date = record.timestamp?.let { SimpleDateFormat("dd MMM yyyy, HH:mm", Locale.forLanguageTag("id-ID")).format(it) } ?: "N/A",
            timeAgo = getTimeAgo(record.timestamp),
            percentage = record.riskPercentage.toInt(),
            status = status
        )
    }

    private fun getTimeAgo(date: Date?): String {
        if (date == null) return ""
        val seconds = TimeUnit.MILLISECONDS.toSeconds(Date().time - date.time)
        val minutes = TimeUnit.MILLISECONDS.toMinutes(Date().time - date.time)
        val hours = TimeUnit.MILLISECONDS.toHours(Date().time - date.time)
        val days = TimeUnit.MILLISECONDS.toDays(Date().time - date.time)

        return when {
            seconds < 60 -> "$seconds detik yang lalu"
            minutes < 60 -> "$minutes menit yang lalu"
            hours < 24 -> "$hours jam yang lalu"
            else -> "$days hari yang lalu"
        }
    }
}