package com.example.prediai.presentation.labs

import android.content.Context
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.prediai.domain.model.AnalysisItem
import com.example.prediai.domain.model.AnalysisType
import com.example.prediai.domain.model.LabAnalysisRecord
import com.example.prediai.domain.model.LabAnalysisResult
import com.example.prediai.domain.repository.HistoryRepository
import com.example.prediai.presentation.labs.common.getFileName
import com.google.ai.client.generativeai.GenerativeModel
import com.google.ai.client.generativeai.type.content
import com.tom_roush.pdfbox.pdmodel.PDDocument
import com.tom_roush.pdfbox.text.PDFTextStripper
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import java.io.InputStream
import java.text.SimpleDateFormat
import java.util.Locale
import javax.inject.Inject

data class LabsUiState(
    val isLoading: Boolean = false,
    val selectedFileUri: Uri? = null,
    val selectedFileName: String? = null,
    val analysisResult: LabAnalysisResult? = null,
    val errorMessage: String? = null,
    val analysisComplete: Boolean = false,
    val recentAnalyses: List<AnalysisItem> = emptyList()
)

@HiltViewModel
class LabsViewModel @Inject constructor(
    private val generativeModel: GenerativeModel,
    private val historyRepository: HistoryRepository,
    @ApplicationContext private val context: Context
) : ViewModel() {

    private val _uiState = MutableStateFlow(LabsUiState())
    val uiState = _uiState.asStateFlow()

    init {
        loadRecentAnalyses()
    }

    private fun loadRecentAnalyses() {
        viewModelScope.launch {
            historyRepository.getLabAnalysisHistory().collect { result ->
                result.onSuccess { records ->
                    val items = records.take(5).map { mapRecordToAnalysisItem(it) }
                    _uiState.update { it.copy(recentAnalyses = items) }
                }.onFailure { error ->
                    Log.e("LabsViewModel", "Gagal memuat riwayat analisis lab", error)
                }
            }
        }
    }

    fun viewHistoricalAnalysis(recordId: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null, analysisComplete = false) }
            try {
                // Ambil satu record spesifik dari repository
                val recordResult = historyRepository.getLabAnalysisRecordById(recordId)
                val record = recordResult.getOrThrow()

                if (record?.analysisResult != null) {
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            selectedFileName = record.fileName, // Update nama file untuk ditampilkan di result screen
                            analysisResult = record.analysisResult,
                            analysisComplete = true // Pemicu navigasi
                        )
                    }
                } else {
                    throw Exception("Data riwayat tidak ditemukan.")
                }
            } catch (e: Exception) {
                _uiState.update { it.copy(isLoading = false, errorMessage = e.message) }
            }
        }
    }

    fun onFileSelected(uri: Uri?) {
        if (uri != null) {
            val fileName = getFileName(uri, context)
            _uiState.update { it.copy(selectedFileUri = uri, selectedFileName = fileName, errorMessage = null) }
        }
    }

    fun clearFileSelection() {
        _uiState.update { it.copy(selectedFileUri = null, selectedFileName = null, errorMessage = null) }
    }

    fun startAnalysis() {
        _uiState.value.selectedFileUri?.let { uri ->
            viewModelScope.launch {
                _uiState.update { it.copy(isLoading = true, errorMessage = null, analysisComplete = false) }
                try {
                    val mimeType = context.contentResolver.getType(uri)
                    val extractedContent: Any? = when {
                        mimeType?.startsWith("image/") == true -> uriToBitmap(uri)
                        mimeType == "application/pdf" -> extractTextFromPdf(uri)
                        mimeType == "text/plain" -> extractTextFromTxt(uri)
                        else -> throw Exception("Tipe file tidak didukung: $mimeType")
                    }

                    val fileBytes = context.contentResolver.openInputStream(uri)?.readBytes()
                        ?: throw Exception("Tidak bisa membaca file.")
                    val fileUrlResult = historyRepository.uploadImage(fileBytes)
                    val fileUrl = fileUrlResult.getOrThrow()

                    val analysisResult = generateExplanation(extractedContent)

                    val record = LabAnalysisRecord(
                        fileName = _uiState.value.selectedFileName ?: "N/A",
                        fileUrl = fileUrl,
                        analysisResult = analysisResult
                    )
                    historyRepository.saveLabAnalysisRecord(record).getOrThrow()

                    _uiState.update { it.copy(isLoading = false, analysisResult = analysisResult, analysisComplete = true) }
                } catch (e: Exception) {
                    _uiState.update { it.copy(isLoading = false, errorMessage = "Error: ${e.message}") }
                }
            }
        }
    }

    private suspend fun generateExplanation(content: Any?): LabAnalysisResult {
        val prompt = """
            Peran: Anda adalah seorang asisten medis AI yang ahli dalam menerjemahkan hasil lab untuk pasien awam.
            Konteks: Berikut adalah data dari dokumen hasil pemeriksaan laboratorium seorang pasien. Datanya bisa berupa teks atau gambar.
            ---
            [DATA PASIEN DI BAWAH INI]
            ---
            Tugas:
            1. Analisis data di atas. Identifikasi 3-5 parameter kunci (contoh: Glukosa Puasa, Kolesterol, HbA1c, dll.).
            2. Untuk setiap parameter kunci, sebutkan nilainya, satuannya, dan bandingkan dengan rentang nilai normal umum. Tentukan statusnya (Normal/Tinggi/Rendah).
            3. Buat sebuah ringkasan umum (summary) yang menjelaskan kondisi pasien dalam 2-3 kalimat sederhana.
            4. Berikan rekomendasi langkah selanjutnya (next_steps) yang bersifat umum.
            Format Output:
            Berikan jawaban HANYA dalam format JSON yang valid dan bersih tanpa markdown ```json. Strukturnya harus seperti berikut:
            {
              "summary": "Penjelasan singkat kondisi pasien...",
              "key_findings": [
                {
                  "parameter": "Nama Parameter",
                  "value": "Nilai Pasien",
                  "normal_range": "Rentang Normal",
                  "status": "Normal/Tinggi/Rendah",
                  "explanation": "Penjelasan singkat arti dari parameter ini."
                }
              ],
              "next_steps": "Rekomendasi umum seperti konsultasi ke dokter..."
            }
            PENTING: Jangan memberikan diagnosis medis. Selalu sarankan untuk berkonsultasi dengan dokter.
        """.trimIndent()

        val response = withContext(Dispatchers.IO) {
            when (content) {
                is String -> {
                    val fullPrompt = prompt.replace("[DATA PASIEN DI BAWAH INI]", content)
                    generativeModel.generateContent(fullPrompt)
                }
                is Bitmap -> {
                    val inputContent = content {
                        image(content)
                        text(prompt)
                    }
                    generativeModel.generateContent(inputContent)
                }
                else -> throw Exception("Tipe konten tidak valid untuk dikirim ke AI.")
            }
        }

        val jsonText = response.text ?: throw Exception("AI tidak memberikan respon teks.")
        return try {
            val json = Json { ignoreUnknownKeys = true; isLenient = true }
            val cleanedJson = jsonText.removeSurrounding("```json\n", "\n```").trim()
            json.decodeFromString<LabAnalysisResult>(cleanedJson)
        } catch (e: Exception) {
            Log.e("LabsViewModel", "JSON Parsing Error: ${e.message}, Response: $jsonText")
            throw Exception("Gagal memproses respon AI.")
        }
    }

    fun onNavigationComplete() {
        _uiState.update { it.copy(analysisComplete = false) }
        clearFileSelection()
    }

    private fun mapRecordToAnalysisItem(record: LabAnalysisRecord): AnalysisItem {
        val fileType = when {
            record.fileName.endsWith(".pdf", true) -> AnalysisType.PDF
            else -> AnalysisType.IMAGE
        }
        val date = record.timestamp?.let {
            SimpleDateFormat("dd MMMM yyyy", Locale.forLanguageTag("id-ID")).format(it)
        } ?: "N/A"
        return AnalysisItem(
            record.id,
            record.fileName,
            date,
            fileType,
            true
        )
    }

    // --- Helper Functions ---

    private suspend fun extractTextFromPdf(uri: Uri): String = withContext(Dispatchers.IO) {
        var document: PDDocument? = null
        var inputStream: InputStream? = null
        try {
            inputStream = context.contentResolver.openInputStream(uri)
            document = PDDocument.load(inputStream)
            PDFTextStripper().getText(document)
        } finally {
            document?.close()
            inputStream?.close()
        }
    }

    private suspend fun extractTextFromTxt(uri: Uri): String = withContext(Dispatchers.IO) {
        context.contentResolver.openInputStream(uri)?.bufferedReader().use { it?.readText() } ?: ""
    }

    private fun uriToBitmap(uri: Uri): Bitmap {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            ImageDecoder.decodeBitmap(ImageDecoder.createSource(context.contentResolver, uri))
        } else {
            MediaStore.Images.Media.getBitmap(context.contentResolver, uri)
        }
    }
}