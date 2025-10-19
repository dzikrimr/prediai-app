package com.example.prediai.presentation.main.progress.comps

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.PriorityHigh
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.lerp
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.prediai.presentation.main.progress.ScanResult
import com.example.prediai.presentation.main.progress.ScanStatus

@Composable
fun RecentScans(
    scanResults: List<ScanResult>,
    onSeeMoreClick: () -> Unit,
    onItemClick: (String) -> Unit // <-- Parameter baru untuk klik per item
) {
    val isHistoryEmpty = scanResults.isEmpty()

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Scan Terbaru",
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 18.sp
                )
                Text(
                    text = "Lihat Lainnya",
                    color = if (isHistoryEmpty) Color.Gray else Color(0xFF00B4A3),
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier.clickable(
                        enabled = !isHistoryEmpty, // Tombol non-aktif jika riwayat kosong
                        onClick = onSeeMoreClick
                    )
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // --- KONDISI JIKA RIWAYAT KOSONG ---
            if (isHistoryEmpty) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 32.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Belum ada aktivitas scan.",
                        color = Color.Gray,
                        textAlign = TextAlign.Center
                    )
                }
            } else {
                Column {
                    scanResults.forEachIndexed { index, scanResult ->
                        ScanItem(
                            scanResult = scanResult,
                            onClick = { onItemClick(scanResult.id) } // <-- Teruskan ID saat diklik
                        )
                        if (index < scanResults.lastIndex) {
                            HorizontalDivider(color = Color.LightGray.copy(alpha = 0.3f))
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun ScanItem(
    scanResult: ScanResult,
    onClick: () -> Unit
) {
    val dynamicRiskColor = getInterpolatedColor(scanResult.riskPercentage)
    val (icon, iconBgColor) = when (scanResult.status) {
        ScanStatus.LOW -> Icons.Default.Check to Color(0xFFE8F5E9)
        ScanStatus.MEDIUM -> Icons.Default.PriorityHigh to Color(0xFFFFF8E1)
        ScanStatus.HIGH -> Icons.Default.PriorityHigh to Color(0xFFFFEBEE)
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick) // <-- Buat seluruh baris bisa diklik
            .padding(vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(40.dp)
                .background(iconBgColor, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = scanResult.riskLevel,
                tint = dynamicRiskColor,
                modifier = Modifier.size(20.dp)
            )
        }
        Spacer(modifier = Modifier.width(16.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(scanResult.date, fontWeight = FontWeight.Medium, fontSize = 14.sp)
            Text(scanResult.riskLevel, fontSize = 12.sp, color = Color.Gray, fontWeight = FontWeight.Normal)
        }
        Column(horizontalAlignment = Alignment.End) {
            Text("${scanResult.riskPercentage}%", fontWeight = FontWeight.SemiBold, color = dynamicRiskColor, fontSize = 14.sp)
            Text(scanResult.time, fontSize = 12.sp, color = Color.Gray)
        }
    }
}

private fun getInterpolatedColor(percentage: Int): Color {
    return when {
        percentage <= 60 -> lerp(
            start = Color(0xFF9CA3AF),
            stop = Color(0xFFFFBE0A),
            fraction = percentage / 60f
        )
        percentage <= 80 -> lerp(
            start = Color(0xFFFFBE0A),
            stop = Color(0xFFFC4D43),
            fraction = (percentage - 60) / 20f
        )
        else -> Color(0xFFFC4D43)
    }
}