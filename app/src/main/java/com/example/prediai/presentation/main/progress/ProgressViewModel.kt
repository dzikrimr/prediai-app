package com.example.prediai.presentation.main.progress

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

enum class ScanStatus { LOW, MEDIUM, HIGH }
enum class ChartFilter { ONE_MONTH, THREE_MONTHS, ALL_TIME }

data class ScanResult(
    val date: String,
    val riskLevel: String,
    val riskPercentage: Int,
    val time: String,
    val status: ScanStatus
)

data class ProgressUiState(
    val totalScans: Int = 24,
    val averageRisk: Int = 32,
    val selectedFilter: ChartFilter = ChartFilter.ONE_MONTH,
    val chartData: List<Pair<String, Float>> = listOf(
        "Apr '24" to 3.5f, "May '24" to 4.2f, "Jun '24" to 5.1f,
        "Jul '24" to 6.3f, "Aug '24" to 7.0f, "Sep '24" to 7.8f, "Oct '24" to 8.5f
    ),
    val recentScans: List<ScanResult> = listOf(
        ScanResult("15 Jan 2024", "Risiko Rendah", 25, "09:30", ScanStatus.LOW),
        ScanResult("14 Jan 2024", "Risiko Sedang", 45, "14:20", ScanStatus.MEDIUM),
        ScanResult("13 Jan 2024", "Risiko Rendah", 20, "11:15", ScanStatus.LOW)
    )
)

@HiltViewModel
class ProgressViewModel @Inject constructor() : ViewModel() {
    private val _uiState = MutableStateFlow(ProgressUiState())
    val uiState = _uiState.asStateFlow()

    fun onFilterChanged(filter: ChartFilter) {
        // TODO: Implement data fetching logic based on the selected filter
    }
}