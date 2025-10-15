package com.example.prediai.presentation.history

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

@Composable
fun RiwayatScreen(navController: NavController) {
    // Data sample riwayat scan
    val historyItems = listOf(
        HistoryItem("Pemeriksaan Lidah", "13 Okt 2025", "Normal"),
        HistoryItem("Pemeriksaan Kuku", "11 Okt 2025", "Perlu Pemeriksaan Lanjutan"),
        HistoryItem("Pemeriksaan Lidah", "08 Okt 2025", "Normal")
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF9FAFB))
            .padding(horizontal = 16.dp, vertical = 12.dp)
    ) {
        Text(
            text = "Riwayat Pemeriksaan",
            fontSize = 22.sp,
            fontWeight = FontWeight.SemiBold,
            color = Color(0xFF111827),
            modifier = Modifier.padding(top = 8.dp, bottom = 16.dp)
        )

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(historyItems) { item ->
                HistoryCard(item)
            }
        }
    }
}

data class HistoryItem(
    val title: String,
    val date: String,
    val result: String
)

@Composable
fun HistoryCard(item: HistoryItem) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(90.dp),
        shape = MaterialTheme.shapes.medium,
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = item.title,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color(0xFF111827)
                )
                Text(
                    text = item.date,
                    fontSize = 13.sp,
                    color = Color(0xFF6B7280)
                )
            }

            Text(
                text = item.result,
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold,
                color = if (item.result.contains("Normal")) Color(0xFF00B4A3) else Color(0xFFFF6B6B)
            )
        }
    }
}
