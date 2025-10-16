package com.example.prediai.presentation.main.schedule

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.time.LocalDate
import javax.inject.Inject

enum class ScheduleStatus { MENDATANG, SELESAI, TERLEWAT }
enum class ScheduleType(val displayName: String) {
    CEK_GULA("Cek Gula Darah"),
    KONSULTASI("Konsultasi Dokter"),
    OLAHRAGA("Olahraga"),
    MINUM_OBAT("Minum Obat")
}

data class ScheduleItem(
    val id: String,
    val type: ScheduleType,
    val description: String,
    val time: String,
    val status: ScheduleStatus
)

data class ScheduleUiState(
    val selectedDate: LocalDate = LocalDate.now(),
    val currentMonth: String = "January 2024",
    val datesWithScheduledEvents: Set<Int> = setOf(4, 8, 15, 17, 19, 22),
    val schedulesForSelectedDay: List<ScheduleItem> = listOf(
        ScheduleItem("1", ScheduleType.CEK_GULA, "Regular blood sugar monitoring", "09:00 AM", ScheduleStatus.MENDATANG),
        ScheduleItem("2", ScheduleType.KONSULTASI, "Monthly check-up with Dr. Sarah", "02:30 PM", ScheduleStatus.SELESAI),
        ScheduleItem("3", ScheduleType.OLAHRAGA, "30 minutes morning walk", "06:00 AM", ScheduleStatus.TERLEWAT),
        ScheduleItem("4", ScheduleType.MINUM_OBAT, "Metformin 500mg after dinner", "08:00 PM", ScheduleStatus.MENDATANG)
    ),
    val isAddScheduleSheetVisible: Boolean = false
)

@HiltViewModel
class ScheduleViewModel @Inject constructor() : ViewModel() {
    private val _uiState = MutableStateFlow(ScheduleUiState())
    val uiState = _uiState.asStateFlow()

    fun onDateSelected(date: LocalDate) {
        _uiState.update { it.copy(selectedDate = date) }
        // TODO: Load schedules for the selected date from repository
    }

    fun showAddScheduleSheet() {
        _uiState.update { it.copy(isAddScheduleSheetVisible = true) }
    }

    fun hideAddScheduleSheet() {
        _uiState.update { it.copy(isAddScheduleSheetVisible = false) }
    }

    // Functions to handle form input will go here
}