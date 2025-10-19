package com.example.prediai.presentation.main.notification

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.prediai.domain.model.ScheduleType
import com.example.prediai.domain.usecase.GetSchedulesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import java.time.LocalDate
import java.time.LocalDateTime // <-- IMPORT BARU
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.util.Locale
import javax.inject.Inject

data class NotificationItemData(
    val title: String,
    val dateTime: String
)

// State for the entire UI (Tidak berubah)
data class NotificationUiState(
    val notifications: List<NotificationItemData> = emptyList()
)

@HiltViewModel
class NotificationViewModel @Inject constructor(
    private val getSchedulesUseCase: GetSchedulesUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(NotificationUiState())
    val uiState = _uiState.asStateFlow()

    private val outputFormatter = DateTimeFormatter.ofPattern(
        "d MMMM • h:mm a", // Contoh: "19 Oktober • 2:30 PM"
        Locale("id", "ID")
    )

    init {
        loadNotifications()
    }

    private fun loadNotifications() {
        getSchedulesUseCase().onEach { allSchedules ->
            // 1. Ambil waktu "sekarang"
            val now = LocalDateTime.now()

            val mappedNotifications = allSchedules
                .sortedByDescending { "${it.date}T${it.time}" }
                .map { scheduleItem ->

                    // 2. Tentukan status (lewat atau akan datang)
                    val itemDateTime = try {
                        LocalDateTime.of(
                            LocalDate.parse(scheduleItem.date, DateTimeFormatter.ISO_LOCAL_DATE),
                            LocalTime.parse(scheduleItem.time, DateTimeFormatter.ISO_LOCAL_TIME)
                        )
                    } catch (e: Exception) {
                        now // Fallback jika format salah
                    }

                    val isFuture = itemDateTime.isAfter(now)

                    // 3. Buat notifikasi berdasarkan status
                    NotificationItemData(
                        title = generateTitle(scheduleItem.type, isFuture), // <- Judul bervariasi
                        dateTime = formatScheduleDateTime(itemDateTime) // <- Waktu diformat
                    )
                }

            _uiState.update { it.copy(notifications = mappedNotifications) }

        }.launchIn(viewModelScope)
    }

    /**
     * Helper untuk membuat judul yang bervariasi (Request 1 & 2)
     */
    private fun generateTitle(type: ScheduleType, isFuture: Boolean): String {
        val templates = if (isFuture) {
            // --- Teks untuk jadwal AKAN DATANG ---
            when (type) {
                ScheduleType.CEK_GULA -> listOf("Jangan lupa cek gula darahmu", "Pengingat: Cek Gula Darah", "Waktunya cek gula!")
                ScheduleType.KONSULTASI -> listOf("Jadwal konsultasi dokter", "Pengingat: Konsultasi", "Saatnya bertemu dokter")
                ScheduleType.OLAHRAGA -> listOf("Ayo olahraga!", "Pengingat: Waktunya bergerak", "Jangan lupa jadwal olahragamu")
                ScheduleType.MINUM_OBAT -> listOf("Waktunya minum obat", "Jangan lupa obatmu!", "Pengingat Minum Obat")
                ScheduleType.SKRINING_AI -> listOf("Waktunya Skrining AI", "Ayo lakukan scan PrediAI", "Pengingat: Skrining AI")
                ScheduleType.JADWAL_MAKAN -> listOf("Waktunya makan!", "Jangan telat makan, ya", "Pengingat Jadwal Makan")
                ScheduleType.CEK_TENSI -> listOf("Waktunya Cek Tensi Darah", "Jangan lupa cek tensi", "Pengingat: Cek Tensi")
            }
        } else {
            // --- Teks untuk jadwal TERLEWAT ---
            when (type) {
                ScheduleType.CEK_GULA -> listOf("Cek gula darah terlewat", "Anda lupa cek gula darah", "Jadwal cek gula terlewat")
                ScheduleType.KONSULTASI -> listOf("Jadwal konsultasi terlewat", "Anda melewatkan konsultasi", "Konsultasi dokter terlewat")
                ScheduleType.OLAHRAGA -> listOf("Jadwal olahraga terlewat", "Anda tidak berolahraga", "Olahraga terlewat")
                ScheduleType.MINUM_OBAT -> listOf("Obat terlewat", "Anda lupa minum obat", "Jadwal minum obat terlewat")
                ScheduleType.SKRINING_AI -> listOf("Skrining AI terlewat", "Anda melewatkan Skrining AI", "Jadwal scan terlewat")
                ScheduleType.JADWAL_MAKAN -> listOf("Jadwal makan terlewat", "Anda telat makan", "Waktu makan terlewat")
                ScheduleType.CEK_TENSI -> listOf("Cek tensi terlewat", "Anda lupa cek tensi", "Jadwal cek tensi terlewat")
            }
        }
        // Pilih satu secara acak dari daftar template
        return templates.random()
    }

    /**
     * Helper untuk memformat waktu (sudah di-parse)
     */
    private fun formatScheduleDateTime(itemDateTime: LocalDateTime): String {
        return try {
            itemDateTime.format(outputFormatter)
        } catch (e: Exception) {
            "Invalid time"
        }
    }
}