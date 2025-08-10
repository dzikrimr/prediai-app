package com.example.prediai

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.prediai.ui.theme.PrediAITheme

data class ScanDetail(
    val scanType: String,
    val date: String,
    val duration: String,
    val accuracy: String,
    val riskLevel: String,
    val riskPercentage: Int,
    val findings: List<String>,
    val additionalInfo: List<AdditionalInfo>,
    val recommendations: List<Recommendation>
)

data class AdditionalInfo(
    val question: String,
    val answer: String,
    val status: String // "Ya", "Tidak", "Normal"
)

data class Recommendation(
    val title: String,
    val description: String,
    val icon: ImageVector,
    val color: Color,
    val actionText: String? = null
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HistoryDetailScreen() {
    val scanDetail = ScanDetail(
        scanType = "Scan Kuku & Lidah",
        date = "15 Januari 2024, 14:30",
        duration = "2 menit 15 detik",
        accuracy = "96.8%",
        riskLevel = "Potensi Risiko Sedang",
        riskPercentage = 65,
        findings = listOf(
            "Perubahan warna pada kuku menunjukkan sirkulasi darah kurang optimal",
            "Lapisan putih pada lidah mengindikasikan kemungkinan gangguan metabolisme",
            "Tekstur kuku menunjukkan tanda-tanda dehidrasi"
        ),
        additionalInfo = listOf(
            AdditionalInfo("Apakah Anda sering merasa haus berlebihan?", "Ya, terutama malam hari", "Ya"),
            AdditionalInfo("Frekuensi buang air kecil meningkat?", "Ya, lebih dari biasanya", "Ya"),
            AdditionalInfo("Merasa lelah tanpa sebab yang jelas?", "Tidak, energi normal", "Tidak")
        ),
        recommendations = listOf(
            Recommendation(
                title = "Konsultasi Medis",
                description = "Segera lakukan pemeriksaan gula darah dan konsultasi dengan dokter untuk skrining lanjut.",
                icon = Icons.Default.LocalHospital,
                color = PrimaryColor,
                actionText = "Cari Dokter"
            ),
            Recommendation(
                title = "Pola Makan",
                description = "• Kurangi konsumsi makanan tinggi gula\n• Perbanyak sayuran hijau dan protein tanpa lemak\n• Makan dalam porsi kecil tapi sering",
                icon = Icons.Default.Restaurant,
                color = SecondaryColor
            ),
            Recommendation(
                title = "Aktivitas Fisik",
                description = "• Olahraga ringan 30 menit setiap hari\n• Jalan kaki setelah makan\n• Yoga atau meditasi untuk mengelola stress",
                icon = Icons.Default.FitnessCenter,
                color = Color(0xFF2196F3)
            ),
            Recommendation(
                title = "Monitoring Rutin",
                description = "Lakukan scan ulang dalam 2 minggu untuk memantau perkembangan kondisi Anda.",
                icon = Icons.Default.Schedule,
                color = Color(0xFF9C27B0)
            )
        )
    )

    Scaffold(
        topBar = {
            DetailTopBar()
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(BackgroundColor)
                .padding(paddingValues),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                // Scan Info Header
                ScanInfoHeader(scanDetail)
            }

            item {
                // Scan Images
                ScanImagesSection()
            }

            item {
                // Analysis Results
                AnalysisResultsSection(scanDetail)
            }

            item {
                // Risk Level
                RiskLevelSection(scanDetail)
            }

            item {
                // Additional Information
                AdditionalInformationSection(scanDetail.additionalInfo)
            }

            item {
                // Recommendations
                RecommendationsSection(scanDetail.recommendations)
            }

            item {
                // Action Buttons
                ActionButtonsSection()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailTopBar() {
    TopAppBar(
        title = {
            Text(
                text = "Detail Riwayat",
                color = Color.Black,
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp
            )
        },
        navigationIcon = {
            IconButton(onClick = { /* Handle back */ }) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Back",
                    tint = Color.Black
                )
            }
        },
        actions = {
            IconButton(onClick = { /* Handle share */ }) {
                Icon(
                    imageVector = Icons.Default.Share,
                    contentDescription = "Share",
                    tint = Color.Black
                )
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Color.White
        )
    )
}

@Composable
fun ScanInfoHeader(scanDetail: ScanDetail) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Fingerprint,
                        contentDescription = "Scan",
                        tint = PrimaryColor,
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = scanDetail.scanType,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 16.sp
                    )
                }

                Box(
                    modifier = Modifier
                        .background(SecondaryColor, RoundedCornerShape(12.dp))
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = "Selesai",
                        color = Color.White,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }

            Text(
                text = scanDetail.date,
                color = Color.Gray,
                fontSize = 14.sp,
                modifier = Modifier.padding(top = 4.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                InfoItem(
                    label = "Waktu Scan",
                    value = scanDetail.duration
                )
                InfoItem(
                    label = "Akurasi",
                    value = scanDetail.accuracy
                )
            }
        }
    }
}

@Composable
fun InfoItem(label: String, value: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = value,
            fontWeight = FontWeight.Bold,
            fontSize = 18.sp,
            color = Color.Black
        )
        Text(
            text = label,
            fontSize = 12.sp,
            color = Color.Gray
        )
    }
}

@Composable
fun ScanImagesSection() {
    Column(modifier = Modifier.padding(horizontal = 16.dp)) {
        Text(
            text = "Gambar Hasil Scan",
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp,
            color = Color.Black
        )

        Spacer(modifier = Modifier.height(12.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            ScanImageCard(
                title = "Kuku",
                modifier = Modifier.weight(1f)
            )
            ScanImageCard(
                title = "Lidah",
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
fun ScanImageCard(title: String, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier
            .height(120.dp)
            .clickable { /* View full image */ },
        colors = CardDefaults.cardColors(containerColor = Color.White),
        shape = RoundedCornerShape(8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            // Placeholder for scan image
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Gray.copy(alpha = 0.1f))
            ) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Icon(
                        imageVector = if (title == "Kuku") Icons.Default.Fingerprint else Icons.Default.Favorite,
                        contentDescription = title,
                        tint = Color.Gray,
                        modifier = Modifier.size(32.dp)
                    )
                    Text(
                        text = "Gambar $title",
                        color = Color.Gray,
                        fontSize = 12.sp
                    )
                }
            }

            // Title overlay
            Box(
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .background(Color.Black.copy(alpha = 0.7f), RoundedCornerShape(4.dp))
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
    }
}

@Composable
fun AnalysisResultsSection(scanDetail: ScanDetail) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.Warning,
                    contentDescription = "Analysis",
                    tint = WarningColor,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Hasil Analisis",
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    color = Color.Black
                )
            }

            Text(
                text = scanDetail.riskLevel,
                color = WarningColor,
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                modifier = Modifier.padding(top = 4.dp)
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = "Indikasi yang Ditemukan:",
                fontWeight = FontWeight.SemiBold,
                fontSize = 14.sp,
                color = Color.Black
            )

            Spacer(modifier = Modifier.height(8.dp))

            scanDetail.findings.forEach { finding ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 2.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(6.dp)
                            .background(WarningColor, CircleShape)
                            .offset(y = 6.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = finding,
                        fontSize = 13.sp,
                        color = Color.Black,
                        lineHeight = 18.sp,
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }
    }
}

@Composable
fun RiskLevelSection(scanDetail: ScanDetail) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                text = "Tingkat Risiko:",
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                color = Color.Black
            )

            Spacer(modifier = Modifier.height(12.dp))

            LinearProgressIndicator(
                progress = scanDetail.riskPercentage / 100f,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(8.dp)
                    .clip(RoundedCornerShape(4.dp)),
                color = WarningColor,
                trackColor = Color.Gray.copy(alpha = 0.2f)
            )

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "${scanDetail.riskPercentage}%",
                    fontWeight = FontWeight.Bold,
                    fontSize = 24.sp,
                    color = WarningColor
                )
            }

            Text(
                text = "Risiko sedang - disarankan pemeriksaan lebih lanjut",
                color = Color.Gray,
                fontSize = 12.sp,
                modifier = Modifier.padding(top = 4.dp)
            )
        }
    }
}

@Composable
fun AdditionalInformationSection(additionalInfo: List<AdditionalInfo>) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                text = "Informasi Tambahan",
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                color = Color.Black
            )

            Spacer(modifier = Modifier.height(12.dp))

            additionalInfo.forEach { info ->
                AdditionalInfoItem(info = info)
                Spacer(modifier = Modifier.height(8.dp))
            }
        }
    }
}

@Composable
fun AdditionalInfoItem(info: AdditionalInfo) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.Top
    ) {
        Box(
            modifier = Modifier
                .size(20.dp)
                .background(PrimaryColor.copy(alpha = 0.1f), CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.QuestionMark,
                contentDescription = "Question",
                tint = PrimaryColor,
                modifier = Modifier.size(12.dp)
            )
        }

        Spacer(modifier = Modifier.width(12.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = info.question,
                fontSize = 14.sp,
                color = Color.Black,
                fontWeight = FontWeight.Medium
            )
            Text(
                text = "→ ${info.answer}",
                fontSize = 13.sp,
                color = Color.Gray,
                modifier = Modifier.padding(top = 2.dp)
            )
        }

        Box(
            modifier = Modifier
                .background(
                    when (info.status) {
                        "Ya" -> WarningColor.copy(alpha = 0.1f)
                        "Tidak" -> SecondaryColor.copy(alpha = 0.1f)
                        else -> Color.Gray.copy(alpha = 0.1f)
                    },
                    RoundedCornerShape(8.dp)
                )
                .padding(horizontal = 8.dp, vertical = 4.dp)
        ) {
            Text(
                text = info.status,
                fontSize = 10.sp,
                color = when (info.status) {
                    "Ya" -> WarningColor
                    "Tidak" -> SecondaryColor
                    else -> Color.Gray
                },
                fontWeight = FontWeight.Medium
            )
        }
    }
}

@Composable
fun RecommendationsSection(recommendations: List<Recommendation>) {
    Column(modifier = Modifier.padding(horizontal = 16.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                imageVector = Icons.Default.Lightbulb,
                contentDescription = "Recommendations",
                tint = PrimaryColor,
                modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "Saran & Rekomendasi",
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                color = Color.Black
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        recommendations.forEach { recommendation ->
            RecommendationCard(recommendation = recommendation)
            Spacer(modifier = Modifier.height(12.dp))
        }
    }
}

@Composable
fun RecommendationCard(recommendation: Recommendation) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .background(recommendation.color.copy(alpha = 0.1f), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = recommendation.icon,
                        contentDescription = recommendation.title,
                        tint = recommendation.color,
                        modifier = Modifier.size(20.dp)
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))

                Text(
                    text = recommendation.title,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    color = Color.Black
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = recommendation.description,
                fontSize = 14.sp,
                color = Color.Gray,
                lineHeight = 20.sp
            )

            recommendation.actionText?.let { actionText ->
                Spacer(modifier = Modifier.height(12.dp))

                Button(
                    onClick = { /* Handle action */ },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = recommendation.color
                    ),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = actionText,
                        color = Color.White,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }
    }
}

@Composable
fun ActionButtonsSection() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // Konsultasi dengan AI Chatbot
        Button(
            onClick = { /* Navigate to chatbot */ },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(containerColor = PrimaryColor),
            shape = RoundedCornerShape(12.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(vertical = 4.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Chat,
                    contentDescription = "Chat",
                    tint = Color.White,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Konsultasi dengan AI Chatbot",
                    color = Color.White,
                    fontWeight = FontWeight.Medium,
                    fontSize = 16.sp
                )
            }
        }

        // Jadwalkan Scan Ulang
        OutlinedButton(
            onClick = { /* Schedule new scan */ },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.outlinedButtonColors(
                contentColor = PrimaryColor
            ),
            border = ButtonDefaults.outlinedButtonBorder.copy(
                width = 2.dp
            ),
            shape = RoundedCornerShape(12.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(vertical = 4.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Schedule,
                    contentDescription = "Schedule",
                    tint = PrimaryColor,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Jadwalkan Scan Ulang",
                    color = PrimaryColor,
                    fontWeight = FontWeight.Medium,
                    fontSize = 16.sp
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun HistoryDetailScreenPreview() {
    PrediAITheme {
        HistoryDetailScreen()
    }
}