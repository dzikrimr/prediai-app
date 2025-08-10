package com.example.prediai

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScanResultsScreen(navController: NavController) {
    val tealColor = Color(0xFF00BFA5)

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

            // Questionnaire Section
            item {
                QuestionnaireSection()
            }

            // Analysis Results Section
            item {
                AnalysisResultsSection()
            }

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
                            // Navigate to AI consultation
                        },
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.outlinedButtonColors(
                            contentColor = tealColor
                        ),
                        border = androidx.compose.foundation.BorderStroke(
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
                        border = androidx.compose.foundation.BorderStroke(
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
fun QuestionnaireSection() {
    var selectedAnswers by remember {
        mutableStateOf(mapOf(
            "haus" to "Ya",
            "berat" to "Tidak",
            "buang_air" to "Sering"
        ))
    }

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
                    imageVector = Icons.Default.Assignment,
                    contentDescription = null,
                    tint = Color(0xFF00BFA5),
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Pertanyaan Tambahan",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Question 1
            QuestionItem(
                question = "Apakah Anda sering merasa haus berlebihan?",
                selectedAnswer = selectedAnswers["haus"] ?: "",
                options = listOf("Ya", "Tidak"),
                onAnswerSelected = { answer ->
                    selectedAnswers = selectedAnswers + ("haus" to answer)
                }
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Question 2
            QuestionItem(
                question = "Apakah Anda mengalami penurunan berat badan tanpa sebab?",
                selectedAnswer = selectedAnswers["berat"] ?: "",
                options = listOf("Ya", "Tidak"),
                onAnswerSelected = { answer ->
                    selectedAnswers = selectedAnswers + ("berat" to answer)
                }
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Question 3
            Text(
                text = "Seberapa sering Anda buang air kecil dalam sehari?",
                fontSize = 14.sp,
                color = Color(0xFF333333)
            )

            Spacer(modifier = Modifier.height(8.dp))

            // First row of options
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                AnswerChip(
                    text = "Normal",
                    isSelected = selectedAnswers["buang_air"] == "Normal",
                    onClick = { selectedAnswers = selectedAnswers + ("buang_air" to "Normal") },
                    modifier = Modifier.weight(1f)
                )
                AnswerChip(
                    text = "Sering",
                    isSelected = selectedAnswers["buang_air"] == "Sering",
                    onClick = { selectedAnswers = selectedAnswers + ("buang_air" to "Sering") },
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Second row of options
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                AnswerChip(
                    text = "Sangat Sering",
                    isSelected = selectedAnswers["buang_air"] == "Sangat Sering",
                    onClick = { selectedAnswers = selectedAnswers + ("buang_air" to "Sangat Sering") },
                    modifier = Modifier.weight(1f)
                )
                AnswerChip(
                    text = "Jarang",
                    isSelected = selectedAnswers["buang_air"] == "Jarang",
                    onClick = { selectedAnswers = selectedAnswers + ("buang_air" to "Jarang") },
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}

@Composable
fun QuestionItem(
    question: String,
    selectedAnswer: String,
    options: List<String>,
    onAnswerSelected: (String) -> Unit
) {
    Column {
        Text(
            text = question,
            fontSize = 14.sp,
            color = Color(0xFF333333)
        )

        Spacer(modifier = Modifier.height(8.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            options.forEach { option ->
                AnswerChip(
                    text = option,
                    isSelected = option == selectedAnswer,
                    onClick = { onAnswerSelected(option) },
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}

@Composable
fun AnswerChip(
    text: String,
    isSelected: Boolean,
    onClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    val backgroundColor = if (isSelected) Color(0xFF00BFA5) else Color(0xFFF5F5F5)
    val textColor = if (isSelected) Color.White else Color(0xFF666666)

    Box(
        modifier = modifier
            .background(backgroundColor, RoundedCornerShape(10.dp))
            .clickable { onClick() }
            .padding(horizontal = 16.dp, vertical = 12.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            color = textColor,
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun AnalysisResultsSection() {
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
                        text = "Rendah",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color(0xFFFF8F00)
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
                            .fillMaxWidth(0.25f)
                            .fillMaxHeight()
                            .background(Color(0xFFFF8F00), RoundedCornerShape(4.dp))
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "25% kemungkinan gejala diabetes",
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
                        text = "Berdasarkan analisis citra kuku dan lidah, tidak ditemukan indikasi kuat diabetes. Namun, tetap lakukan pemeriksaan rutin untuk pencegahan.",
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

@Preview(showBackground = true)
@Composable
fun ScanResultsScreenPreview() {
    MaterialTheme {
        val navController = rememberNavController()
        ScanResultsScreen(navController = navController)
    }
}