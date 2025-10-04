package com.example.prediai.domain.usecase

import com.example.prediai.domain.model.Message
import com.example.prediai.domain.repository.ChatRepository
import javax.inject.Inject

class SendMessageUseCase @Inject constructor(
    private val chatRepository: ChatRepository
) {
    suspend operator fun invoke(message: String): Result<Message> {
        return chatRepository.sendMessage(message).map { responseText ->
            Message(
                text = responseText,
                isFromUser = false,
                timestamp = getCurrentTime()
            )
        }
    }

    private fun getCurrentTime(): String {
        val formatter = java.text.SimpleDateFormat("HH:mm", java.util.Locale.getDefault())
        return formatter.format(java.util.Date())
    }
}