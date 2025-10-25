package com.example.prediai.presentation.scan

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.drawable.BitmapDrawable
import android.location.Geocoder
import android.location.LocationManager
import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import coil.imageLoader
import coil.request.ImageRequest
import com.example.prediai.data.remote.scan.AnalysisResponse
import com.example.prediai.domain.model.NearbyPlace
import com.example.prediai.domain.model.ScanHistoryRecord
import com.example.prediai.domain.model.UserProfile
import com.example.prediai.domain.repository.AuthRepository
import com.example.prediai.domain.repository.HistoryRepository
import com.example.prediai.domain.repository.PlacesRepository
import com.example.prediai.domain.repository.UserRepository
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.LatLng
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.io.ByteArrayOutputStream
import java.util.Locale
import javax.inject.Inject
import kotlin.math.min

data class ScanResultUiState(
    val analysisData: AnalysisResponse? = null,
    val nailImage: ByteArray? = null,
    val tongueImage: ByteArray? = null,
    val nearbyPlaces: List<NearbyPlace> = emptyList(),
    val isLocationLoading: Boolean = false,
    val locationError: String? = null
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        other as ScanResultUiState
        if (analysisData != other.analysisData) return false
        if (nearbyPlaces != other.nearbyPlaces) return false // <--- BARIS KRITIS DITAMBAHKAN
        if (isLocationLoading != other.isLocationLoading) return false // <--- BARIS KRITIS DITAMBAHKAN
        if (locationError != other.locationError) return false // <--- BARIS KRITIS DITAMBAHKAN

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
        result = 31 * result + (nearbyPlaces.hashCode()) // <--- BARIS KRITIS DITAMBAHKAN
        result = 31 * result + isLocationLoading.hashCode() // <--- BARIS KRITIS DITAMBAHKAN
        result = 31 * result + (locationError?.hashCode() ?: 0) // <--- BARIS KRITIS DITAMBAHKAN
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
    private val placesRepository: PlacesRepository,
    savedStateHandle: SavedStateHandle,
    @ApplicationContext private val context: Context
) : ViewModel() {

    private val _uiState = MutableStateFlow(ScanResultUiState())
    val uiState = _uiState.asStateFlow()
    private val _isSaving = MutableStateFlow(false)
    val isSaving = _isSaving.asStateFlow()

    private val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)

    init {
        Log.d("ScanResultVM", "ViewModel initialized.")
        savedStateHandle.get<String>("historyId")?.let { historyId ->
            if (historyId.isNotBlank()) {
                Log.d("ScanResultVM", "History ID ditemukan: $historyId. Memuat data...")
                loadHistoryRecord(historyId) // <-- Diubah di sini, context sudah menjadi properti kelas
            }
        } ?: Log.d("ScanResultVM", "Tidak ada History ID. Menunggu setData() dari scan baru.")
    }

    private fun isLocationEnabled(): Boolean {
        val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
                locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
    }

    fun onLocationPermissionDenied() {
        _uiState.update { it.copy(locationError = "Izin lokasi diperlukan untuk menemukan fasilitas kesehatan terdekat.") }
    }

    @SuppressLint("MissingPermission")
    fun findNearbyHealthcare() {
        if (uiState.value.isLocationLoading || uiState.value.nearbyPlaces.isNotEmpty()) {
            Log.d("ScanResultVM", "findNearbyHealthcare dibatalkan: sedang memuat atau data sudah ada")
            return
        }
        viewModelScope.launch {
            _uiState.update { it.copy(isLocationLoading = true, locationError = null) }

            if (!isLocationEnabled()) {
                _uiState.update { it.copy(locationError = "Layanan lokasi (GPS) tidak aktif.", isLocationLoading = false) }
                return@launch
            }

            try {
                val location = fusedLocationClient.lastLocation.await()
                if (location != null) {
                    val userLatLng = LatLng(location.latitude, location.longitude)
                    val cityName = getCityNameFromLatLng(userLatLng)
                    if (cityName.isNullOrBlank()) {
                        _uiState.update { it.copy(locationError = "Gagal mendapatkan nama kota.", isLocationLoading = false) }
                        return@launch
                    }

                    placesRepository.findNearbyHospitalsByCity(cityName)
                        .onSuccess { places ->
                            Log.d("ScanResultVM", "Nearby places updated: ${places.size} places - $places")
                            _uiState.update { it.copy(nearbyPlaces = places.toList(), isLocationLoading = false) }
                        }
                        .onFailure { exception ->
                            Log.e("ScanResultVM", "Failed to fetch places: ${exception.message}")
                            _uiState.update { it.copy(locationError = "Gagal mencari tempat: ${exception.message}", isLocationLoading = false) }
                        }
                } else {
                    _uiState.update { it.copy(locationError = "Gagal mendapatkan lokasi saat ini.", isLocationLoading = false) }
                }
            } catch (e: SecurityException) {
                _uiState.update { it.copy(locationError = "Izin lokasi ditolak.", isLocationLoading = false) }
            } catch (e: Exception) {
                _uiState.update { it.copy(locationError = "Terjadi error: ${e.message}", isLocationLoading = false) }
            }
        }
    }
    @Suppress("DEPRECATION")
    private fun getCityNameFromLatLng(latLng: LatLng): String? {
        return try {
            val geocoder = Geocoder(context, Locale.getDefault())
            val addresses = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1)

            addresses?.firstOrNull()?.let { address ->

                val subAdminArea = address.subAdminArea
                val locality = address.locality
                val adminArea = address.adminArea
                val finalCityName = subAdminArea
                    ?: if (locality != null && locality.startsWith("Kecamatan", ignoreCase = true)) {
                        locality.substringAfter("Kecamatan").trim()
                    } else {
                        locality
                    }
                    ?: adminArea

                Log.d("Location", "Geocoder memilih lokasi: $finalCityName (setelah dibersihkan)")

                return finalCityName

            }
        } catch (e: Exception) {
            Log.e("Location", "Geocoding failed", e)
            null
        }
    }

    private fun loadHistoryRecord(id: String) {
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

    private suspend fun loadImageAsByteArray(url: String): ByteArray? {
        if (url.isBlank()) return null
        return try {
            val request = ImageRequest.Builder(context)
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
                analysis
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
                } else {
                    throw Exception("Gagal mengunggah gambar")
                }

            } catch (e: Exception) {
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

        // Tetapkan batas skor maksimum berdasarkan justifikasi bobot hibrida kita
        // (3+2+2+1) + (4*5) + (3+4+3+1) = 8 + 20 + 11 = 39
        val MAX_QUESTIONNAIRE_SCORE = 39f

        // --- 1. Hitung Skor Kuesioner (Bobot Baru) ---

        // Loop 1: Gejala Fisik dan Riwayat Medis (dari 'symptomsAndHistory')
        userProfile.symptomsAndHistory.forEach { (question, answer) ->
            if (answer) { // Hanya jika jawabannya "Ya" / true
                when {
                    // --- Gejala Fisik (Bobot 4 per gejala) ---
                    question.contains("lelah") -> {
                        questionnaireScore += 4
                        questionnaireRiskFactors.add("Sering merasa mudah lelah")
                    }
                    question.contains("luka") -> {
                        questionnaireScore += 4
                        questionnaireRiskFactors.add("Luka lama sembuh")
                    }
                    question.contains("kabur") -> {
                        questionnaireScore += 4
                        questionnaireRiskFactors.add("Penglihatan kabur")
                    }
                    question.contains("kesemutan") -> {
                        questionnaireScore += 4
                        questionnaireRiskFactors.add("Sering kesemutan")
                    }
                    question.contains("haus") -> {
                        questionnaireScore += 4
                        questionnaireRiskFactors.add("Sering merasa haus berlebihan")
                    }

                    // --- Riwayat Medis (Bobot 3, 2, 2, 1) ---
                    question.contains("diabetes") -> {
                        questionnaireScore += 3
                        questionnaireRiskFactors.add("Riwayat diabetes dalam keluarga")
                    }
                    question.contains("tekanan darah tinggi") -> {
                        questionnaireScore += 2
                        questionnaireRiskFactors.add("Riwayat tekanan darah tinggi")
                    }
                    question.contains("kolesterol tinggi atau obesitas") -> {
                        questionnaireScore += 2
                        questionnaireRiskFactors.add("Riwayat kolesterol tinggi/obesitas")
                    }
                    question.contains("obat tertentu") -> {
                        questionnaireScore += 1
                        questionnaireRiskFactors.add("Konsumsi obat tertentu (steroid, dll)")
                    }
                }
            }
        }

        // Loop 2: Gaya Hidup (dari 'lifestyle')
        userProfile.lifestyle.forEach { (question, answer) ->
            when {
                // Bobot 3 (Sesuai FINDRISC)
                question.contains("aktivitas fisik") && answer == "Jarang" -> {
                    questionnaireScore += 3
                    questionnaireRiskFactors.add("Jarang melakukan aktivitas fisik")
                }
                // Bobot 4 (Diperberat)
                question.contains("gula") && (answer == "Sering" || answer == "Setiap Hari") -> {
                    questionnaireScore += 4
                    questionnaireRiskFactors.add("Sering mengonsumsi makanan/minuman tinggi gula")
                }
                // Bobot 3 (Faktor risiko kuat)
                question.contains("merokok") && answer == "Ya" -> {
                    questionnaireScore += 3
                    questionnaireRiskFactors.add("Memiliki kebiasaan merokok atau minum alkohol")
                }
                // Bobot 1 (Faktor risiko minor)
                question.contains("tidur") && answer == "< 5 jam" -> {
                    questionnaireScore += 1
                    questionnaireRiskFactors.add("Waktu tidur rata-rata kurang dari 5 jam")
                }
            }
        }

        // --- 2. Normalisasi Skor Kuesioner ---
        // Normalisasi skor (misal 15 dari 39) menjadi persentase (0-100%)
        val questionnairePercentage = (questionnaireScore / MAX_QUESTIONNAIRE_SCORE) * 100f

        // --- 3. Gabungkan Risiko (Weighted Average) ---
        val originalScanPercent = scanResult.riskPercentage

        // Tetapkan Bobot: 60% dari Scan AI, 40% dari Kuesioner
        val SCAN_WEIGHT = 0.60f
        val QUESTIONNAIRE_WEIGHT = 0.40f

        var adjustedPercent = (originalScanPercent * SCAN_WEIGHT) + (questionnairePercentage * QUESTIONNAIRE_WEIGHT)

        adjustedPercent = min(adjustedPercent, 99.0f)

        // --- 4. Tentukan Level Risiko Baru ---
        val newRiskLevel = when {
            adjustedPercent >= 70f -> "Tinggi"
            adjustedPercent >= 50f -> "Sedang"
            else -> "Rendah"
        }

        val combinedRiskFactors = (scanResult.riskFactors + questionnaireRiskFactors).distinct()

        return AnalysisResponse(
            riskLevel = newRiskLevel,
            riskPercentage = adjustedPercent,
            riskFactors = combinedRiskFactors
        )
    }
}