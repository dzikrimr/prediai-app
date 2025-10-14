package com.example.prediai.domain.model

data class Message(
    val text: String,
    val isFromUser: Boolean,
    val timestamp: String,
    // TAMBAHKAN PROPERTI INI
    val messageType: MessageType = MessageType.TEXT // Beri nilai default
)