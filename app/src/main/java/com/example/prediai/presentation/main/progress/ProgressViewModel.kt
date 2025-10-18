package com.example.prediai.presentation.main.progress

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
import java.util.Calendar
import java.util.Date
import java.util.Locale
import javax.inject.Inject

enum class ScanStatus { LOW, MEDIUM, HIGH }
enum class ChartFilter { ONE_WEEK, ONE_MONTH, SIX_MONTHS, ALL_TIME }

data class ScanResult(
    val id: String,
    val date: String,
    val riskLevel: String,
    val riskPercentage: Int,
    val time: String,
    val status: ScanStatus
)

data class ProgressUiState(
    val isLoading: Boolean = true,
    val totalScans: Int = 0,
    val averageRisk: Int = 0,
    val selectedFilter: ChartFilter = ChartFilter.ONE_WEEK,
    val chartData: List<Pair<String, Float>> = emptyList(),
    val recentScans: List<ScanResult> = emptyList()
)

@HiltViewModel
class ProgressViewModel @Inject constructor(
    private val historyRepository: HistoryRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(ProgressUiState())
    val uiState = _uiState.asStateFlow()

    private var allHistoryRecords: List<ScanHistoryRecord> = emptyList()

    init {
        loadScanHistory()
    }

    private fun loadScanHistory() {
        viewModelScope.launch {
            historyRepository.getScanHistory().collect { result ->
                result.onSuccess { records ->
                    allHistoryRecords = records
                    if (records.isNotEmpty()) {
                        processRecords(records)
                    } else {
                        _uiState.update { it.copy(isLoading = false, totalScans = 0, averageRisk = 0, recentScans = emptyList(), chartData = emptyList()) }
                    }
                }.onFailure {
                    _uiState.update { it.copy(isLoading = false) }
                }
            }
        }
    }

    private fun processRecords(records: List<ScanHistoryRecord>) {
        val totalScans = records.size
        val averageRisk = records.map { it.riskPercentage }.average().toInt()
        val recentScans = records.take(3).map { mapRecordToScanResult(it) }

        _uiState.update {
            it.copy(
                isLoading = false,
                totalScans = totalScans,
                averageRisk = averageRisk,
                recentScans = recentScans
            )
        }
        // Panggil update chart setelah data dasar di-update
        updateChartData()
    }

    fun onFilterChanged(filter: ChartFilter) {
        _uiState.update { it.copy(selectedFilter = filter) }
        updateChartData()
    }

    private fun updateChartData() {
        val calendar = Calendar.getInstance()
        val selectedFilter = _uiState.value.selectedFilter

        // 1. Filter record berdasarkan rentang waktu yang dipilih
        val recordsToProcess = when (selectedFilter) {
            ChartFilter.ONE_WEEK -> {
                calendar.add(Calendar.WEEK_OF_YEAR, -1)
                allHistoryRecords.filter { it.timestamp?.after(calendar.time) == true }
            }
            ChartFilter.ONE_MONTH -> {
                calendar.add(Calendar.MONTH, -1)
                allHistoryRecords.filter { it.timestamp?.after(calendar.time) == true }
            }
            ChartFilter.SIX_MONTHS -> {
                calendar.add(Calendar.MONTH, -6)
                allHistoryRecords.filter { it.timestamp?.after(calendar.time) == true }
            }
            ChartFilter.ALL_TIME -> allHistoryRecords
        }

        if (recordsToProcess.isEmpty()) {
            _uiState.update { it.copy(chartData = emptyList()) }
            return
        }

        // 2. Tentukan format label berdasarkan filter (untuk sumbu-X grafik)
        val (groupByFormat, sortFormat) = when (selectedFilter) {
            ChartFilter.ONE_WEEK -> "EEE" to "yyyy-MM-dd" // Label: Sen, Sel, Rab...
            ChartFilter.ONE_MONTH -> "dd MMM" to "yyyy-MM-dd" // Label: 25 Okt, 26 Okt...
            else -> "MMM ''yy" to "yyyy-MM" // Label: Okt '25, Nov '25...
        }
        val groupFormatter = SimpleDateFormat(groupByFormat, Locale.forLanguageTag("id-ID"))
        val sortFormatter = SimpleDateFormat(sortFormat, Locale.forLanguageTag("id-ID"))


        // 3. Kelompokkan data, hitung rata-rata, dan urutkan
        val chartData = recordsToProcess
            .filter { it.timestamp != null }
            .groupBy { groupFormatter.format(it.timestamp!!) }
            .mapValues { entry -> entry.value.map { it.riskPercentage }.average().toFloat() }
            .map { it.key to it.value }
            .sortedBy { (dateStr, _) ->
                // Gunakan format yang bisa diurutkan untuk memastikan kronologi
                if (selectedFilter == ChartFilter.ONE_WEEK) {
                    // Pengurutan khusus untuk hari dalam seminggu
                    val dayOfWeek = Calendar.getInstance().apply { time = groupFormatter.parse(dateStr)!! }
                    dayOfWeek.get(Calendar.DAY_OF_WEEK).toLong()
                } else {
                    sortFormatter.parse(dateStr)?.time ?: 0L
                }
            }
        _uiState.update { it.copy(chartData = chartData) }
    }

    private fun mapRecordToScanResult(record: ScanHistoryRecord): ScanResult {
        val status = when {
            record.riskPercentage > 70 -> ScanStatus.HIGH
            record.riskPercentage > 50 -> ScanStatus.MEDIUM
            else -> ScanStatus.LOW
        }
        val dateFormat = SimpleDateFormat("dd MMM yyyy", Locale.forLanguageTag("id-ID"))
        val timeFormat = SimpleDateFormat("HH:mm", Locale.getDefault())

        return ScanResult(
            id = record.id,
            date = record.timestamp?.let { dateFormat.format(it) } ?: "N/A",
            riskLevel = "Risiko ${record.riskLevel}",
            riskPercentage = record.riskPercentage.toInt(),
            time = record.timestamp?.let { timeFormat.format(it) } ?: "",
            status = status
        )
    }
}