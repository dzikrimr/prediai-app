package com.example.prediai.presentation.main.history.comps

import android.R.attr.onClick
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.PriorityHigh
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.prediai.presentation.main.history.HistoryStatus
import com.example.prediai.presentation.main.history.ScanHistoryItem
import com.example.prediai.presentation.theme.PrediAITheme

@Composable
fun HistoryItemCard(
    item: ScanHistoryItem,
    onClick: () -> Unit
) {
    val statusColor = when (item.status) {
        HistoryStatus.NORMAL -> Color(0xFF4CAF50)
        HistoryStatus.PERINGATAN -> Color(0xFFFFC107)
        HistoryStatus.TINGGI -> Color(0xFFFC4D43)
        else -> Color.Gray
    }

    val icon = when (item.status) {
        HistoryStatus.NORMAL -> Icons.Default.Check
        HistoryStatus.PERINGATAN -> Icons.Default.Warning
        HistoryStatus.TINGGI -> Icons.Default.PriorityHigh
        else -> Icons.Default.Check
    }

    // BARU: Buat interactionSource untuk clickable
    val interactionSource = remember { MutableInteractionSource() }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // Bagian Atas: Ikon, Judul, Persentase
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .background(statusColor.copy(alpha = 0.1f), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(imageVector = icon, contentDescription = item.title, tint = statusColor)
                }
                Spacer(modifier = Modifier.width(16.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(item.title, fontWeight = FontWeight.SemiBold, fontSize = 16.sp)
                    Text(item.date, color = Color.Gray, fontSize = 12.sp)
                }
                Box(
                    modifier = Modifier
                        .background(statusColor.copy(alpha = 0.15f), RoundedCornerShape(50))
                        .padding(horizontal = 12.dp, vertical = 6.dp)
                ) {
                    Text(
                        text = "${item.percentage}%",
                        color = statusColor,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Bagian Tengah: Deskripsi
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(item.description, color = Color.Gray, fontSize = 14.sp)
                Text(item.timeAgo, color = Color.Gray, fontSize = 14.sp)
            }

            HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp))

            // DIUBAH: Gunakan overload clickable yang lebih lengkap
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable(
                        onClick = onClick
                    ),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Lihat Detail",
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.Medium
                )
                Spacer(modifier = Modifier.weight(1f))
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                    contentDescription = "Lihat Detail",
                    tint = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}

@Preview
@Composable
fun HistoryItemCardPreview() {
    PrediAITheme {
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            HistoryItemCard(
                item = ScanHistoryItem("1", "Risiko Tinggi", "Hari ini, 14:30", timeAgo = "2 menit yang lalu", percentage = 85, status = HistoryStatus.TINGGI),
                onClick = {}
            )
            HistoryItemCard(
                item = ScanHistoryItem("2", "Peringatan", "Kemarin, 09:15", timeAgo = "1 hari yang lalu", percentage = 65, status = HistoryStatus.PERINGATAN),
                onClick = {}
            )
        }
    }
}