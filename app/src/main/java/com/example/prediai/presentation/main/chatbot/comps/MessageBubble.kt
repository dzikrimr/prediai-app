package com.example.prediai.presentation.main.chatbot.comps

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.prediai.R
import com.example.prediai.presentation.main.chatbot.ChatMessage

@Composable
fun MessageBubble(message: ChatMessage) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = if (message.isFromUser) Arrangement.End else Arrangement.Start
    ) {
        if (!message.isFromUser) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .background(Color(0xFF00B4A3), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_chatbot_icon),
                    contentDescription = "AI Icon",
                    tint = Color.White,
                    modifier = Modifier.size(24.dp)
                )
            }
            Spacer(Modifier.width(8.dp))
        }

        Column(
            horizontalAlignment = if (message.isFromUser) Alignment.End else Alignment.Start
        ) {
            Card(
                shape = RoundedCornerShape(
                    topStart = 16.dp,
                    topEnd = 16.dp,
                    bottomStart = if (message.isFromUser) 16.dp else 0.dp,
                    bottomEnd = if (message.isFromUser) 0.dp else 16.dp
                ),
                colors = CardDefaults.cardColors(
                    containerColor = if (message.isFromUser) Color(0xFF00B4A3) else Color.White
                ),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
            ) {
                Text(
                    text = message.text,
                    modifier = Modifier.padding(16.dp),
                    color = if (message.isFromUser) Color.White else Color.Black.copy(alpha = 0.8f)
                )
            }
            Spacer(Modifier.height(4.dp))
            Text(message.timestamp, fontSize = 12.sp, color = Color.Gray, modifier = Modifier.padding(horizontal = 8.dp))
        }
    }
}