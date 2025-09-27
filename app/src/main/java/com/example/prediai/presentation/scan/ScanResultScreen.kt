package com.example.prediai.presentation.scan

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
//import com.example.prediai.presentation.questionaire.QuestionnaireAnswers
import kotlinx.serialization.json.Json

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScanResultsScreen(
    navController: NavController,
    answersJson: String? = null
) {
    val tealColor = Color(0xFF00BFA5)
//    val answers = answersJson?.let { Json.decodeFromString<QuestionnaireAnswers>(it) }

    // Calculate risk level based on answers
//    val (riskLevel, riskPercentage, conclusion) = calculateRiskLevel(answers)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF8F9FA))
    ) {
        // Header Section
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(bottomStart = 24.dp, bottomEnd = 24.dp),
            colors = CardDefaults.cardColors(containerColor = tealColor),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
        ) {
            Column(
                modifier = Modifier.padding(24.dp)
            ) {
                // Top bar with back button and title
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Back",
                        tint = Color.White,
                        modifier = Modifier
                            .size(24.dp)
                            .clickable { navController.navigateUp() }
                    )
                    Spacer(modifier = Modifier.width(16.dp))
                    Text(
                        text = "Hasil Scan",
                        color = Color.White,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                Spacer(modifier = Modifier.height(20.dp))

                // Scan completed info
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = Color.White.copy(alpha = 0.15f)
                    )
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .background(Color.White.copy(alpha = 0.2f), CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.CheckCircle,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(20.dp)
                            )
                        }

                        Spacer(modifier = Modifier.width(12.dp))

                        Column {
                            Text(
                                text = "Scan Selesai",
                                color = Color.White,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                            Text(
                                text = "Analisis kuku & lidah berhasil",
                                color = Color.White.copy(alpha = 0.8f),
                                fontSize = 14.sp
                            )
                        }
                    }
                }
            }
        }

        LazyColumn(
            modifier = Modifier.padding(horizontal = 24.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            item { Spacer(modifier = Modifier.height(8.dp)) }

            // Analysis Results Section
//            item {
//                AnalysisResultsSection(riskLevel, riskPercentage, conclusion)
//            }

            // Recommendations Section
            item {
                RecommendationsSection()
            }

            // Action Buttons
            item {
                Column(
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Button(
                        onClick = {
                            // Handle save result
                        },
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(containerColor = tealColor),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Save,
                            contentDescription = null,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Simpan Hasil Scan",
                            modifier = Modifier.padding(vertical = 8.dp),
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }

                    OutlinedButton(
                        onClick = {
                            navController.navigate("chatbot")
                        },
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.outlinedButtonColors(
                            contentColor = tealColor
                        ),
                        border = BorderStroke(
                            width = 1.dp,
                            color = tealColor
                        ),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Chat,
                            contentDescription = null,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Konsultasi dengan AI",
                            modifier = Modifier.padding(vertical = 8.dp),
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }

                    OutlinedButton(
                        onClick = {
                            // Handle share result
                        },
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.outlinedButtonColors(
                            contentColor = Color(0xFF666666)
                        ),
                        border = BorderStroke(
                            width = 1.dp,
                            color = Color(0xFFE0E0E0)
                        ),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Share,
                            contentDescription = null,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Bagikan Hasil",
                            modifier = Modifier.padding(vertical = 8.dp),
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }

            item { Spacer(modifier = Modifier.height(24.dp)) }
        }
    }
}

@Composable
fun AnalysisResultsSection(riskLevel: String, riskPercentage: Float, conclusion: String) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(20.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.Analytics,
                    contentDescription = null,
                    tint = Color(0xFF4CAF50),
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Hasil Analisis",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Risk Level
            Column {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Tingkat Risiko",
                        fontSize = 14.sp,
                        color = Color(0xFF333333)
                    )
                    Text(
                        text = riskLevel,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium,
                        color = when (riskLevel) {
                            "Rendah" -> Color(0xFFFF8F00)
                            "Sedang" -> Color(0xFFFF5722)
                            "Tinggi" -> Color(0xFFD32F2F)
                            else -> Color(0xFFFF8F00)
                        }
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Progress bar
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(8.dp)
                        .background(Color(0xFFF0F0F0), RoundedCornerShape(4.dp))
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth(riskPercentage)
                            .fillMaxHeight()
                            .background(
                                when (riskLevel) {
                                    "Rendah" -> Color(0xFFFF8F00)
                                    "Sedang" -> Color(0xFFFF5722)
                                    "Tinggi" -> Color(0xFFD32F2F)
                                    else -> Color(0xFFFF8F00)
                                },
                                RoundedCornerShape(4.dp)
                            )
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "${(riskPercentage * 100).toInt()}% kemungkinan gejala diabetes",
                    fontSize = 12.sp,
                    color = Color(0xFF666666)
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Analysis Details
            Row(
                horizontalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                AnalysisDetail(
                    icon = Icons.Default.Fingerprint,
                    title = "Analisis Kuku",
                    description = "Warna dan tekstur normal",
                    iconColor = Color(0xFF00BFA5),
                    modifier = Modifier.weight(1f)
                )

                AnalysisDetail(
                    icon = Icons.Default.Visibility,
                    title = "Analisis Lidah",
                    description = "Tidak ada tanda diabetes",
                    iconColor = Color(0xFF2196F3),
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Conclusion Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFEFF6FF))
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Info,
                            contentDescription = null,
                            tint = Color(0xFF1D4ED8),
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Kesimpulan",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Color(0xFF1D4ED8)
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = conclusion,
                        fontSize = 14.sp,
                        color = Color(0xFF1D4ED8),
                        lineHeight = 20.sp
                    )
                }
            }
        }
    }
}

@Composable
fun AnalysisDetail(
    icon: ImageVector,
    title: String,
    description: String,
    iconColor: Color,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(40.dp)
                .background(iconColor.copy(alpha = 0.1f), CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = iconColor,
                modifier = Modifier.size(20.dp)
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = title,
            fontSize = 12.sp,
            fontWeight = FontWeight.Medium,
            textAlign = TextAlign.Center
        )

        Text(
            text = description,
            fontSize = 10.sp,
            color = Color(0xFF666666),
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun RecommendationsSection() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(20.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.Recommend,
                    contentDescription = null,
                    tint = Color(0xFF2196F3),
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Saran Pencegahan",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Recommendation items
            RecommendationItem(
                icon = Icons.Default.Restaurant,
                title = "Pola Makan Sehat",
                description = "Konsumsi makanan bergizi seimbang, kurangi gula dan karbohidrat berlebih",
                iconColor = Color(0xFF4CAF50)
            )

            Spacer(modifier = Modifier.height(16.dp))

            RecommendationItem(
                icon = Icons.Default.FitnessCenter,
                title = "Olahraga Teratur",
                description = "Lakukan aktivitas fisik minimal 30 menit setiap hari",
                iconColor = Color(0xFF2196F3)
            )

            Spacer(modifier = Modifier.height(16.dp))

            RecommendationItem(
                icon = Icons.Default.Schedule,
                title = "Pemeriksaan Rutin",
                description = "Lakukan cek gula darah setiap 6 bulan sekali",
                iconColor = Color(0xFF9C27B0)
            )
        }
    }
}

@Composable
fun RecommendationItem(
    icon: ImageVector,
    title: String,
    description: String,
    iconColor: Color
) {
    Row(
        verticalAlignment = Alignment.Top
    ) {
        Box(
            modifier = Modifier
                .size(40.dp)
                .background(iconColor.copy(alpha = 0.1f), CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = iconColor,
                modifier = Modifier.size(20.dp)
            )
        }

        Spacer(modifier = Modifier.width(12.dp))

        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = title,
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                color = iconColor
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = description,
                fontSize = 12.sp,
                color = Color(0xFF666666),
                lineHeight = 16.sp
            )
        }
    }
}

//private fun calculateRiskLevel(answers: QuestionnaireAnswers?): Triple<String, Float, String> {
//    if (answers == null) {
//        return Triple(
//            "Rendah",
//            0.25f,
//            "Berdasarkan analisis citra kuku dan lidah, tidak ditemukan indikasi kuat diabetes. Namun, tetap lakukan pemeriksaan rutin untuk pencegahan."
//        )
//    }
//
//    var riskScore = 0
//    if (answers.haus == "Ya") riskScore += 2
//    if (answers.berat == "Ya") riskScore += 2
//    when (answers.buang_air) {
//        "Sering" -> riskScore += 2
//        "Sangat Sering" -> riskScore += 3
//    }
//
//    val (riskLevel, riskPercentage) = when (riskScore) {
//        in 0..2 -> "Rendah" to 0.25f
//        in 3..5 -> "Sedang" to 0.50f
//        else -> "Tinggi" to 0.75f
//    }
//
//    val conclusion = when (riskLevel) {
//        "Rendah" -> "Berdasarkan analisis citra kuku, lidah, dan kuesioner, risiko diabetes rendah. Tetap jaga pola hidup sehat dan lakukan pemeriksaan rutin."
//        "Sedang" -> "Analisis menunjukkan risiko diabetes sedang berdasarkan kuesioner dan citra. Konsultasikan dengan dokter untuk pemeriksaan lebih lanjut."
//        "Tinggi" -> "Hasil analisis dan kuesioner menunjukkan risiko diabetes tinggi. Segera konsultasikan dengan dokter untuk evaluasi menyeluruh."
//        else -> "Berdasarkan analisis citra kuku dan lidah, tidak ditemukan indikasi kuat diabetes. Namun, tetap lakukan pemeriksaan rutin untuk pencegahan."
//    }
//
//    return Triple(riskLevel, riskPercentage, conclusion)
//}