package com.example.prediai.presentation.main.chatbot.comps

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.platform.SoftwareKeyboardController
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatInputSection(
    messageText: String,
    onMessageChange: (String) -> Unit,
    onSendMessage: () -> Unit,
    isLoading: Boolean,
    errorMessage: String?,
    onDismissError: () -> Unit,
    keyboardController: SoftwareKeyboardController? = null
) {
    Column {
        errorMessage?.let { error ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 4.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.errorContainer
                )
            ) {
                Row(
                    modifier = Modifier.padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Filled.Error,
                        contentDescription = "Error",
                        tint = MaterialTheme.colorScheme.onErrorContainer,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = error,
                        color = MaterialTheme.colorScheme.onErrorContainer,
                        fontSize = 12.sp
                    )
                    Spacer(modifier = Modifier.weight(1f))
                    IconButton(
                        onClick = onDismissError,
                        modifier = Modifier.size(20.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Close,
                            contentDescription = "Close",
                            tint = MaterialTheme.colorScheme.onErrorContainer,
                            modifier = Modifier.size(14.dp)
                        )
                    }
                }
            }
        }

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                verticalAlignment = Alignment.Bottom
            ) {
                OutlinedTextField(
                    value = messageText,
                    onValueChange = onMessageChange,
                    modifier = Modifier
                        .weight(1f)
                        .padding(horizontal = 4.dp),
                    placeholder = {
                        Text(
                            text = if (isLoading) "AI sedang memproses..." else "Ketik pesan Anda...",
                            color = MaterialTheme.colorScheme.outline,
                            fontSize = 14.sp
                        )
                    },
                    enabled = !isLoading,
                    keyboardOptions = KeyboardOptions(
                        imeAction = ImeAction.Send,
                        keyboardType = KeyboardType.Text
                    ),
                    keyboardActions = KeyboardActions(
                        onSend = {
                            if (!isLoading) {
                                onSendMessage()
                                keyboardController?.hide()
                            }
                        }
                    ),
                    shape = RoundedCornerShape(20.dp),
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        focusedBorderColor = Color(0xFF00B4A3),
                        unfocusedBorderColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.3f),
                        containerColor = Color.Transparent
                    ),
                    maxLines = 4
                )
                IconButton(
                    onClick = {
                        if (!isLoading) {
                            onSendMessage()
                        }
                    },
                    enabled = messageText.isNotBlank() && !isLoading
                ) {
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .background(
                                if (messageText.isNotBlank() && !isLoading) Color(0xFF00B4A3)
                                else MaterialTheme.colorScheme.surfaceVariant,
                                RoundedCornerShape(16.dp)
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        if (isLoading) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(20.dp),
                                color = Color.White,
                                strokeWidth = 2.dp
                            )
                        } else {
                            Icon(
                                imageVector = Icons.Filled.Send,
                                contentDescription = "Send",
                                tint = Color.White,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}