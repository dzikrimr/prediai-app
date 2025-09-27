package com.example.prediai.presentation.chatbot

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.prediai.domain.model.Message
import com.example.prediai.domain.model.QuickReply
import com.example.prediai.domain.usecase.GetInitialMessagesUseCase
import com.example.prediai.domain.usecase.SendMessageUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChatbotViewModel @Inject constructor(
    private val sendMessageUseCase: SendMessageUseCase,
    private val getInitialMessagesUseCase: GetInitialMessagesUseCase
) : ViewModel() {
    var messages by mutableStateOf<List<Message>>(emptyList())
        private set
    var messageText by mutableStateOf("")
        private set
    var isTyping by mutableStateOf(false)
        private set
    var errorMessage by mutableStateOf<String?>(null)
        private set
    private var lastSendTime by mutableStateOf(0L)

    val quickReplies = listOf(
        QuickReply("Apa saja gejala awal diabetes?", "help"),
        QuickReply("Bagaimana cara mengontrol gula darah?", "trending_up")
    )

    init {
        viewModelScope.launch {
            messages = getInitialMessagesUseCase()
        }
    }

    fun sendMessage(text: String) {
        val currentTime = System.currentTimeMillis()
        if (text.isNotBlank() && !isTyping && (currentTime - lastSendTime) > 1000) {
            lastSendTime = currentTime
            val userMessage = Message(text = text, isFromUser = true, timestamp = getCurrentTime())
            messages = messages + userMessage
            messageText = ""
            isTyping = true
            errorMessage = null

            viewModelScope.launch {
                sendMessageUseCase(text).fold(
                    onSuccess = { botResponse ->
                        messages = messages + botResponse
                        isTyping = false
                    },
                    onFailure = { exception ->
                        isTyping = false
                        val errorDetail = when {
                            exception.message?.contains("API") == true -> "Terjadi masalah dengan koneksi API"
                            exception.message?.contains("model") == true -> "Model AI tidak tersedia"
                            exception.message?.contains("quota") == true -> "Kuota API habis"
                            exception.message?.contains("network") == true -> "Masalah jaringan"
                            else -> "Gangguan koneksi"
                        }
                        errorMessage = errorDetail
                        messages = messages + Message(
                            text = "Maaf, terjadi gangguan sementara. Silakan coba lagi.",
                            isFromUser = false,
                            timestamp = getCurrentTime()
                        )
                    }
                )
            }
        }
    }

    fun updateMessageText(newText: String) {
        messageText = newText
    }

    fun clearErrorMessage() {
        errorMessage = null
    }

    private fun getCurrentTime(): String {
        val formatter = java.text.SimpleDateFormat("HH:mm", java.util.Locale.getDefault())
        return formatter.format(java.util.Date())
    }
}