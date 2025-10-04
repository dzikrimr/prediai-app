package com.example.prediai.presentation.auth.comps

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun QuestionnaireStep(
    answers: Map<Int, Boolean?>,
    onAnswer: (Int, Boolean) -> Unit,
    onNextClick: () -> Unit,
    isLoading: Boolean
) {
    val primaryColor = Color(0xFF00B4A3)

    val questions = listOf(
        "Apakah Anda sering merasa mudah lelah tanpa aktivitas berat?",
        "Apakah luka pada tubuh Anda lama sembuhnya?",
        "Apakah Anda sering mengalami penglihatan kabur?",
        "Apakah Anda sering merasa kesemutan di tangan atau kaki?",
        "Apakah Anda sering merasa kesemutan di tangan atau kaki?"
    )

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Title
        Text(
            text = "Bantu Kami Kenali Kesehatan Gula Darah Anda",
            fontSize = 15.sp,
            fontWeight = FontWeight.SemiBold,
            color = Color(0xFF2D3748)
        )

        // Questions
        questions.forEachIndexed { index, question ->
            Column(
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = question,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color(0xFF4A5568)
                )

                Row(
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Button(
                        onClick = { onAnswer(index, true) },
                        modifier = Modifier.weight(1f).height(42.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (answers[index] == true) primaryColor else Color(0xFFE2E8F0),
                            contentColor = if (answers[index] == true) Color.White else Color(0xFF2D3748)
                        )
                    ) {
                        Text("Ya", fontWeight = FontWeight.SemiBold)
                    }

                    Button(
                        onClick = { onAnswer(index, false) },
                        modifier = Modifier.weight(1f).height(42.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (answers[index] == false) primaryColor else Color(0xFFE2E8F0),
                            contentColor = if (answers[index] == false) Color.White else Color(0xFF2D3748)
                        )
                    ) {
                        Text("Tidak", fontWeight = FontWeight.SemiBold)
                    }
                }
            }
        }

        // Next Button
        Button(
            onClick = onNextClick,
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp),
            enabled = !isLoading,
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = primaryColor,
                disabledContainerColor = Color(0xFFE2E8F0)
            )
        ) {
            if (isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.size(20.dp),
                    color = Color.White,
                    strokeWidth = 2.dp
                )
            } else {
                Text("Selanjutnya", color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.SemiBold)
            }
        }
    }
}


