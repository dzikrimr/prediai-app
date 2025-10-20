package com.example.prediai.presentation.main.notification

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
// Import use case DISMISS, bukan DELETE
import com.example.prediai.domain.usecase.DismissScheduleNotificationUseCase // <-- Benar
import com.example.prediai.domain.model.ScheduleType
import com.example.prediai.domain.usecase.GetSchedulesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.util.Locale
import javax.inject.Inject
import kotlin.random.Random

// Data class item (tidak berubah dari revisi sebelumnya)
data class NotificationItemData(
    val id: String, // ID dari ScheduleItem
    val title: String,
    val dateTime: String
)

// UI State (tidak berubah dari revisi sebelumnya)
data class NotificationUiState(
    val notifications: List<NotificationItemData> = emptyList()
)

@HiltViewModel
class NotificationViewModel @Inject constructor(
    private val getSchedulesUseCase: GetSchedulesUseCase,
    // Inject use case DISMISS
    private val dismissScheduleUseCase: DismissScheduleNotificationUseCase // <-- Benar
) : ViewModel() {

    private val _uiState = MutableStateFlow(NotificationUiState())
    val uiState = _uiState.asStateFlow()

    private val outputFormatter = DateTimeFormatter.ofPattern(
        "d MMMM • h:mm a",
        Locale("id", "ID")
    )

    init {
        loadNotifications()
    }

    private fun loadNotifications() {
        val now = LocalDateTime.now()
        Log.d("NotificationVM", "loadNotifications started listening...") // <-- LOG 1

        getSchedulesUseCase().onEach { allSchedules ->
            Log.d("NotificationVM", "Received ${allSchedules.size} schedules from repo.") // <-- LOG 2
            val mappedNotifications = allSchedules
                // Filter item yang BELUM di-dismiss
                .filter { !it.isDismissed } // <-- Filter ditambahkan
                .sortedByDescending { "${it.date}T${it.time}" }
                .map { scheduleItem ->
                    val itemDateTime = try {
                        LocalDateTime.of(
                            LocalDate.parse(scheduleItem.date, DateTimeFormatter.ISO_LOCAL_DATE),
                            LocalTime.parse(scheduleItem.time, DateTimeFormatter.ISO_LOCAL_TIME)
                        )
                    } catch (e: Exception) { now }

                    val isFuture = itemDateTime.isAfter(now)

                    NotificationItemData(
                        id = scheduleItem.id, // Sertakan ID
                        title = generateTitle(scheduleItem.type, isFuture),
                        dateTime = formatScheduleDateTime(itemDateTime)
                    )
                }

            Log.d("NotificationVM", "Mapped to ${mappedNotifications.size} notifications after filter.") // <-- LOG 3
            _uiState.update { it.copy(notifications = mappedNotifications) }

        }.launchIn(viewModelScope)
    }

    // Ganti nama fungsi menjadi DISMISS dan panggil use case DISMISS
    fun dismissNotification(notificationId: String) {
        viewModelScope.launch {
            Log.d("NotificationVM", "Attempting to dismiss ID: $notificationId") // <-- LOG 4
            try {
                dismissScheduleUseCase(notificationId)
                Log.d("NotificationVM", "Dismiss successful for ID: $notificationId") // <-- LOG 5
            } catch (e: Exception) {
                Log.e("NotificationVM", "Error dismissing ID: $notificationId", e) // <-- LOG 6
            }
        }
    }

    // Fungsi generateTitle (tidak berubah)
    private fun generateTitle(type: ScheduleType, isFuture: Boolean): String {
        val templates = if (isFuture) {
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
        return templates.random()
    }

    // Fungsi formatScheduleDateTime (tidak berubah)
    private fun formatScheduleDateTime(itemDateTime: LocalDateTime): String {
        return try {
            itemDateTime.format(outputFormatter)
        } catch (e: Exception) {
            "Invalid time"
        }
    }
}