package com.example.prediai.presentation.main.chatbot.comps

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.prediai.presentation.main.chatbot.ChatFilter

@Composable
fun ChatbotFilterChips(filters: List<ChatFilter>) {
    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        contentPadding = PaddingValues(horizontal = 16.dp)
    ) {
        items(filters) { filter ->
            Surface(
                shape = RoundedCornerShape(50),
                color = Color(0xFFE8F8F7),
                border = BorderStroke(1.dp, Color(0xFFB2DFDB))
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(painterResource(id = filter.icon), contentDescription = null, tint = Color(0xFF00B4A3))
                    Spacer(Modifier.width(8.dp))
                    Text(filter.label, color = Color(0xFF00B4A3), fontWeight = FontWeight.Medium)
                }
            }
        }
    }
}