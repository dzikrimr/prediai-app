package com.example.prediai.presentation.main.chatbot

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.prediai.domain.model.Message
import com.example.prediai.domain.model.QuickReply
import com.example.prediai.domain.usecase.GetInitialMessagesUseCase
import com.example.prediai.domain.usecase.SendMessageUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.inject.Inject

// BARU: Menyatukan semua state ke dalam satu data class
data class ChatbotUiState(
    val messages: List<Message> = emptyList(),
    val messageText: String = "",
    val isTyping: Boolean = false,
    val errorMessage: String? = null,
    val quickReplies: List<QuickReply> = listOf(
        QuickReply("Apa saja gejala awal diabetes?", "help"),
        QuickReply("Bagaimana cara mengontrol gula darah?", "trending_up")
    )
)

@HiltViewModel
class ChatbotViewModel @Inject constructor(
    private val sendMessageUseCase: SendMessageUseCase,
    private val getInitialMessagesUseCase: GetInitialMessagesUseCase
) : ViewModel() {

    // DIUBAH: Menggunakan satu StateFlow untuk UiState
    private val _uiState = MutableStateFlow(ChatbotUiState())
    val uiState = _uiState.asStateFlow()

    private var lastSendTime = 0L

    init {
        loadInitialMessages()
    }

    private fun loadInitialMessages() {
        viewModelScope.launch {
            val initialMessages = getInitialMessagesUseCase()
            _uiState.update { it.copy(messages = initialMessages) }
        }
    }

    fun sendMessage(text: String) {
        val currentTime = System.currentTimeMillis()
        val currentState = _uiState.value
        if (text.isNotBlank() && !currentState.isTyping && (currentTime - lastSendTime) > 1000) {
            lastSendTime = currentTime
            val userMessage = Message(text = text, isFromUser = true, timestamp = getCurrentTime())

            _uiState.update {
                it.copy(
                    messages = it.messages + userMessage,
                    messageText = "",
                    isTyping = true,
                    errorMessage = null
                )
            }

            viewModelScope.launch {
                sendMessageUseCase(text).fold(
                    onSuccess = { botResponse ->
                        _uiState.update { it.copy(messages = it.messages + botResponse, isTyping = false) }
                    },
                    onFailure = { exception ->
                        val errorDetail = when {
                            exception.message?.contains("API") == true -> "Terjadi masalah dengan koneksi API"
                            else -> "Gangguan koneksi, silakan coba lagi."
                        }
                        val errorBotMessage = Message(
                            text = "Maaf, terjadi gangguan sementara. Silakan coba lagi.",
                            isFromUser = false,
                            timestamp = getCurrentTime()
                        )
                        _uiState.update {
                            it.copy(
                                isTyping = false,
                                errorMessage = errorDetail,
                                messages = it.messages + errorBotMessage
                            )
                        }
                    }
                )
            }
        }
    }

    fun updateMessageText(newText: String) {
        _uiState.update { it.copy(messageText = newText) }
    }

    fun clearErrorMessage() {
        _uiState.update { it.copy(errorMessage = null) }
    }

    private fun getCurrentTime(): String {
        val formatter = SimpleDateFormat("HH:mm", Locale.getDefault())
        return formatter.format(Date())
    }
}