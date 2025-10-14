package com.example.prediai.domain.repository

interface ChatRepository {
    suspend fun sendMessage(message: String): Result<String>
    suspend fun getInitialMessages(): List<com.example.prediai.domain.model.Message>
}