package com.example.prediai.presentation.main.chatbot.comps

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.prediai.presentation.main.chatbot.QuickReply

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QuickReplies(replies: List<QuickReply>, onReplyClick: (String) -> Unit) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Pertanyaan yang sering ditanyakan", color = Color.Gray, fontSize = 14.sp)
        Spacer(Modifier.height(16.dp))

        // DIUBAH: Tambahkan padding horizontal pada Column ini
        Column(
            modifier = Modifier.padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            replies.forEach { reply ->
                Card(
                    onClick = { onReplyClick(reply.text) },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(painterResource(id = reply.icon), contentDescription = null, tint = Color(0xFF00B4A3))
                        Spacer(Modifier.width(16.dp))
                        Text(reply.text, modifier = Modifier.weight(1f))
                    }
                }
            }
        }
    }
}