package com.example.prediai.presentation.scan

import android.graphics.Bitmap
import android.util.Log
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.prediai.data.remote.scan.AnalysisResponse
import com.example.prediai.data.remote.scan.RetrofitClient
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.ByteArrayOutputStream

data class ScanUiState(
    val currentStep: Int = 1, // 1: Kuku, 2: Lidah
    val statusMessage: String = "Posisikan kuku Anda",
    val isDetectionSuccessful: Boolean = false,
    val cameraLens: Int = CameraSelector.LENS_FACING_BACK,
    val isLoading: Boolean = false,
    // State baru untuk hasil analisis
    val analysisResult: AnalysisResponse? = null,
    val analysisError: String? = null
)

class ScanViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(ScanUiState())
    val uiState = _uiState.asStateFlow()

    private var lastAnalyzedTimestamp = 0L

    // Variabel untuk menyimpan gambar yang berhasil dideteksi
    var capturedNailImage: ByteArray? = null
    var capturedTongueImage: ByteArray? = null


    val imageAnalyzer = ImageAnalysis.Analyzer { imageProxy ->
        val currentTime = System.currentTimeMillis()
        if (currentTime - lastAnalyzedTimestamp >= 1500 && !_uiState.value.isLoading) {
            val bitmap = imageProxy.toBitmap()
            if (bitmap != null) {
                detectObject(bitmap, imageProxy.toJpegByteArray())
            }
            lastAnalyzedTimestamp = currentTime
        }
        imageProxy.close()
    }

    private fun detectObject(bitmap: Bitmap, imageBytes: ByteArray) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, isDetectionSuccessful = false) }

            val requestBody = imageBytes.toRequestBody("image/jpeg".toMediaTypeOrNull())
            val part = MultipartBody.Part.createFormData("file", "image.jpg", requestBody)

            try {
                val currentStep = _uiState.value.currentStep
                val response = if (currentStep == 1)
                    RetrofitClient.detectionInstance.detectKuku(part)
                else
                    RetrofitClient.detectionInstance.detectLidah(part)

                if (response.isSuccessful && response.body() != null) {
                    val body = response.body()!!
                    val detection = body.detection

                    _uiState.update {
                        it.copy(
                            isDetectionSuccessful = detection != null,
                            statusMessage = body.message,
                            isLoading = false
                        )
                    }

                    if (detection != null) {
                        lastAnalyzedTimestamp = Long.MAX_VALUE // Blokir frame
                        // Simpan gambar jika deteksi berhasil
                        if (currentStep == 1) {
                            capturedNailImage = imageBytes
                        } else {
                            capturedTongueImage = imageBytes
                        }
                    }
                } else {
                    _uiState.update { it.copy(statusMessage = "Gagal mendeteksi objek (${response.code()})", isLoading = false) }
                }
            } catch (e: Exception) {
                e.printStackTrace()
                _uiState.update { it.copy(statusMessage = "Terjadi kesalahan koneksi", isLoading = false) }
            }
        }
    }

    // Fungsi BARU untuk melakukan analisis
    fun performAnalysis() {
        if (capturedNailImage == null || capturedTongueImage == null) {
            _uiState.update { it.copy(analysisError = "Gambar kuku atau lidah belum diambil.") }
            return
        }

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, statusMessage = "Menganalisis hasil...") }

            try {
                val nailReqBody = capturedNailImage!!.toRequestBody("image/jpeg".toMediaTypeOrNull())
                val nailPart = MultipartBody.Part.createFormData("kuku_image", "kuku.jpg", nailReqBody)

                val tongueReqBody = capturedTongueImage!!.toRequestBody("image/jpeg".toMediaTypeOrNull())
                val tonguePart = MultipartBody.Part.createFormData("lidah_image", "lidah.jpg", tongueReqBody)

                val response = RetrofitClient.analysisInstance.analyzeImages(tonguePart, nailPart)

                if (response.isSuccessful && response.body() != null) {
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            analysisResult = response.body(),
                            analysisError = null
                        )
                    }
                } else {
                    val errorBody = response.errorBody()?.string() // Dapatkan detail error dari body
                    Log.e("ScanViewModel", "Analisis gagal: HTTP ${response.code()}, Error: $errorBody")
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            analysisError = "Gagal menganalisis gambar (${response.code()})",
                            statusMessage = "Analisis Gagal"
                        )
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        analysisError = "Koneksi ke server analisis gagal.",
                        statusMessage = "Analisis Gagal"
                    )
                }
            }
        }
    }

    fun resetDetection() {
        lastAnalyzedTimestamp = 0L // Mengaktifkan kembali analisis frame kamera
        _uiState.update {
            it.copy(
                isDetectionSuccessful = false,
                isLoading = false,
                statusMessage = if (it.currentStep == 1) "Posisikan kuku Anda" else "Posisikan lidah Anda"
            )
        }
    }

    fun flipCamera() {
        _uiState.update {
            val newLens = if (it.cameraLens == CameraSelector.LENS_FACING_BACK)
                CameraSelector.LENS_FACING_FRONT
            else
                CameraSelector.LENS_FACING_BACK
            it.copy(cameraLens = newLens)
        }
    }

    fun proceedToNextStep() {
        _uiState.update {
            it.copy(
                currentStep = 2,
                statusMessage = "Posisikan lidah Anda",
                cameraLens = CameraSelector.LENS_FACING_BACK, // Lidah lebih baik kamera depan
                isDetectionSuccessful = false // Reset status deteksi untuk step selanjutnya
            )
        }
        lastAnalyzedTimestamp = 0L
    }
}

fun ImageProxy.toJpegByteArray(): ByteArray {
    val bitmap = this.toBitmap()!!
    val stream = ByteArrayOutputStream()
    bitmap.compress(Bitmap.CompressFormat.JPEG, 90, stream)
    return stream.toByteArray()
}

// Helper untuk konversi ImageProxy ke Bitmap
fun ImageProxy.toBitmap(): Bitmap? {
    val planeProxy = planes[0]
    val buffer = planeProxy.buffer
    val bytes = ByteArray(buffer.remaining())
    buffer.get(bytes)
    return android.graphics.BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
}