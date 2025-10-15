package com.example.prediai.presentation.main.progress.comps

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Analytics
import androidx.compose.material.icons.filled.PieChart
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.prediai.presentation.theme.PrediAITheme

@Composable
fun SummaryCards(totalScans: Int, averageRisk: Int) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Card 1
        SummaryCard(
            title = "Total",
            value = totalScans.toString(),
            subtitle = "Total Scan",
            icon = Icons.Default.Analytics,
            gradientColors = listOf(Color(0xFF00B4A3), Color(0xFF009688)), // Gradasi hijau
            modifier = Modifier.weight(1f)
        )
        // Card 2
        SummaryCard(
            title = "Avg",
            value = "$averageRisk%",
            subtitle = "Rata-rata Risiko",
            icon = Icons.Default.PieChart,
            gradientColors = listOf(Color(0xFFFFC107), Color(0xFFFB923C)), // Gradasi oranye
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
private fun SummaryCard(
    title: String,
    value: String,
    subtitle: String,
    icon: ImageVector,
    gradientColors: List<Color>,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(20.dp),
        // Buat Card transparan agar background gradasi terlihat
        colors = CardDefaults.cardColors(containerColor = Color.Transparent),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        // Kolom ini sekarang memiliki latar belakang gradasi
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(Brush.verticalGradient(colors = gradientColors))
                .padding(16.dp)
        ) {
            // Bagian atas dengan ikon dan judul ber-frame
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(icon, contentDescription = title, tint = Color.White)
                Spacer(modifier = Modifier.width(8.dp))
                // DIUBAH: Frame untuk teks judul
                Box(
                    modifier = Modifier
                        .background(
                            color = Color.White.copy(alpha = 0.2f),
                            shape = RoundedCornerShape(50) // Bentuk pil/rounded maksimal
                        )
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = title,
                        color = Color.White,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // DIUBAH: Teks nilai utama
            Text(
                text = value,
                color = Color.White,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            )

            // DIUBAH: Teks subjudul
            Text(
                text = subtitle,
                color = Color.White.copy(alpha = 0.9f),
                fontSize = 12.sp,
                fontWeight = FontWeight.Normal // Regular
            )
        }
    }
}

@Preview
@Composable
fun SummaryCardsPreview() {
    PrediAITheme {
        SummaryCards(totalScans = 24, averageRisk = 32)
    }
}