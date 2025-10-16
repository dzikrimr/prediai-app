package com.example.prediai.presentation.main.chatbot.comps

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.prediai.presentation.theme.PrediAITheme

@Composable
fun ChatInputField(
    value: String,
    onValueChange: (String) -> Unit,
    onSendClick: () -> Unit
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shadowElevation = 8.dp,
        color = Color.White
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedTextField(
                value = value,
                onValueChange = onValueChange,
                modifier = Modifier.weight(1f),
                placeholder = { Text("Ketik pesan Anda...") },
                shape = RoundedCornerShape(50),
                // DIUBAH: Menggunakan parameter yang benar
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color.Transparent,
                    unfocusedBorderColor = Color.Transparent,
                    focusedContainerColor = Color(0xFFF3F4F6),
                    unfocusedContainerColor = Color(0xFFF3F4F6)
                )
            )
            Spacer(Modifier.width(8.dp))
            IconButton(
                onClick = onSendClick,
                modifier = Modifier
                    .size(48.dp)
                    .background(Color(0xFF00B4A3), CircleShape),
                enabled = value.isNotBlank()
            ) {
                Icon(Icons.AutoMirrored.Filled.Send, contentDescription = "Send", tint = Color.White)
            }
        }
    }
}

@Preview
@Composable
fun ChatInputFieldPreview() {
    PrediAITheme {
        val text = remember { mutableStateOf("") }
        ChatInputField(
            value = text.value,
            onValueChange = { text.value = it },
            onSendClick = {}
        )
    }
}