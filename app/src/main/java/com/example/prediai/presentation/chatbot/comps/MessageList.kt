package com.example.prediai.presentation.chatbot.comps

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.prediai.domain.model.Message

@Composable
fun MessageList(
    messages: List<Message>,
    isTyping: Boolean,
    modifier: Modifier = Modifier,
    listState: LazyListState
) {
    LazyColumn(
        state = listState,
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        contentPadding = PaddingValues(bottom = 16.dp)
    ) {
        item {
            OnlineStatusIndicator()
        }
        items(messages) { message ->
            MessageBubble(message = message)
        }
        if (isTyping) {
            item {
                TypingIndicator()
            }
        }
    }
}