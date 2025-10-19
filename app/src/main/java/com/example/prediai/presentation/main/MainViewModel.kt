// File: MainViewModel.kt

package com.example.prediai.presentation.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.prediai.domain.model.ScanHistoryRecord // <<< PASTIKAN INI DIIMPOR
import com.example.prediai.domain.repository.HistoryRepository // <<< PASTIKAN INI DIIMPOR
import com.example.prediai.domain.repository.UserRepository // <<< PASTIKAN INI DIIMPOR
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Locale
import javax.inject.Inject

// Model data sederhana untuk UI
data class Reminder(val title: String, val time: String)

// Menambahkan properti 'id'
data class Recommendation(
    val id: String,
    val title: String,
    val source: String,
    val views: String,
    val imageUrl: String
)

// Data class untuk menampung semua state di HomeScreen
data class MainUiState(
    val isLoading: Boolean = true, // Tambahkan loading state
    val userName: String = "Pengguna", // Default diubah agar dipopulasi dari Repo
    val riskPercentage: Int? = null, // null jika belum ada data
    val lastCheckDate: String? = null, // null jika belum ada data
    val lastCheckResult: String? = null, // null jika belum ada data
    val reminders: List<Reminder> = listOf(
        Reminder("Take a Medicine", "Today at 7:00 AM"),
        Reminder("Take a Exercise", "Today at 2:00 PM"),
        Reminder("Take a Medicine", "Today at 7:00 PM"),
    ),
    val recommendations: List<Recommendation> = listOf(
        Recommendation("1", "Tips agar gula darah tidak naik", "Ilmu Dokter", "91rb x ditonton", "https://picsum.photos/200"),
        Recommendation("2", "Kenali gejala diabetesmu melalui tangan", "Ilmu Dokter", "91rb x ditonton", "https://picsum.photos/201"),
        Recommendation("3", "Tips agar gula darah tidak naik", "Ilmu Dokter", "91rb x ditonton", "https://picsum.photos/202"),
    ),
    val errorMessage: String? = null // Tambahkan error state
)

@HiltViewModel
class MainViewModel @Inject constructor(
    // Inject repository yang dibutuhkan
    private val historyRepository: HistoryRepository,
    private val userRepository: UserRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(MainUiState())
    val uiState: StateFlow<MainUiState> = _uiState.asStateFlow()

    init {
        loadUserData()
        loadScanSummary()
    }

    private fun loadUserData() {
        viewModelScope.launch {
            userRepository.getCachedUserProfile().collect { profile ->
                _uiState.update {
                    it.copy(userName = profile?.name ?: "Pengguna")
                }
            }
        }
    }

    private fun loadScanSummary() {
        viewModelScope.launch {
            historyRepository.getScanHistory().collect { result ->
                result.onSuccess { records ->
                    processRecords(records)
                }.onFailure { error ->
                    _uiState.update { it.copy(
                        isLoading = false,
                        errorMessage = "Gagal memuat riwayat: ${error.message}",
                        // Reset data risiko jika gagal
                        riskPercentage = null,
                        lastCheckDate = null,
                        lastCheckResult = null
                    )}
                }
            }
        }
    }

    private fun processRecords(records: List<ScanHistoryRecord>) {
        // Filter hanya record yang valid (timestamp tidak null dan riskPercentage tidak null/valid)
        val validRecords = records.filter { it.timestamp != null && it.riskPercentage > 0 } // Asumsikan riskPercentage adalah Int > 0 untuk validitas

        // Cek jika list kosong atau tidak ada record valid
        if (validRecords.isEmpty()) {
            _uiState.update { it.copy(
                isLoading = false,
                riskPercentage = 0, // 0% jika memang belum ada riwayat sama sekali
                lastCheckDate = "Belum Ada",
                lastCheckResult = "Belum Ada Pemeriksaan",
                errorMessage = null // Pastikan error dihilangkan
            )}
            return
        }

        // Ambil data rata-rata dan scan terakhir
        val averageRisk = validRecords.map { it.riskPercentage }.average().toInt()

        // Safety check untuk memastikan lastRecord tidak null (meskipun validRecords.isNotEmpty() sudah menjamin)
        val lastRecord = validRecords.sortedByDescending { it.timestamp!! }.firstOrNull()

        if (lastRecord == null) {
            _uiState.update { it.copy(
                isLoading = false,
                riskPercentage = averageRisk, // Gunakan rata-rata meskipun lastRecord null
                lastCheckDate = "Data tidak valid",
                lastCheckResult = "Data pemeriksaan terakhir tidak lengkap"
            )}
            return
        }


        // Map hasil scan terakhir
        val lastScanSummary = mapRecordToSummary(lastRecord)

        _uiState.update {
            it.copy(
                isLoading = false,
                riskPercentage = averageRisk, // Ini yang Anda harapkan 51%
                lastCheckDate = lastScanSummary.date,
                lastCheckResult = lastScanSummary.result,
                errorMessage = null
            )
        }
    }

    // FUNGSI HELPER UNTUK MENGAMBIL DATA TERAKHIR
    private data class LastScanSummary(val date: String, val result: String)

    private fun mapRecordToSummary(record: ScanHistoryRecord): LastScanSummary {
        val dateFormat = SimpleDateFormat("dd MMM yyyy", Locale.forLanguageTag("id-ID"))
        val dateString = record.timestamp?.let { dateFormat.format(it) } ?: "N/A"

        // Logika untuk menentukan ringkasan hasil
        val resultText = when {
            record.riskPercentage > 75 -> "Risiko tinggi. Wajib konsultasi dokter!"
            record.riskPercentage > 50 -> "Terdeteksi beberapa indikator. Disarankan konsultasi dokter."
            else -> "Tidak ada kemungkinan gejala serius terdeteksi."
        }

        return LastScanSummary(dateString, resultText)
    }
}