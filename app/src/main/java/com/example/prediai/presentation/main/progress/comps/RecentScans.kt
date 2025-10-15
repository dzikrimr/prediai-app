package com.example.prediai.presentation.main.progress.comps

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.PriorityHigh
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.lerp
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.prediai.presentation.main.progress.ScanResult
import com.example.prediai.presentation.main.progress.ScanStatus
import com.example.prediai.presentation.theme.PrediAITheme

@Composable
fun RecentScans(scanResults: List<ScanResult>) {
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
                // DIUBAH: Properti teks "Scan Terbaru"
                Text(
                    text = "Scan Terbaru",
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 18.sp
                )
                // DIUBAH: Properti teks "Lihat Lainnya"
                Text(
                    text = "Lihat Lainnya",
                    color = Color(0xFF00B4A3),
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Column {
                scanResults.forEachIndexed { index, scanResult ->
                    ScanItem(scanResult = scanResult)
                    if (index < scanResults.lastIndex) {
                        HorizontalDivider(color = Color.LightGray.copy(alpha = 0.3f))
                    }
                }
            }
        }
    }
}

@Composable
private fun ScanItem(scanResult: ScanResult) {
    // DIUBAH: Menggunakan warna dinamis berdasarkan persentase
    val dynamicRiskColor = getInterpolatedColor(scanResult.riskPercentage)

    val (icon, iconBgColor) = when (scanResult.status) {
        ScanStatus.LOW -> Icons.Default.Check to Color(0xFFE8F5E9)
        ScanStatus.MEDIUM -> Icons.Default.PriorityHigh to Color(0xFFFFF8E1)
        ScanStatus.HIGH -> Icons.Default.PriorityHigh to Color(0xFFFFEBEE)
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
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
                tint = dynamicRiskColor, // Tint ikon juga menggunakan warna dinamis
                modifier = Modifier.size(20.dp)
            )
        }
        Spacer(modifier = Modifier.width(16.dp))
        Column(modifier = Modifier.weight(1f)) {
            // DIUBAH: Properti teks tanggal
            Text(
                text = scanResult.date,
                fontWeight = FontWeight.Medium,
                fontSize = 14.sp
            )
            // DIUBAH: Properti teks level risiko
            Text(
                text = scanResult.riskLevel,
                fontSize = 12.sp,
                color = Color.Gray,
                fontWeight = FontWeight.Normal // Regular
            )
        }
        Column(horizontalAlignment = Alignment.End) {
            // DIUBAH: Properti teks persentase
            Text(
                text = "${scanResult.riskPercentage}%",
                fontWeight = FontWeight.SemiBold,
                color = dynamicRiskColor, // Menggunakan warna dinamis
                fontSize = 14.sp
            )
            Text(scanResult.time, fontSize = 12.sp, color = Color.Gray)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun RecentScansPreview() {
    PrediAITheme {
        RecentScans(
            scanResults = listOf(
                ScanResult("15 Jan 2024", "Risiko Rendah", 25, "09:30", ScanStatus.LOW),
                ScanResult("14 Jan 2024", "Risiko Sedang", 65, "14:20", ScanStatus.MEDIUM),
                ScanResult("13 Jan 2024", "Risiko Tinggi", 85, "11:15", ScanStatus.HIGH)
            )
        )
    }
}

// Fungsi bantuan untuk transisi warna, sama seperti di RiskStatusCard
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