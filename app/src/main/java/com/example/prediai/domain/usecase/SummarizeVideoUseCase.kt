package com.example.prediai.domain.usecase

import com.google.ai.client.generativeai.GenerativeModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

/**
 * Use case untuk meminta rangkuman video dari Gemini berdasarkan transkrip.
 */
class SummarizeVideoUseCase @Inject constructor(
    // Inject GenerativeModel (disediakan oleh Hilt dari Modul di bawah)
    private val generativeModel: GenerativeModel
) {
    operator fun invoke(transcript: String): Flow<Result<String>> = flow {
        // --- VALIDASI INPUT ---
        if (transcript.isBlank() || transcript.length < 50) { // Cek jika transkrip terlalu pendek
            emit(Result.failure(IllegalArgumentException("Transkrip terlalu pendek atau kosong.")))
            return@flow
        }

        try {
            val prompt = """
                Berikut adalah transkrip dari sebuah video edukasi kesehatan, kemungkinan tentang diabetes. 
                Buatlah rangkuman singkat (sekitar 3-5 kalimat) dalam Bahasa Indonesia yang mudah dipahami 
                mengenai poin-poin utama video tersebut. Fokus pada informasi yang paling relevan untuk pasien atau orang awam.

                Transkrip:
                $transcript
            """.trimIndent()

            // Panggil API Gemini
            val response = generativeModel.generateContent(prompt)

            response.text?.let { summary ->
                emit(Result.success(summary.trim())) // Trim hasil rangkuman
            } ?: emit(Result.failure(Exception("Gemini tidak memberikan respons teks.")))

        } catch (e: Exception) {
            // Tangkap semua jenis error (jaringan, API key salah, dll.)
            emit(Result.failure(Exception("Gagal menghubungi AI untuk rangkuman: ${e.localizedMessage}", e)))
        }
    }.flowOn(Dispatchers.IO) // Pastikan berjalan di background thread
}