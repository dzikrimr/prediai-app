package com.example.prediai.presentation.scan

import android.content.Context
import android.graphics.drawable.BitmapDrawable
import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import coil.imageLoader
import coil.request.ImageRequest
import com.example.prediai.data.remote.scan.AnalysisResponse
import com.example.prediai.domain.model.ScanHistoryRecord
import com.example.prediai.domain.model.UserProfile
import com.example.prediai.domain.repository.AuthRepository
import com.example.prediai.domain.repository.HistoryRepository
import com.example.prediai.domain.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.io.ByteArrayOutputStream
import javax.inject.Inject
import kotlin.math.min

// Data class tidak perlu diubah, biarkan seperti ini
data class ScanResultUiState(
    val analysisData: AnalysisResponse? = null,
    val nailImage: ByteArray? = null,
    val tongueImage: ByteArray? = null
) {
    // ... (equals dan hashCode tetap sama)
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


@HiltViewModel
class ScanResultViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val authRepository: AuthRepository,
    private val historyRepository: HistoryRepository,
    savedStateHandle: SavedStateHandle,
    @ApplicationContext private val context: Context
) : ViewModel() {

    private val _uiState = MutableStateFlow(ScanResultUiState())
    val uiState = _uiState.asStateFlow()
    private val _isSaving = MutableStateFlow(false)
    val isSaving = _isSaving.asStateFlow()

    init {
        Log.d("ScanResultVM", "ViewModel initialized.")
        savedStateHandle.get<String>("historyId")?.let { historyId ->
            if (historyId.isNotBlank()) {
                Log.d("ScanResultVM", "History ID ditemukan: $historyId. Memuat data...")
                loadHistoryRecord(historyId) // <-- Diubah di sini, context sudah menjadi properti kelas
            }
        } ?: Log.d("ScanResultVM", "Tidak ada History ID. Menunggu setData() dari scan baru.")
    }

    private fun loadHistoryRecord(id: String) { // <-- Diubah di sini
        viewModelScope.launch {
            Log.d("ScanResultVM", "Menjalankan getScanRecordById untuk ID: $id")
            historyRepository.getScanRecordById(id).onSuccess { record ->
                if (record != null) {
                    Log.d("ScanResultVM", "SUKSES: Data riwayat ditemukan: $record")
                    val analysis = AnalysisResponse(
                        riskLevel = record.riskLevel,
                        riskPercentage = record.riskPercentage,
                        riskFactors = record.riskFactors
                    )

                    val nailBitmapDeferred = async { loadImageAsByteArray(record.nailImageUrl) }
                    val tongueBitmapDeferred = async { loadImageAsByteArray(record.tongueImageUrl) }

                    _uiState.update {
                        it.copy(
                            analysisData = analysis,
                            nailImage = nailBitmapDeferred.await(),
                            tongueImage = tongueBitmapDeferred.await()
                        )
                    }
                    Log.d("ScanResultVM", "UI State telah diperbarui dengan data riwayat.")
                } else {
                    Log.e("ScanResultVM", "GAGAL: Data (record) null meskipun repository sukses.")
                }
            }.onFailure { exception ->
                Log.e("ScanResultVM", "GAGAL: Fungsi getScanRecordById gagal.", exception)
            }
        }
    }

    private suspend fun loadImageAsByteArray(url: String): ByteArray? { // <-- Diubah di sini
        if (url.isBlank()) return null
        return try {
            val request = ImageRequest.Builder(context) // Menggunakan context dari constructor
                .data(url)
                .allowHardware(false)
                .build()
            val drawable = context.imageLoader.execute(request).drawable
            val bitmap = (drawable as? BitmapDrawable)?.bitmap

            bitmap?.let {
                val stream = ByteArrayOutputStream()
                it.compress(android.graphics.Bitmap.CompressFormat.JPEG, 90, stream)
                stream.toByteArray()
            }
        } catch (e: Exception) {
            Log.e("ScanResultVM", "Gagal memuat gambar dari URL: $url", e)
            null
        }
    }

    fun setData(
        analysis: AnalysisResponse,
        nailPhoto: ByteArray?,
        tonguePhoto: ByteArray?
    ) {
        viewModelScope.launch {
            val uid = authRepository.getCurrentUser()?.uid
            val userProfile = uid?.let { userRepository.getUserProfileFromFirebase(it) }

            val combinedAnalysis = if (userProfile != null) {
                calculateCombinedRisk(analysis, userProfile)
            } else {
                analysis // Jika profil tidak ditemukan, gunakan hasil scan asli
            }

            _uiState.update {
                it.copy(
                    analysisData = combinedAnalysis,
                    nailImage = nailPhoto,
                    tongueImage = tonguePhoto
                )
            }
            saveToHistory()
        }
    }

    fun saveToHistory() {
        viewModelScope.launch {
            _isSaving.value = true
            try {
                val nailBytes = _uiState.value.nailImage
                val tongueBytes = _uiState.value.tongueImage
                val analysis = _uiState.value.analysisData

                if (nailBytes == null || tongueBytes == null || analysis == null) {
                    throw Exception("Data tidak lengkap untuk disimpan")
                }

                // Upload gambar secara paralel
                val nailUrlDeferred = async { historyRepository.uploadImage(nailBytes) }
                val tongueUrlDeferred = async { historyRepository.uploadImage(tongueBytes) }

                val nailResult = nailUrlDeferred.await()
                val tongueResult = tongueUrlDeferred.await()

                if (nailResult.isSuccess && tongueResult.isSuccess) {
                    val record = ScanHistoryRecord(
                        riskLevel = analysis.riskLevel,
                        riskPercentage = analysis.riskPercentage,
                        riskFactors = analysis.riskFactors,
                        nailImageUrl = nailResult.getOrThrow(),
                        tongueImageUrl = tongueResult.getOrThrow()
                    )
                    historyRepository.saveScanRecord(record).getOrThrow()
                    // Di sini Anda bisa menambahkan state untuk notifikasi "Berhasil Disimpan"
                } else {
                    throw Exception("Gagal mengunggah gambar")
                }

            } catch (e: Exception) {
                // Di sini Anda bisa menambahkan state untuk notifikasi error
            } finally {
                _isSaving.value = false
            }
        }
    }

    private fun calculateCombinedRisk(
        scanResult: AnalysisResponse,
        userProfile: UserProfile
    ): AnalysisResponse {
        var questionnaireScore = 0f
        val questionnaireRiskFactors = mutableListOf<String>()

        // 1. Hitung skor dari Gejala dan Riwayat Medis
        userProfile.symptomsAndHistory.forEach { (question, answer) ->
            if (answer) { // Jika jawabannya "Ya" (true)
                questionnaireScore += 8 // Tambah 8 poin untuk setiap gejala/riwayat
                when {
                    question.contains("diabetes") -> questionnaireRiskFactors.add("Riwayat diabetes dalam keluarga")
                    question.contains("lelah") -> questionnaireRiskFactors.add("Sering merasa mudah lelah")
                    question.contains("haus") -> questionnaireRiskFactors.add("Sering merasa haus berlebihan")
                    // Tambahkan deskripsi lain jika perlu
                }
            }
        }

        // 2. Hitung skor dari Gaya Hidup
        userProfile.lifestyle.forEach { (question, answer) ->
            when {
                question.contains("aktivitas fisik") && answer == "Jarang" -> {
                    questionnaireScore += 10
                    questionnaireRiskFactors.add("Jarang melakukan aktivitas fisik")
                }
                question.contains("gula") && (answer == "Sering" || answer == "Setiap Hari") -> {
                    questionnaireScore += 10
                    questionnaireRiskFactors.add("Sering mengonsumsi makanan/minuman tinggi gula")
                }
                question.contains("merokok") && answer == "Ya" -> {
                    questionnaireScore += 10
                    questionnaireRiskFactors.add("Memiliki kebiasaan merokok atau minum alkohol")
                }
                question.contains("tidur") && answer == "< 5 jam" -> {
                    questionnaireScore += 5
                    questionnaireRiskFactors.add("Waktu tidur rata-rata kurang dari 5 jam")
                }
            }
        }

        // Batasi skor kuesioner maksimal 100
        questionnaireScore = min(questionnaireScore, 100f)

        // 3. Logika Kombinasi Skor
        val originalScanPercent = scanResult.riskPercentage
        var adjustedPercent = originalScanPercent

        if (questionnaireScore < 30) { // Risiko kuesioner RENDAH -> Melemahkan hasil scan
            adjustedPercent *= 0.7f // Kurangi persentase scan sebesar 30%
        } else if (questionnaireScore > 60) { // Risiko kuesioner TINGGI -> Menguatkan hasil scan
            adjustedPercent = (adjustedPercent * 0.6f) + (questionnaireScore * 0.4f) // Rata-rata terbobot
        }
        // Jika risiko kuesioner SEDANG, kita biarkan persentase scan lebih dominan (tidak ada perubahan signifikan)

        // Pastikan persentase tidak melebihi 99%
        adjustedPercent = min(adjustedPercent, 99.0f)

        // 4. Tentukan Risk Level baru dan gabungkan Risk Factors
        val newRiskLevel = when {
            adjustedPercent > 70 -> "Tinggi"
            adjustedPercent > 50 -> "Sedang"
            else -> "Rendah"
        }
        val combinedRiskFactors = (scanResult.riskFactors + questionnaireRiskFactors).distinct()

        // 5. Kembalikan hasil analisis yang baru
        return AnalysisResponse(
            riskLevel = newRiskLevel,
            riskPercentage = adjustedPercent,
            riskFactors = combinedRiskFactors
        )
    }
}