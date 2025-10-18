package com.example.prediai.presentation.main.schedule

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.prediai.domain.model.ScheduleItem // <-- Import dari domain
import com.example.prediai.domain.model.ScheduleType // <-- Import dari domain
import com.example.prediai.domain.model.ScheduleStatus // <-- Import dari domain
import com.example.prediai.domain.usecase.AddScheduleUseCase // <-- IMPORT BARU
import com.example.prediai.domain.usecase.GetSchedulesUseCase // <-- IMPORT BARU
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn // <-- IMPORT BARU
import kotlinx.coroutines.flow.onEach // <-- IMPORT BARU
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.DateTimeFormatter
import java.util.Locale
import javax.inject.Inject

// ... (HAPUS data class ScheduleItem, ScheduleType, ScheduleStatus dari sini) ...

data class ScheduleUiState(
    val selectedDate: LocalDate = LocalDate.now(),
    val datesWithScheduledEvents: Set<LocalDate> = emptySet(),
    val schedulesForSelectedDay: List<ScheduleItem> = emptyList(),
    val isAddScheduleSheetVisible: Boolean = false,
    val currentMonth: String = YearMonth.now().format(
        DateTimeFormatter.ofPattern("MMMM yyyy", Locale.getDefault())
    )
    // Kamu bisa tambahkan state loading/error di sini
)

@HiltViewModel
class ScheduleViewModel @Inject constructor(
    // 1. INJECT USE CASE
    private val getSchedulesUseCase: GetSchedulesUseCase,
    private val addScheduleUseCase: AddScheduleUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(ScheduleUiState())
    val uiState = _uiState.asStateFlow()

    // 2. Buat satu list master untuk menyimpan semua jadwal
    private var allSchedules: List<ScheduleItem> = emptyList()

    // 3. Buat formatter tanggal
    private val dateFormatter = DateTimeFormatter.ISO_LOCAL_DATE // "yyyy-MM-dd"

    init {
        // 4. Langsung panggil flow untuk mendengarkan perubahan
        loadSchedules()
    }

    private fun loadSchedules() {
        getSchedulesUseCase().onEach { schedules ->
            allSchedules = schedules // Simpan di list master

            // Perbarui penanda kalender
            val datesWithEvents = schedules.map {
                LocalDate.parse(it.date, dateFormatter)
            }.toSet()
            _uiState.update { it.copy(datesWithScheduledEvents = datesWithEvents) }

            // Refresh jadwal untuk hari yang sedang dipilih
            loadSchedulesForDay(uiState.value.selectedDate)
        }.launchIn(viewModelScope)
    }

    fun onDateSelected(date: LocalDate) {
        _uiState.update { it.copy(selectedDate = date) }
        loadSchedulesForDay(date)
    }

    // Fungsi ini tidak perlu load data, hanya filter dari list master
    private fun loadSchedulesForDay(date: LocalDate) {
        val selectedDateString = date.format(dateFormatter)
        val schedulesForDay = allSchedules.filter { it.date == selectedDateString }
        _uiState.update { it.copy(schedulesForSelectedDay = schedulesForDay) }
    }

    // Fungsi onMonthChanged mungkin tidak perlu lagi load data
    // karena 'loadSchedules' sudah mendengarkan SEMUA data
    fun onMonthChanged(newMonth: YearMonth) {
        _uiState.update { it.copy(
            currentMonth = newMonth.format(
                DateTimeFormatter.ofPattern("MMMM yyyy", Locale.getDefault())
            )
        )}
    }

    // --- Bottom Sheet Functions ---
    fun showAddScheduleSheet() {
        _uiState.update { it.copy(isAddScheduleSheetVisible = true) }
    }

    fun hideAddScheduleSheet() {
        _uiState.update { it.copy(isAddScheduleSheetVisible = false) }
    }

    // 5. Buat fungsi untuk MENYIMPAN jadwal
    fun saveSchedule(
        typeString: String, // Kita terima String dari UI
        notes: String,      // Kita terima 'notes' sebagai deskripsi
        time: String
    ) {
        viewModelScope.launch {
            // Lakukan konversi String ke Enum di sini
            val scheduleType = when (typeString) {
                "Cek Gula Darah" -> ScheduleType.CEK_GULA
                "Konsultasi Dokter" -> ScheduleType.KONSULTASI
                "Olahraga" -> ScheduleType.OLAHRAGA
                "Minum Obat" -> ScheduleType.MINUM_OBAT
                "Skrining AI" -> ScheduleType.SKRINING_AI
                "Jadwal Makan" -> ScheduleType.JADWAL_MAKAN
                "Cek Tensi Darah" -> ScheduleType.CEK_TENSI
                else -> return@launch // Jika tidak valid, jangan simpan
            }

            val newSchedule = ScheduleItem(
                // id akan dibuat oleh repository
                type = scheduleType,
                description = notes, // 'notes' dari UI disimpan sebagai 'description'
                date = uiState.value.selectedDate.format(dateFormatter), // Ambil tgl terpilih
                time = time,
                status = ScheduleStatus.MENDATANG
            )
            try {
                addScheduleUseCase(newSchedule)
                hideAddScheduleSheet() // Tutup sheet jika sukses
            } catch (e: Exception) {
                // TODO: Tampilkan error ke user
            }
        }
    }
}