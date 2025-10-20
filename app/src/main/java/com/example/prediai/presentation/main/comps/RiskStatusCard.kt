package com.example.prediai.presentation.main.comps

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.lerp
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.prediai.R
import com.example.prediai.presentation.theme.PrediAITheme

@Composable
fun RiskStatusCard(
    riskPercentage: Int?,
    lastCheckDate: String?,
    lastCheckResult: String?,
    onSeeHistoryClick: () -> Unit // PASTIKAN PARAMETER INI ADA
) {
    val displayPercentage = riskPercentage ?: 0
    val displayDate = lastCheckDate ?: "Belum Ada Pemeriksaan"
    val displayResult = lastCheckResult ?: "Belum dapat mengecek tanpa pengecekan"

    val dynamicRingColor = getInterpolatedColor(displayPercentage)

    Card(
        modifier = Modifier
            .padding(horizontal = 16.dp)
            .fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF00B4A3)),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(24.dp))
        ) {
            Image(
                painter = painterResource(id = R.drawable.sel_darah),
                contentDescription = null,
                modifier = Modifier.matchParentSize(),
                contentScale = ContentScale.Crop
            )

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 15.dp, vertical = 24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(111.dp),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = Color.White.copy(alpha = 0.4f)
                    )
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(horizontal = 16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                "Rata - Rata Risiko",
                                color = Color.White,
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                "Terakhir diperiksa",
                                color = Color.White,
                                fontSize = 14.sp,
                                fontStyle = FontStyle.Italic
                            )
                            Text(
                                displayDate,
                                color = Color.White,
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 14.sp
                            )
                        }
                        Spacer(modifier = Modifier.width(16.dp))

                        Box(
                            modifier = Modifier
                                .size(88.dp)
                                .clip(CircleShape)
                                .background(Color(0xFF008B7D))
                                .border(
                                    width = 5.dp,
                                    color = dynamicRingColor,
                                    shape = CircleShape
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "$displayPercentage%",
                                color = Color.White,
                                fontSize = 24.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "Pengecekan Terakhir mu :",
                    color = Color.White,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 20.sp
                )

                Text(
                    text = displayResult,
                    color = Color.White,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier.padding(top = 4.dp),
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.padding(top = 16.dp))

                Button(
                    onClick = onSeeHistoryClick, // PASTIKAN ONCLICK MENGGUNAKAN PARAMETER
                    shape = RoundedCornerShape(30.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color.White),
                    modifier = Modifier
                        .size(width = 211.dp, height = 48.dp),
                    elevation = ButtonDefaults.buttonElevation(defaultElevation = 8.dp),
                ) {
                    Text(
                        text = "Lihat Riwayat",
                        color = Color(0xFF00B4A3),
                        fontWeight = FontWeight.Medium,
                        fontSize = 16.sp
                    )
                }
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun RiskStatusCardFilledPreview() {
    PrediAITheme {
        Column(verticalArrangement = Arrangement.spacedBy(16.dp), modifier = Modifier.padding(16.dp)) {
            RiskStatusCard(
                riskPercentage = 0,
                lastCheckDate = "15 Jan 2025",
                lastCheckResult = "Tidak ada kemungkinan gejala",
                onSeeHistoryClick = {} // Tambahkan untuk preview
            )
            RiskStatusCard(
                riskPercentage = 32,
                lastCheckDate = "15 Jan 2025",
                lastCheckResult = "Terdeteksi beberapa indikator. Disarankan konsultasi dokter.",
                onSeeHistoryClick = {} // Tambahkan untuk preview
            )
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