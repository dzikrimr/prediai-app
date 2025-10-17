package com.example.prediai.presentation.main.chatbot

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
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
                title = "Konsultasi AI",
                subtitle = "AI Consultation",
                onBackClick = { navController.popBackStack() }
            )
        },
        bottomBar = {
            ChatInputField(
                value = uiState.inputText,
                onValueChange = viewModel::onInputTextChanged,
                onSendClick = { viewModel.sendMessage(uiState.inputText) },
                modifier = Modifier
                    .imePadding()                // naik saat keyboard muncul
                    .navigationBarsPadding()     // aman dari gesture bar
            )
        },
        contentWindowInsets = WindowInsets(0, 0, 0, 0) // ⚡ Hindari padding ganda
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(bottom = 8.dp), // beri jarak sedikit dari input
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

                item {
                    DirectDoctorButton(
                        onClick = { navController.navigate("doctor") }
                    )
                }
            }
        }
    }
}

// BARU: Composable terpisah untuk tombol navigasi dokter
@Composable
private fun DirectDoctorButton(onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = rememberRipple(),
                onClick = onClick
            ),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFE8F8F7)),
        border = BorderStroke(1.dp, Color(0xFF00B4A3))
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Konsultasi dengan dokter langsung",
                modifier = Modifier.weight(1f),
                color = Color(0xFF00B4A3),
                fontWeight = FontWeight.SemiBold
            )
            Icon(
                imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                contentDescription = null,
                tint = Color(0xFF00B4A3)
            )
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