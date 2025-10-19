package com.example.prediai.presentation.main.education

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.prediai.data.remote.youtube.YoutubeApiService // <-- IMPORT BARU
import com.example.prediai.domain.model.EducationVideo
import com.example.prediai.domain.model.VideoCategories
import com.example.prediai.domain.repository.EducationRepository
import com.example.prediai.domain.usecase.SummarizeVideoUseCase // <-- IMPORT BARU
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.ResponseBody
import java.io.BufferedReader
import java.io.InputStreamReader
import javax.inject.Inject
import com.example.prediai.BuildConfig

data class EducationUiState(
    // State untuk List Screen
    val categories: List<String> = listOf(
        VideoCategories.SEMUA, VideoCategories.NUTRISI, VideoCategories.OLAHRAGA,
        VideoCategories.PERAWATAN, VideoCategories.GEJALA
    ),
    val selectedCategory: String = VideoCategories.SEMUA,
    val videos: List<EducationVideo> = emptyList(),
    val isLoading: Boolean = false,

    // State untuk Detail Screen
    val selectedVideo: EducationVideo? = null,
    val aiSummary: String? = null,
    val isSummaryLoading: Boolean = false,
    val summaryError: String? = null,
    val isTranscriptLoading: Boolean = false,
    val transcriptError: String? = null
)

@HiltViewModel
class EducationViewModel @Inject constructor(
    private val educationRepository: EducationRepository,
    // --- INJECT DEPENDENSI BARU ---
    private val youtubeApiService: YoutubeApiService,
    private val summarizeVideoUseCase: SummarizeVideoUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(EducationUiState())
    val uiState = _uiState.asStateFlow()

    init {
        // Muat video awal untuk list
        selectCategory(VideoCategories.SEMUA)
    }

    // --- Fungsi untuk List Screen (Tidak Berubah) ---
    fun selectCategory(category: String) {
        _uiState.update {
            it.copy(
                selectedCategory = category,
                isLoading = true,
                // Reset detail saat ganti kategori
                selectedVideo = null,
                aiSummary = null,
                summaryError = null,
                transcriptError = null
            )
        }
        educationRepository.getEducationVideos(category)
            .onEach { videos ->
                _uiState.update {
                    it.copy(videos = videos, isLoading = false)
                }
            }
            .launchIn(viewModelScope)
    }

    // --- FUNGSI BARU UNTUK DETAIL SCREEN ---

    /** Dipanggil saat user klik video di list */
    fun selectVideo(video: EducationVideo) {
        // Reset state summary sebelumnya sebelum memilih video baru
        _uiState.update {
            it.copy(
                selectedVideo = video,
                aiSummary = null,
                isSummaryLoading = false,
                summaryError = null,
                isTranscriptLoading = false,
                transcriptError = null
            )
        }
        // Langsung mulai proses ambil transkrip & summary
        fetchTranscriptAndSummarize()
    }

    /** Dipanggil saat user kembali dari detail screen */
    fun clearSelectedVideo() {
        _uiState.update {
            it.copy(
                selectedVideo = null,
                aiSummary = null,
                isSummaryLoading = false,
                summaryError = null,
                isTranscriptLoading = false,
                transcriptError = null
            )
        }
    }


    /** Fungsi utama untuk proses detail */
    fun fetchTranscriptAndSummarize() {
        val videoId = _uiState.value.selectedVideo?.youtubeVideoId ?: return // Keluar jika tidak ada video terpilih

        viewModelScope.launch {
            _uiState.update { it.copy(isTranscriptLoading = true, transcriptError = null, summaryError = null, aiSummary = null) }

            val transcriptResult = getTranscript(videoId)

            if (transcriptResult.isSuccess) {
                val transcript = transcriptResult.getOrNull()
                _uiState.update { it.copy(isTranscriptLoading = false) }

                if (!transcript.isNullOrBlank()) {
                    summarizeTranscript(transcript)
                } else {
                    _uiState.update { it.copy(transcriptError = "Transkrip tidak tersedia untuk video ini.") }
                }
            } else {
                _uiState.update {
                    it.copy(
                        isTranscriptLoading = false,
                        transcriptError = "Gagal mengambil transkrip: ${transcriptResult.exceptionOrNull()?.message}"
                    )
                }
            }
        }
    }

    /** Memanggil Gemini Use Case */
    private fun summarizeTranscript(transcript: String) {
        summarizeVideoUseCase(transcript)
            .onStart { _uiState.update { it.copy(isSummaryLoading = true, summaryError = null) } }
            .onEach { result ->
                if (result.isSuccess) {
                    _uiState.update { it.copy(aiSummary = result.getOrNull(), isSummaryLoading = false) }
                } else {
                    _uiState.update {
                        it.copy(
                            summaryError = "Gagal membuat rangkuman: ${result.exceptionOrNull()?.message}",
                            isSummaryLoading = false
                        )
                    }
                }
            }
            .launchIn(viewModelScope)
    }

    /** Mengambil Transkrip dari YouTube API */
    private suspend fun getTranscript(videoId: String): Result<String?> {
        return withContext(Dispatchers.IO) {
            try {
                val captionListResponse = youtubeApiService.listCaptions(videoId = videoId)
                val captionItem = captionListResponse.items?.firstOrNull {
                    it.snippet.language == "id" || it.snippet.language == "en"
                }

                if (captionItem == null) {
                    Result.success(null) // Tidak ada transkrip
                } else {
                    val captionId = captionItem.id
                    val responseBody = youtubeApiService.downloadCaption(captionId = captionId)
                    val transcriptText = parseSrt(responseBody)
                    Result.success(transcriptText)
                }
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }

    /** Helper parser SRT */
    private fun parseSrt(responseBody: ResponseBody): String {
        val reader = BufferedReader(InputStreamReader(responseBody.byteStream()))
        val transcriptBuilder = StringBuilder()
        var line: String?
        try {
            while (reader.readLine().also { line = it } != null) {
                if (line?.matches(Regex("^\\d+$")) == false &&
                    line?.contains("-->") == false &&
                    line?.isNotBlank() == true
                ) {
                    val cleanLine = line?.replace(Regex("<.*?>"), "")
                    transcriptBuilder.append(cleanLine).append(" ")
                }
            }
        } catch (e: Exception) { /* ignored */ }
        finally { reader.close() }
        return transcriptBuilder.toString().trim()
    }
}