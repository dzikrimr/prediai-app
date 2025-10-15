package com.example.prediai.presentation.scan

import android.graphics.Bitmap
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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
    val isLoading: Boolean = false
)

class ScanViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(ScanUiState())
    val uiState = _uiState.asStateFlow()

    private var lastAnalyzedTimestamp = 0L

    // Analyzer untuk memproses frame kamera
    val imageAnalyzer = ImageAnalysis.Analyzer { imageProxy ->
        val currentTime = System.currentTimeMillis()
        // Throttle: Hanya proses satu frame setiap 1.5 detik
        if (currentTime - lastAnalyzedTimestamp >= 1500 && !_uiState.value.isLoading) {
            val bitmap = imageProxy.toBitmap()
            if (bitmap != null) {
                detectObject(bitmap)
            }
            lastAnalyzedTimestamp = currentTime
        }
        imageProxy.close()
    }

    private fun detectObject(bitmap: Bitmap) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }

            val stream = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.JPEG, 90, stream)
            val byteArray = stream.toByteArray()

            val requestBody = byteArray.toRequestBody("image/jpeg".toMediaTypeOrNull())
            val part = MultipartBody.Part.createFormData("file", "image.jpg", requestBody)

            try {
                val currentStep = _uiState.value.currentStep
                val response = if (currentStep == 1)
                    RetrofitClient.instance.detectKuku(part)
                else
                    RetrofitClient.instance.detectLidah(part)

                if (response.isSuccessful) {
                    val body = response.body()
                    if (body != null) {
                        val detection = body.detection
                        val message = body.message

                        _uiState.update {
                            it.copy(
                                isDetectionSuccessful = detection != null,
                                statusMessage = message,
                                isLoading = false
                            )
                        }

                        // Jika deteksi berhasil, hentikan analisis kamera (pause)
                        if (detection != null) {
                            lastAnalyzedTimestamp = Long.MAX_VALUE // blokir frame selanjutnya
                        }
                    }
                } else {
                    _uiState.update {
                        it.copy(
                            statusMessage = "Gagal mendeteksi objek (${response.code()})",
                            isDetectionSuccessful = false,
                            isLoading = false
                        )
                    }
                }

            } catch (e: Exception) {
                e.printStackTrace()
                _uiState.update {
                    it.copy(
                        statusMessage = "Terjadi kesalahan koneksi",
                        isDetectionSuccessful = false,
                        isLoading = false
                    )
                }
            }
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
            ScanUiState(
                currentStep = 2,
                statusMessage = "Posisikan lidah Anda",
                cameraLens = CameraSelector.LENS_FACING_BACK
            )
        }
        lastAnalyzedTimestamp = 0L // aktifkan kembali analisis frame
    }
}

// Helper untuk konversi ImageProxy ke Bitmap
fun ImageProxy.toBitmap(): Bitmap? {
    val planeProxy = planes[0]
    val buffer = planeProxy.buffer
    val bytes = ByteArray(buffer.remaining())
    buffer.get(bytes)
    return android.graphics.BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
}