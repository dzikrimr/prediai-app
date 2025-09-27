package com.example.prediai.domain.usecase

import com.example.prediai.domain.model.Message
import com.example.prediai.domain.repository.ChatRepository
import javax.inject.Inject

class GetInitialMessagesUseCase @Inject constructor(
    private val chatRepository: ChatRepository
) {
    suspend operator fun invoke(): List<Message> {
        return chatRepository.getInitialMessages()
    }
}