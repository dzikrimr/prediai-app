// File: MainViewModel.kt

package com.example.prediai.presentation.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.prediai.domain.model.EducationVideo
import com.example.prediai.domain.model.ScanHistoryRecord
import com.example.prediai.domain.model.ScheduleType
import com.example.prediai.domain.model.ScheduleItem // Asumsi: Import ini diperlukan untuk ScheduleType
import com.example.prediai.domain.repository.EducationRepository // <<< PASTIKAN INI DIIMPOR
import com.example.prediai.domain.repository.HistoryRepository // <<< PASTIKAN INI DIIMPOR
import com.example.prediai.domain.repository.UserRepository // <<< PASTIKAN INI DIIMPOR
import com.example.prediai.domain.usecase.GetSchedulesUseCase // <<< PASTIKAN INI DIIMPOR
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
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
    val profileImageUrl: String? = null, // URL Foto Profil
    val riskPercentage: Int? = null, // null jika belum ada data
    val lastCheckDate: String? = null, // null jika belum ada data
    val lastCheckResult: String? = null, // null jika belum ada data
    val reminders: List<Reminder> = emptyList(), // Diambil dari schedules
    val recommendations: List<EducationVideo> = emptyList(), // Diambil dari EducationRepository
    val errorMessage: String? = null // Tambahkan error state
)

@HiltViewModel
class MainViewModel @Inject constructor(
    // Inject repository dan use case baru
    private val historyRepository: HistoryRepository,
    private val userRepository: UserRepository,
    private val educationRepository: EducationRepository, // <<< TAMBAH INI
    private val getSchedulesUseCase: GetSchedulesUseCase // <<< TAMBAH INI
) : ViewModel() {

    private val _uiState = MutableStateFlow(MainUiState())
    val uiState: StateFlow<MainUiState> = _uiState.asStateFlow()

    init {
        loadUserData()
        loadScanSummary()
        // --- MEMUAT DATA BARU ---
        loadRecommendations()
        loadUpcomingSchedules()
    }

    // --- FUNGSI BARU DARI KODE ASLI (untuk rekomendasi) ---
    private fun loadRecommendations() {
        // Menggunakan EducationVideo, bukan Recommendation kustom Anda
        educationRepository.getRecommendations()
            .onEach { recommendedVideos ->
                _uiState.update { it.copy(recommendations = recommendedVideos) }
            }
            .launchIn(viewModelScope)
    }

    // --- FUNGSI BARU DARI KODE ASLI (untuk jadwal/reminders) ---
    private fun loadUpcomingSchedules() {
        val today = LocalDate.now()
        val tomorrow = today.plusDays(1)
        val dateFormatter = DateTimeFormatter.ISO_LOCAL_DATE
        val todayString = today.format(dateFormatter)
        val tomorrowString = tomorrow.format(dateFormatter)

        val now = LocalDateTime.now()

        getSchedulesUseCase().onEach { allSchedules ->
            val upcomingReminders = allSchedules
                .filter { scheduleItem ->
                    val isTodayOrTomorrow = scheduleItem.date == todayString || scheduleItem.date == tomorrowString
                    if (!isTodayOrTomorrow) {
                        false
                    } else {
                        try {
                            val itemDateTime = LocalDateTime.parse("${scheduleItem.date}T${scheduleItem.time}")
                            itemDateTime.isAfter(now)
                        } catch (e: Exception) {
                            false
                        }
                    }
                }
                .sortedWith(compareBy({ it.date }, { it.time }))
                .map { scheduleItem ->
                    val title = when (scheduleItem.type) {
                        ScheduleType.CEK_GULA -> "Cek Gula"
                        ScheduleType.KONSULTASI -> "Konsultasi"
                        ScheduleType.OLAHRAGA -> "Olahraga"
                        ScheduleType.MINUM_OBAT -> "Minum Obat"
                        ScheduleType.SKRINING_AI -> "Skrining AI"
                        ScheduleType.JADWAL_MAKAN -> "Jadwal Makan"
                        ScheduleType.CEK_TENSI -> "Cek Tensi Darah"
                    }

                    val timeText = if (scheduleItem.date == todayString) {
                        "Hari ini pada ${scheduleItem.time}"
                    } else {
                        "Besok pada ${scheduleItem.time}"
                    }

                    Reminder(
                        title = title,
                        time = timeText
                    )
                }

            _uiState.update { it.copy(reminders = upcomingReminders) }

        }.launchIn(viewModelScope)
    }

    private fun loadUserData() {
        viewModelScope.launch {

            // --- 1. Ambil UID pengguna saat ini ---
            val firebaseUser = userRepository.getCurrentUser()
            val uid = firebaseUser?.uid ?: return@launch // Jika tidak ada user, hentikan

            // --- 2. Ambil data dari cache (untuk tampilan cepat) ---
            userRepository.getCachedUserProfile()
                .take(1) // Ambil nilai pertama dari cache, lalu hentikan koleksi
                .collect { cachedProfile ->

                    // Proses dan tampilkan data cache
                    val fullNameCache = cachedProfile?.name.orEmpty()
                    val firstNameCache = if (fullNameCache.isNotBlank()) fullNameCache.split(" ")
                        .first() else "Pengguna"
                    val imageUrlCache = cachedProfile?.profileImageUrl

                    // Update UI dengan data cache (jika ada)
                    if (firstNameCache != "Pengguna" || !imageUrlCache.isNullOrEmpty()) {
                        _uiState.update {
                            it.copy(
                                userName = firstNameCache,
                                profileImageUrl = imageUrlCache
                            )
                        }
                    }

                    val firebaseProfile = userRepository.getUserProfileFromFirebase(uid)

                    if (firebaseProfile != null) {
                        userRepository.saveUserProfileToCache(uid, firebaseProfile)

                        val fullNameFirebase = firebaseProfile.name.orEmpty()
                        val firstNameFirebase =
                            if (fullNameFirebase.isNotBlank()) fullNameFirebase.split(" ")
                                .first() else "Pengguna"
                        val imageUrlFirebase = firebaseProfile.profileImageUrl

                        _uiState.update {
                            it.copy(
                                userName = firstNameFirebase,
                                profileImageUrl = imageUrlFirebase
                            )
                        }
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

    // --- FUNGSI YANG SUDAH ADA (Helper) ---
    private fun processRecords(records: List<ScanHistoryRecord>) {
        val validRecords = records.filter { it.timestamp != null && it.riskPercentage > 0 }

        if (validRecords.isEmpty()) {
            _uiState.update { it.copy(
                isLoading = false,
                riskPercentage = 0,
                lastCheckDate = "Belum Ada",
                lastCheckResult = "Belum Ada Pemeriksaan",
                errorMessage = null
            )}
            return
        }

        val averageRisk = validRecords.map { it.riskPercentage }.average().toInt()
        val lastRecord = validRecords.sortedByDescending { it.timestamp!! }.firstOrNull()

        if (lastRecord == null) {
            _uiState.update { it.copy(
                isLoading = false,
                riskPercentage = averageRisk,
                lastCheckDate = "Data tidak valid",
                lastCheckResult = "Data pemeriksaan terakhir tidak lengkap"
            )}
            return
        }

        val lastScanSummary = mapRecordToSummary(lastRecord)

        _uiState.update {
            it.copy(
                isLoading = false,
                riskPercentage = averageRisk,
                lastCheckDate = lastScanSummary.date,
                lastCheckResult = lastScanSummary.result,
                errorMessage = null
            )
        }
    }

    private data class LastScanSummary(val date: String, val result: String)

    private fun mapRecordToSummary(record: ScanHistoryRecord): LastScanSummary {
        val dateFormat = SimpleDateFormat("dd MMM yyyy", Locale.forLanguageTag("id-ID"))
        val dateString = record.timestamp?.let { dateFormat.format(it) } ?: "N/A"

        val resultText = when {
            record.riskPercentage > 75 -> "Risiko tinggi. Wajib konsultasi dokter!"
            record.riskPercentage > 50 -> "Terdeteksi beberapa indikator. Disarankan konsultasi dokter."
            else -> "Tidak ada kemungkinan gejala serius terdeteksi."
        }

        return LastScanSummary(dateString, resultText)
    }
}