package com.example.prediai.domain.model

import java.util.*

data class Message(
    val id: String = UUID.randomUUID().toString(),
    val text: String,
    val isFromUser: Boolean,
    val timestamp: String,
    val messageType: MessageType = MessageType.TEXT
)

enum class MessageType {
    TEXT, QUICK_REPLY, TYPING
}

data class QuickReply(
    val text: String,
    val icon: String // Use string identifier for icon to keep domain layer framework-agnostic
)