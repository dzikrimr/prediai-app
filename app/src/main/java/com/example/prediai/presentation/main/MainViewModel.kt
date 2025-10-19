package com.example.prediai.presentation.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.prediai.domain.model.EducationVideo
import com.example.prediai.domain.model.ScheduleType
import com.example.prediai.domain.repository.EducationRepository
// --- 1. IMPORT REPOSITORY BARU ---
import com.example.prediai.domain.repository.UserRepository
import com.example.prediai.domain.usecase.GetSchedulesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import javax.inject.Inject

// Data class untuk menampung semua state di HomeScreen
data class MainUiState(
    // --- 2. UBAH DEFAULT NAME ---
    val userName: String = "...", // Ganti "Sarah" menjadi "..." atau string kosong
    val riskPercentage: Int? = 32,
    val lastCheckDate: String? = "15 Jan 2024",
    val lastCheckResult: String? = "Tidak ada kemungkinan gejala",
    val reminders: List<Reminder> = emptyList(),
    val recommendations: List<EducationVideo> = emptyList()
)

// Model data sederhana untuk UI
data class Reminder(val title: String, val time: String)

@HiltViewModel
class MainViewModel @Inject constructor(
    private val educationRepository: EducationRepository,
    private val getSchedulesUseCase: GetSchedulesUseCase,
    // --- 3. INJECT USER REPOSITORY ---
    private val userRepository: UserRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(MainUiState())
    val uiState = _uiState.asStateFlow()

    init {
        loadRecommendations()
        loadUpcomingSchedules()
        // --- 4. PANGGIL FUNGSI BARU UNTUK LOAD NAMA ---
        loadUserProfile()
    }

    // --- 5. BUAT FUNGSI BARU INI ---
    private fun loadUserProfile() {
        // Ambil profil dari cache (DataStore)
        userRepository.getCachedUserProfile()
            .onEach { userProfile ->
                if (userProfile.name.isNotBlank()) {
                    // --- PERBAIKAN DI SINI ---
                    // 1. Ambil nama lengkap, misal "Ade Nugroho"
                    val fullName = userProfile.name
                    // 2. Pecah berdasarkan spasi ["Ade", "Nugroho"] dan ambil yang pertama
                    val firstName = fullName.split(" ").first()

                    // 3. Simpan hanya nama pertamanya
                    _uiState.update { it.copy(userName = firstName) }
                } else {
                    _uiState.update { it.copy(userName = "Pengguna") } // Fallback jika nama kosong
                }
            }
            .launchIn(viewModelScope)
    }

    private fun loadRecommendations() {
        educationRepository.getRecommendations()
            .onEach { recommendedVideos ->
                _uiState.update { it.copy(recommendations = recommendedVideos) }
            }
            .launchIn(viewModelScope)
    }

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
                            itemDateTime.isAfter(now) // Gunakan 'now'
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
}