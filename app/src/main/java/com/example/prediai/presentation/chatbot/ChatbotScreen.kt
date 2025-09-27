package com.example.prediai.presentation.chatbot

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.prediai.presentation.chatbot.comps.ChatInputSection
import com.example.prediai.presentation.chatbot.comps.ChatTopBar
import com.example.prediai.presentation.chatbot.comps.MessageList
import com.example.prediai.presentation.chatbot.comps.QuickRepliesSection
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatbotScreen(
    navController: NavController,
    viewModel: ChatbotViewModel = hiltViewModel()
) {
    val listState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()
    val keyboardController = LocalSoftwareKeyboardController.current

    LaunchedEffect(viewModel.messages) {
        if (viewModel.messages.isNotEmpty()) {
            coroutineScope.launch {
                try {
                    kotlinx.coroutines.delay(50)
                    listState.animateScrollToItem(viewModel.messages.size)
                } catch (e: Exception) {
                    listState.scrollToItem(viewModel.messages.size)
                }
            }
        }
    }

    Scaffold(
        topBar = {
            ChatTopBar(navController = navController)
        },
        bottomBar = {
            if (viewModel.messages.size == 1) {
                QuickRepliesSection(
                    quickReplies = viewModel.quickReplies,
                    onQuickReplyClick = { reply -> viewModel.sendMessage(reply.text) }
                )
            }
            ChatInputSection(
                messageText = viewModel.messageText,
                onMessageChange = viewModel::updateMessageText,
                onSendMessage = {
                    viewModel.sendMessage(viewModel.messageText)
                    keyboardController?.hide()
                },
                isLoading = viewModel.isTyping,
                errorMessage = viewModel.errorMessage,
                onDismissError = viewModel::clearErrorMessage
            )
        }
    ) { paddingValues ->
        MessageList(
            messages = viewModel.messages,
            isTyping = viewModel.isTyping,
            modifier = Modifier
                .background(Color(0xFFF8F9FA))
                .padding(paddingValues),
            listState = listState
        )
    }
}