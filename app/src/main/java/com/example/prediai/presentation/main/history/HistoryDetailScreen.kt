package com.example.prediai.presentation.main.history

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.prediai.presentation.main.BackgroundColor
import com.example.prediai.presentation.main.DangerColor
import com.example.prediai.presentation.main.PrimaryColor
import com.example.prediai.presentation.main.SecondaryColor
import com.example.prediai.presentation.main.WarningColor

data class ScanDetail(
    val scanType: String,
    val date: String,
    val riskLevel: String,
    val riskPercentage: Int,
    val findings: List<String>,
    val additionalInfo: List<AdditionalInfo>,
    val recommendations: List<Recommendation>
)

data class AdditionalInfo(
    val question: String,
    val answer: String,
    val status: String // "Ya", "Tidak", "Normal", "Sering", "Sangat Sering", "Jarang"
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
fun HistoryDetailScreen(scanId: Int, navController: NavController) {
    // Simulasi data berdasarkan scanId (dalam praktik nyata, ambil dari database atau state)
    val scanDetail = when (scanId) {
        1 -> ScanDetail(
            scanType = "Scan Kuku & Lidah",
            date = "10 Aug 2025, 15:37",
            riskLevel = "Risiko Tinggi",
            riskPercentage = 85,
            findings = listOf(
                "Perubahan warna pada kuku menunjukkan sirkulasi darah kurang optimal",
                "Lapisan putih pada lidah mengindikasikan kemungkinan gangguan metabolisme",
                "Tekstur kuku menunjukkan tanda-tanda dehidrasi"
            ),
            additionalInfo = listOf(
                AdditionalInfo("Sering merasa haus berlebihan?", "Ya, terutama malam hari", "Ya"),
                AdditionalInfo("Penurunan berat badan tanpa sebab?", "Tidak, energi normal", "Tidak"),
                AdditionalInfo("Frekuensi buang air kecil?", "Sering", "Sering")
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
        2 -> ScanDetail(
            scanType = "Scan Kuku & Lidah",
            date = "9 Aug 2025, 09:15",
            riskLevel = "Peringatan",
            riskPercentage = 65,
            findings = listOf(
                "Sirkulasi darah sedikit terganggu",
                "Lapisan tipis pada lidah terdeteksi"
            ),
            additionalInfo = listOf(
                AdditionalInfo("Sering merasa haus berlebihan?", "Tidak", "Tidak"),
                AdditionalInfo("Penurunan berat badan tanpa sebab?", "Tidak", "Tidak"),
                AdditionalInfo("Frekuensi buang air kecil?", "Sering", "Sering")
            ),
            recommendations = listOf(
                Recommendation(
                    title = "Pola Makan",
                    description = "Perbanyak sayuran hijau dan kurangi gula.",
                    icon = Icons.Default.Restaurant,
                    color = SecondaryColor
                ),
                Recommendation(
                    title = "Aktivitas Fisik",
                    description = "Lakukan olahraga ringan 20 menit sehari.",
                    icon = Icons.Default.FitnessCenter,
                    color = Color(0xFF2196F3)
                )
            )
        )
        3 -> ScanDetail(
            scanType = "Scan Kuku & Lidah",
            date = "8 Aug 2025, 16:45",
            riskLevel = "Normal",
            riskPercentage = 15,
            findings = listOf("Tidak ada indikasi abnormal"),
            additionalInfo = listOf(
                AdditionalInfo("Sering merasa haus berlebihan?", "Tidak", "Tidak"),
                AdditionalInfo("Penurunan berat badan tanpa sebab?", "Tidak", "Tidak"),
                AdditionalInfo("Frekuensi buang air kecil?", "Normal", "Normal")
            ),
            recommendations = listOf(
                Recommendation(
                    title = "Pola Hidup Sehat",
                    description = "Jaga pola makan dan olahraga rutin.",
                    icon = Icons.Default.FitnessCenter,
                    color = SecondaryColor
                )
            )
        )
        4 -> ScanDetail(
            scanType = "Scan Kuku & Lidah",
            date = "7 Aug 2025, 11:20",
            riskLevel = "Normal",
            riskPercentage = 20,
            findings = listOf("Semua parameter normal"),
            additionalInfo = listOf(
                AdditionalInfo("Sering merasa haus berlebihan?", "Tidak", "Tidak"),
                AdditionalInfo("Penurunan berat badan tanpa sebab?", "Tidak", "Tidak"),
                AdditionalInfo("Frekuensi buang air kecil?", "Normal", "Normal")
            ),
            recommendations = listOf(
                Recommendation(
                    title = "Pola Hidup Sehat",
                    description = "Terus jaga kesehatan Anda.",
                    icon = Icons.Default.FitnessCenter,
                    color = SecondaryColor
                )
            )
        )
        else -> ScanDetail(
            scanType = "Scan Kuku & Lidah",
            date = "10 Aug 2025, 15:37",
            riskLevel = "Potensi Risiko Sedang",
            riskPercentage = 65,
            findings = listOf(
                "Perubahan warna pada kuku menunjukkan sirkulasi darah kurang optimal",
                "Lapisan putih pada lidah mengindikasikan kemungkinan gangguan metabolisme"
            ),
            additionalInfo = listOf(
                AdditionalInfo("Sering merasa haus berlebihan?", "Ya", "Ya"),
                AdditionalInfo("Penurunan berat badan tanpa sebab?", "Tidak", "Tidak"),
                AdditionalInfo("Frekuensi buang air kecil?", "Sering", "Sering")
            ),
            recommendations = listOf(
                Recommendation(
                    title = "Konsultasi Medis",
                    description = "Segera lakukan pemeriksaan gula darah.",
                    icon = Icons.Default.LocalHospital,
                    color = PrimaryColor,
                    actionText = "Cari Dokter"
                )
            )
        )
    }

    Scaffold(
        topBar = {
            DetailTopBar(navController = navController)
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
                ActionButtonsSection(navController = navController)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailTopBar(navController: NavController) {
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
            IconButton(onClick = { navController.popBackStack() }) {
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
        }
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
                    tint = if (scanDetail.riskLevel.contains("Tinggi")) DangerColor else WarningColor,
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
                color = if (scanDetail.riskLevel.contains("Tinggi")) DangerColor else WarningColor,
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
                            .background(if (scanDetail.riskLevel.contains("Tinggi")) DangerColor else WarningColor, CircleShape)
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
                color = if (scanDetail.riskLevel.contains("Tinggi")) DangerColor else WarningColor,
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
                    color = if (scanDetail.riskLevel.contains("Tinggi")) DangerColor else WarningColor
                )
            }

            Text(
                text = "Risiko ${if (scanDetail.riskLevel.contains("Tinggi")) "tinggi" else "sedang"} - disarankan pemeriksaan lebih lanjut",
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
    val context = LocalContext.current
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
                    onClick = {
                        if (actionText == "Cari Dokter") {
                            val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://www.halodoc.com/cari-dokter/spesialis/spesialis-penyakit-dalam-endokrin-metabolik-diabetes"))
                            context.startActivity(intent)
                        }
                    },
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
fun ActionButtonsSection(navController: NavController) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // Konsultasi dengan AI Chatbot
        Button(
            onClick = {
                navController.navigate("chatbot") {
                    popUpTo(navController.graph.startDestinationId) {
                        saveState = true
                    }
                    launchSingleTop = true
                    restoreState = true
                }
            },
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
    }
}