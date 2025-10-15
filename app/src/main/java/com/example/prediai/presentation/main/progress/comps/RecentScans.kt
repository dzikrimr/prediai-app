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
                Text("Scan Terbaru", fontWeight = FontWeight.Bold, fontSize = 18.sp)
                Text("Lihat Lainnya", color = MaterialTheme.colorScheme.primary, fontSize = 14.sp, fontWeight = FontWeight.SemiBold)
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
    val (icon, iconBgColor) = when (scanResult.status) {
        ScanStatus.LOW -> Icons.Default.Check to Color(0xFFE8F5E9) // Light Green
        ScanStatus.MEDIUM -> Icons.Default.PriorityHigh to Color(0xFFFFF8E1) // Light Yellow
        ScanStatus.HIGH -> Icons.Default.PriorityHigh to Color(0xFFFFEBEE) // Light Red
    }
    val riskColor = when (scanResult.status) {
        ScanStatus.LOW -> Color(0xFF4CAF50) // Green
        ScanStatus.MEDIUM -> Color(0xFFFF9800) // Orange
        ScanStatus.HIGH -> Color(0xFFE53935) // Red
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
                tint = riskColor,
                modifier = Modifier.size(20.dp)
            )
        }
        Spacer(modifier = Modifier.width(16.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(scanResult.date, fontWeight = FontWeight.SemiBold)
            Text(scanResult.riskLevel, fontSize = 14.sp, color = Color.Gray)
        }
        Column(horizontalAlignment = Alignment.End) {
            Text(
                text = "${scanResult.riskPercentage}%",
                fontWeight = FontWeight.Bold,
                color = riskColor,
                fontSize = 18.sp
            )
            Text(scanResult.time, fontSize = 12.sp, color = Color.Gray)
        }
    }
}

@Preview
@Composable
fun RecentScansPreview() {
    PrediAITheme {
        RecentScans(
            scanResults = listOf(
                ScanResult("15 Jan 2024", "Risiko Rendah", 25, "09:30", ScanStatus.LOW),
                ScanResult("14 Jan 2024", "Risiko Sedang", 45, "14:20", ScanStatus.MEDIUM),
            )
        )
    }
}