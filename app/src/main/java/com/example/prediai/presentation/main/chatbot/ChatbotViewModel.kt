package com.example.prediai.presentation.main.chatbot

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.prediai.R
// Import UseCase dari domain layer Anda
import com.example.prediai.domain.usecase.GetInitialMessagesUseCase
import com.example.prediai.domain.usecase.SendMessageUseCase
// Import model domain untuk mapping
import com.example.prediai.domain.model.Message as DomainMessage
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.inject.Inject

// --- Model Data khusus untuk UI ---
// Model-model ini tetap ada di sini karena spesifik untuk tampilan (presentation layer)

data class ChatFilter(val label: String, val icon: Int)

// Ini adalah model 'ChatMessage' untuk UI
data class ChatMessage(val text: String, val isFromUser: Boolean, val timestamp: String)

data class QuickReply(val text: String, val icon: Int)

// State untuk seluruh UI
data class ChatbotUiState(
    val filters: List<ChatFilter> = listOf(
        ChatFilter("Konsultasi Diabetes", R.drawable.ic_health_consultation),
        ChatFilter("Tips Nutrisi", R.drawable.ic_nutrition_tips),
        ChatFilter("Aktivitas Fisik", R.drawable.ic_physical_activity)
    ),
    // DIUBAH: Mulai dengan list kosong, akan diisi oleh UseCase
    val messages: List<ChatMessage> = emptyList(),
    val quickReplies: List<QuickReply> = listOf(
        QuickReply("Apa saja gejala awal diabetes?", R.drawable.ic_help_question),
        QuickReply("Makanan apa yang sebaiknya dihindari?", R.drawable.ic_food_avoid),
        QuickReply("Bagaimana cara mengontrol gula darah?", R.drawable.ic_blood_sugar_control)
    ),
    val inputText: String = ""
)

@HiltViewModel
class ChatbotViewModel @Inject constructor(
    // 1. Inject UseCase yang sudah Anda buat
    private val sendMessageUseCase: SendMessageUseCase,
    private val getInitialMessagesUseCase: GetInitialMessagesUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(ChatbotUiState())
    val uiState = _uiState.asStateFlow()

    // 2. Panggil UseCase untuk memuat pesan awal saat ViewModel dibuat
    init {
        loadInitialMessages()
    }

    private fun loadInitialMessages() {
        viewModelScope.launch {
            // Panggil use case
            val initialDomainMessages = getInitialMessagesUseCase()
            // Map dari domain.Message ke ui.ChatMessage
            val initialUiMessages = initialDomainMessages.map { it.toUiChatMessage() }
            _uiState.update { it.copy(messages = initialUiMessages) }
        }
    }

    fun onInputTextChanged(newText: String) {
        _uiState.update { it.copy(inputText = newText) }
    }

    // 3. Implementasi fungsi sendMessage
    fun sendMessage(text: String) {
        if (text.isBlank()) return

        // Buat pesan pengguna (versi UI)
        val userMessage = ChatMessage(
            text = text,
            isFromUser = true,
            timestamp = getCurrentTimestamp()
        )

        // Langsung tambahkan pesan pengguna ke UI dan kosongkan input
        _uiState.update {
            it.copy(
                messages = it.messages + userMessage,
                inputText = ""
            )
        }

        // Tampilkan indikator loading "..."
        val loadingMessage = ChatMessage(
            text = "...",
            isFromUser = false,
            timestamp = getCurrentTimestamp()
        )
        _uiState.update {
            it.copy(messages = it.messages + loadingMessage)
        }

        // Jalankan coroutine untuk mendapatkan balasan AI
        viewModelScope.launch {
            // Panggil SendMessageUseCase
            sendMessageUseCase(text)
                .onSuccess { aiDomainMessage ->
                    // Jika sukses, map domain.Message ke ui.ChatMessage
                    val aiUiMessage = aiDomainMessage.toUiChatMessage()
                    // Ganti "..." dengan balasan AI
                    _uiState.update { state ->
                        val currentMessages = state.messages.dropLast(1) // Hapus "..."
                        state.copy(messages = currentMessages + aiUiMessage)
                    }
                }
                .onFailure { error ->
                    // Jika gagal, ganti "..." dengan pesan error
                    val errorMessage = ChatMessage(
                        text = "Maaf, terjadi kesalahan: ${error.message}",
                        isFromUser = false,
                        timestamp = getCurrentTimestamp()
                    )
                    _uiState.update { state ->
                        val currentMessages = state.messages.dropLast(1) // Hapus "..."
                        state.copy(messages = currentMessages + errorMessage)
                    }
                }
        }
    }

    // --- Helper Functions ---

    private fun getCurrentTimestamp(): String {
        return try {
            val sdf = SimpleDateFormat("HH:mm", Locale.getDefault())
            sdf.format(Date())
        } catch (e: Exception) {
            "..."
        }
    }

    // Fungsi mapper untuk mengubah model Domain -> model UI
    private fun DomainMessage.toUiChatMessage(): ChatMessage {
        return ChatMessage(
            text = this.text,
            isFromUser = this.isFromUser,
            timestamp = this.timestamp
            // Properti 'messageType' dari domain model diabaikan di UI model,
            // karena 'ChatMessage' (UI) tidak memerlukannya.
        )
    }
}