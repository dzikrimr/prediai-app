package com.example.prediai

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Assignment
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Error
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

@Serializable
data class QuestionnaireAnswers(
    val haus: String,
    val berat: String,
    val buang_air: String
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QuestionnaireScreen(navController: NavController) {
    val tealColor = Color(0xFF00BFA5)
    var selectedAnswers by remember {
        mutableStateOf(mapOf(
            "haus" to "Ya",
            "berat" to "Tidak",
            "buang_air" to "Sering"
        ))
    }
    var errorMessage by remember { mutableStateOf<String?>(null) }

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
                        text = "Kuesioner Kesehatan",
                        color = Color.White,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }

        // Error Message
        errorMessage?.let { error ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp, vertical = 8.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.errorContainer
                )
            ) {
                Row(
                    modifier = Modifier.padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Error,
                        contentDescription = "Error",
                        tint = MaterialTheme.colorScheme.onErrorContainer,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = error,
                        color = MaterialTheme.colorScheme.onErrorContainer,
                        fontSize = 12.sp
                    )
                    Spacer(modifier = Modifier.weight(1f))
                    IconButton(
                        onClick = { errorMessage = null },
                        modifier = Modifier.size(20.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Close",
                            tint = MaterialTheme.colorScheme.onErrorContainer,
                            modifier = Modifier.size(14.dp)
                        )
                    }
                }
            }
        }

        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            item { Spacer(modifier = Modifier.height(8.dp)) }

            // Questionnaire Section
            item {
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
                                tint = tealColor,
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

            // Continue Button
            item {
                Button(
                    onClick = {
                        // Check if all questions are answered
                        if (selectedAnswers["haus"] == null || selectedAnswers["berat"] == null || selectedAnswers["buang_air"] == null) {
                            errorMessage = "Harap jawab semua pertanyaan sebelum melanjutkan."
                            return@Button
                        }

                        try {
                            val answers = QuestionnaireAnswers(
                                haus = selectedAnswers["haus"] ?: "Ya",
                                berat = selectedAnswers["berat"] ?: "Tidak",
                                buang_air = selectedAnswers["buang_air"] ?: "Sering"
                            )
                            val answersJson = Json.encodeToString(answers)
                            // URL-encode the JSON string to handle special characters
                            val encodedAnswersJson = URLEncoder.encode(answersJson, StandardCharsets.UTF_8.toString())
                            navController.navigate("scan_results/$encodedAnswersJson")
                        } catch (e: Exception) {
                            errorMessage = "Gagal memproses jawaban: ${e.message}"
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = tealColor),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text(
                        text = "Lanjutkan",
                        modifier = Modifier.padding(vertical = 8.dp),
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }

            item { Spacer(modifier = Modifier.height(24.dp)) }
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