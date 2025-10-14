package com.example.prediai.presentation.main.chatbot

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.prediai.domain.model.Message
import com.example.prediai.presentation.main.chatbot.comps.ChatInputSection
import com.example.prediai.presentation.main.chatbot.comps.ChatTopBar
import com.example.prediai.presentation.main.chatbot.comps.MessageList
import com.example.prediai.presentation.main.chatbot.comps.QuickRepliesSection
import com.example.prediai.presentation.theme.PrediAITheme
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

// BARU: Statefull Composable (yang berhubungan dengan ViewModel)
@Composable
fun ChatbotRoute(
    navController: NavController,
    viewModel: ChatbotViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    ChatbotScreen(
        navController = navController,
        uiState = uiState,
        onMessageChange = viewModel::updateMessageText,
        onSendMessage = viewModel::sendMessage,
        onDismissError = viewModel::clearErrorMessage
    )
}

// DIUBAH: Stateless Composable (lebih mudah untuk di-preview dan diuji)
@Composable
fun ChatbotScreen(
    navController: NavController,
    uiState: ChatbotUiState,
    onMessageChange: (String) -> Unit,
    onSendMessage: (String) -> Unit,
    onDismissError: () -> Unit
) {
    val listState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()
    val keyboardController = LocalSoftwareKeyboardController.current

    LaunchedEffect(uiState.messages.size) {
        if (uiState.messages.isNotEmpty()) {
            coroutineScope.launch {
                delay(100) // Beri waktu agar item baru dirender
                listState.animateScrollToItem(uiState.messages.lastIndex)
            }
        }
    }

    Scaffold(
        topBar = {
            ChatTopBar(navController = navController)
        },
        // DIUBAH: bottomBar HANYA untuk input field
        bottomBar = {
            ChatInputSection(
                messageText = uiState.messageText,
                onMessageChange = onMessageChange,
                onSendMessage = {
                    onSendMessage(uiState.messageText)
                    keyboardController?.hide()
                },
                isLoading = uiState.isTyping,
                errorMessage = uiState.errorMessage,
                onDismissError = onDismissError
            )
        }
    ) { paddingValues ->
        // DIUBAH: Konten utama menggunakan Column
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFF8F9FA))
                .padding(paddingValues)
                .imePadding() // Agar konten tidak tertutup keyboard
        ) {
            MessageList(
                messages = uiState.messages,
                isTyping = uiState.isTyping,
                modifier = Modifier.weight(1f), // Agar list mengisi ruang yang tersedia
                listState = listState
            )

            // DIUBAH: QuickReplies dipindah ke sini
            if (uiState.messages.size == 1 && !uiState.isTyping) {
                QuickRepliesSection(
                    quickReplies = uiState.quickReplies,
                    onQuickReplyClick = { reply -> onSendMessage(reply.text) }
                )
            }
        }
    }
}

// BARU: Menambahkan Preview untuk melihat tampilan tanpa menjalankan aplikasi
@Preview(showBackground = true)
@Composable
fun ChatbotScreenPreview_Initial() {
    PrediAITheme {
        ChatbotScreen(
            navController = rememberNavController(),
            uiState = ChatbotUiState(
                messages = listOf(
                    Message(
                        text = "Halo! Saya AI Assistant PrediAI. Ada yang bisa saya bantu terkait kesehatan gula darah Anda?",
                        isFromUser = false,
                        timestamp = "08:30"
                    )
                )
            ),
            onMessageChange = {},
            onSendMessage = {},
            onDismissError = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
fun ChatbotScreenPreview_Conversation() {
    PrediAITheme {
        ChatbotScreen(
            navController = rememberNavController(),
            uiState = ChatbotUiState(
                messages = listOf(
                    // DIUBAH: Gunakan argumen bernama (text=, isFromUser=, timestamp=)
                    Message(
                        text = "Halo!",
                        isFromUser = false,
                        timestamp = "08:30"
                    ),
                    Message(
                        text = "Apa saja gejala diabetes?",
                        isFromUser = true,
                        timestamp = "08:31"
                    ),
                    Message(
                        text = "Tentu, gejala umum diabetes meliputi sering merasa haus, sering buang air kecil, penurunan berat badan tanpa sebab yang jelas, dan mudah lelah.",
                        isFromUser = false,
                        timestamp = "08:32"
                    ),
                ),
                isTyping = true,
                errorMessage = "Gagal terhubung ke server."
            ),
            onMessageChange = {},
            onSendMessage = {},
            onDismissError = {}
        )
    }
}