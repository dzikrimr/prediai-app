package com.example.prediai.presentation.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.prediai.domain.model.EducationVideo
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
import java.time.LocalDateTime
import java.time.LocalDateTime.now
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
        val today = LocalDate.now()
        val tomorrow = today.plusDays(1)
        val dateFormatter = DateTimeFormatter.ISO_LOCAL_DATE
        val todayString = today.format(dateFormatter)
        val tomorrowString = tomorrow.format(dateFormatter)

        getSchedulesUseCase().onEach { allSchedules ->
            val upcomingReminders = allSchedules
                .filter { scheduleItem ->
                    // --- 2. PERBARUI LOGIKA FILTER ---
                    val isTodayOrTomorrow = scheduleItem.date == todayString || scheduleItem.date == tomorrowString
                    if (!isTodayOrTomorrow) {
                        false // Bukan hari ini atau besok, buang
                    } else {
                        // Ini adalah hari ini atau besok,
                        // sekarang cek apakah waktunya MASIH di masa depan
                        try {
                            val itemDateTime = LocalDateTime.parse("${scheduleItem.date}T${scheduleItem.time}")
                            itemDateTime.isAfter(now())
                        } catch (e: Exception) {
                            false // Format waktu salah, buang
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