package com.example.prediai.data.repository

import com.example.prediai.domain.model.Message
import com.example.prediai.domain.model.MessageType
import com.example.prediai.domain.repository.ChatRepository
import com.google.ai.client.generativeai.GenerativeModel
import com.google.ai.client.generativeai.type.content
import javax.inject.Inject

class ChatRepositoryImpl @Inject constructor(
    private val generativeModel: GenerativeModel
) : ChatRepository {
    private val chat = generativeModel.startChat(
        history = listOf(
            content(role = "user") {
                text("Kamu adalah AI Assistant PrediAI yang ahli dalam bidang diabetes, nutrisi, dan kesehatan. Berikan jawaban yang akurat, informatif, dan mudah dipahami dalam bahasa Indonesia. Fokus pada informasi medis yang dapat dipercaya dan selalu sarankan untuk konsultasi dengan dokter untuk diagnosis yang akurat. Jawab dengan singkat namun informatif, maksimal 3-4 kalimat per respons.")
            },
            content(role = "model") {
                text("Halo! Saya AI Assistant PrediAI. Saya siap membantu Anda dengan pertanyaan seputar diabetes, nutrisi, dan kesehatan. Ada yang ingin Anda tanyakan?")
            }
        )
    )

    override suspend fun sendMessage(message: String): Result<String> {
        return try {
            val response = chat.sendMessage(message)
            val responseText = response.text
            if (responseText.isNullOrBlank()) {
                Result.failure(Exception("Empty response from AI"))
            } else {
                Result.success(responseText)
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getInitialMessages(): List<Message> {
        return listOf(
            Message(
                text = "Halo! Saya AI Assistant PrediAI. Saya siap membantu Anda dengan pertanyaan seputar diabetes, nutrisi, dan kesehatan. Ada yang ingin Anda tanyakan?",
                isFromUser = false,
                timestamp = getCurrentTime(),
                messageType = MessageType.TEXT
            )
        )
    }

    private fun getCurrentTime(): String {
        val formatter = java.text.SimpleDateFormat("HH:mm", java.util.Locale.getDefault())
        return formatter.format(java.util.Date())
    }
}