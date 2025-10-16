package com.example.prediai.presentation.main.chatbot

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.prediai.presentation.common.TopBar
import com.example.prediai.presentation.main.chatbot.comps.*
import com.example.prediai.presentation.theme.PrediAITheme

@Composable
fun ChatbotScreen(
    navController: NavController,
    viewModel: ChatbotViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            TopBar(
                title = "Asisten Dokter AI",
                subtitle = "Online • Siap membantu",
                onBackClick = { navController.popBackStack() }
                // Anda mungkin perlu menambahkan parameter 'actions' ke TopBar Anda
                // untuk menampilkan ikon tiga titik (MoreVert)
            )
        },
        bottomBar = {
            ChatInputField(
                value = uiState.inputText,
                onValueChange = viewModel::onInputTextChanged,
                onSendClick = { viewModel.sendMessage(uiState.inputText) }
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentPadding = PaddingValues(vertical = 16.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            item {
                ChatbotFilterChips(filters = uiState.filters)
            }

            item {
                Column(
                    modifier = Modifier.padding(horizontal = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    uiState.messages.forEach { message ->
                        MessageBubble(message = message)
                    }
                }
            }

            item {
                QuickReplies(
                    replies = uiState.quickReplies,
                    onReplyClick = { replyText -> viewModel.sendMessage(replyText) }
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ChatbotScreenPreview() {
    PrediAITheme {
        ChatbotScreen(navController = rememberNavController())
    }
}