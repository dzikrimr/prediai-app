package com.example.prediai.presentation.quistionere.comps

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun SectionCard(
    title: String,
    questions: List<Pair<String, List<String>>>,
    answers: Map<String, String>, // Diubah ke Map agar lebih stabil
    onAnswerChange: (question: String, answer: String) -> Unit // Parameter baru
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 2.dp
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            // Section Title - Centered
            Text(
                text = title,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF2D3142),
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Questions
            questions.forEach { (question, options) ->
                Spacer(modifier = Modifier.height(16.dp))
                QuestionItem(
                    question = question,
                    options = options,
                    // Mengambil jawaban menggunakan teks pertanyaan sebagai kunci
                    selectedOption = answers[question],
                    // Mengirim event perubahan dengan teks pertanyaan sebagai kunci
                    onOptionSelected = { selectedAnswer ->
                        onAnswerChange(question, selectedAnswer)
                    }
                )
            }
        }
    }
}