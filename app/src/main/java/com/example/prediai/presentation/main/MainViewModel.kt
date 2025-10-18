package com.example.prediai.presentation.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.prediai.domain.model.EducationVideo
import com.example.prediai.domain.model.ScheduleStatus
import com.example.prediai.domain.model.ScheduleType
import com.example.prediai.domain.repository.EducationRepository
import com.example.prediai.domain.usecase.GetSchedulesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import javax.inject.Inject

// Data class untuk menampung semua state di HomeScreen
data class MainUiState(
    val userName: String = "Sarah",
    val riskPercentage: Int? = 32,
    val lastCheckDate: String? = "15 Jan 2024",
    val lastCheckResult: String? = "Tidak ada kemungkinan gejala",
    val reminders: List<Reminder> = emptyList(), // Awalnya kosong
    val recommendations: List<EducationVideo> = emptyList()
)

// Model data sederhana untuk UI (Tidak berubah)
data class Reminder(val title: String, val time: String)

@HiltViewModel
class MainViewModel @Inject constructor(
    private val educationRepository: EducationRepository,
    private val getSchedulesUseCase: GetSchedulesUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(MainUiState())
    val uiState = _uiState.asStateFlow()

    init {
        loadRecommendations()
        loadUpcomingSchedules() // Panggil fungsi ini
    }

    private fun loadRecommendations() {
        educationRepository.getRecommendations()
            .onEach { recommendedVideos ->
                _uiState.update { it.copy(recommendations = recommendedVideos) }
            }
            .launchIn(viewModelScope)
    }

    // --- 2. PERBARUI FUNGSI INI ---
    private fun loadUpcomingSchedules() {
        // Tentukan tanggal "hari ini" dan "besok"
        val today = LocalDate.now()
        val tomorrow = today.plusDays(1)

        // Buat string formatternya
        val dateFormatter = DateTimeFormatter.ISO_LOCAL_DATE // "yyyy-MM-dd"
        val todayString = today.format(dateFormatter)
        val tomorrowString = tomorrow.format(dateFormatter)

        getSchedulesUseCase().onEach { allSchedules ->
            val upcomingReminders = allSchedules
                .filter {
                    // Filter 1: Ambil jadwal hari ini ATAU besok
                    (it.date == todayString || it.date == tomorrowString) &&
                            // Filter 2: Ambil hanya yang statusnya MENDATANG
                            it.status == ScheduleStatus.MENDATANG
                }
                // Urutkan berdasarkan tanggal, lalu berdasarkan jam
                .sortedWith(compareBy({ it.date }, { it.time }))
                .map { scheduleItem ->
                    // Buat judul (title)
                    val title = when (scheduleItem.type) {
                        ScheduleType.CEK_GULA -> "Cek Gula"
                        ScheduleType.KONSULTASI -> "Konsultasi"
                        ScheduleType.OLAHRAGA -> "Olahraga"
                        ScheduleType.MINUM_OBAT -> "Minum Obat"
                        ScheduleType.SKRINING_AI -> "Skrining AI"
                        ScheduleType.JADWAL_MAKAN -> "Jadwal Makan"
                        ScheduleType.CEK_TENSI -> "Cek Tensi"
                    }

                    // --- 3. BUAT LOGIKA TEKS WAKTU BARU ---
                    val timeText = if (scheduleItem.date == todayString) {
                        "Hari ini pukul ${scheduleItem.time}"
                    } else {
                        "Besok pukul ${scheduleItem.time}"
                    }

                    // Buat objek Reminder
                    Reminder(
                        title = title,
                        time = timeText // <-- Masukkan teks yang baru di sini
                    )
                }

            // Update UI state
            _uiState.update { it.copy(reminders = upcomingReminders) }

        }.launchIn(viewModelScope)
    }
}