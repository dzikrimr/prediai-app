package com.example.prediai.presentation.scan

import androidx.lifecycle.ViewModel
import com.example.prediai.data.remote.scan.AnalysisResponse
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.util.Arrays // Pastikan import ini ada

data class ScanResultUiState(
    val analysisData: AnalysisResponse? = null,
    val nailImage: ByteArray? = null,
    val tongueImage: ByteArray? = null
) {
    // Override equals dan hashCode agar StateFlow berfungsi benar dengan ByteArray
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        other as ScanResultUiState
        if (analysisData != other.analysisData) return false
        if (nailImage != null) {
            if (other.nailImage == null) return false
            if (!nailImage.contentEquals(other.nailImage)) return false
        } else if (other.nailImage != null) return false
        if (tongueImage != null) {
            if (other.tongueImage == null) return false
            if (!tongueImage.contentEquals(other.tongueImage)) return false
        } else if (other.tongueImage != null) return false
        return true
    }

    override fun hashCode(): Int {
        var result = analysisData?.hashCode() ?: 0
        result = 31 * result + (nailImage?.contentHashCode() ?: 0)
        result = 31 * result + (tongueImage?.contentHashCode() ?: 0)
        return result
    }
}


class ScanResultViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(ScanResultUiState())
    val uiState = _uiState.asStateFlow()

    // --- HANYA ADA SATU FUNGSI UNTUK MENGATUR SEMUA DATA ---
    fun setData(
        analysis: AnalysisResponse,
        nailPhoto: ByteArray?,
        tonguePhoto: ByteArray?
    ) {
        _uiState.update {
            it.copy(
                analysisData = analysis,
                nailImage = nailPhoto,
                tongueImage = tonguePhoto
            )
        }
    }
}