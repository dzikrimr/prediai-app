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
fun QuestionnaireStep3(
    answers: MutableMap<Int, String>,
    onAnswer: (Int, String) -> Unit,
    onNextClick: () -> Unit,
    isLoading: Boolean
) {
    val primaryColor = Color(0xFF00B4A3)

    // List pertanyaan + opsi
    val questions = listOf(
        "Seberapa sering Anda melakukan aktivitas fisik dalam seminggu?" to listOf("Jarang", "1x", "2-3x", "> 3x"),
        "Seberapa sering Anda mengonsumsi makanan/minuman tinggi gula?" to listOf("Jarang", "Kadang", "Sering", "Setiap Hari"),
        "Apakah Anda memiliki kebiasaan merokok atau minum alkohol?" to listOf("Ya", "Tidak"),
        "Berapa jam tidur rata-rata Anda setiap malam?" to listOf("< 5 jam", "6-7 Jam", "> 8 jam")
    )

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Bantu Kami Kenali Kesehatan Gula Darah Anda",
            fontSize = 15.sp,
            fontWeight = FontWeight.SemiBold,
            color = Color(0xFF2D3748)
        )

        questions.forEachIndexed { index, pair ->
            val (question, options) = pair
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

                when (index) {
                    0, 1 -> { // 4 tombol → 2x2
                        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                            Row(horizontalArrangement = Arrangement.spacedBy(12.dp), modifier = Modifier.fillMaxWidth()) {
                                ButtonOption(option = options[0], index = index, answers = answers, onAnswer = onAnswer, primaryColor)
                                ButtonOption(option = options[1], index = index, answers = answers, onAnswer = onAnswer, primaryColor)
                            }
                            Row(horizontalArrangement = Arrangement.spacedBy(12.dp), modifier = Modifier.fillMaxWidth()) {
                                ButtonOption(option = options[2], index = index, answers = answers, onAnswer = onAnswer, primaryColor)
                                ButtonOption(option = options[3], index = index, answers = answers, onAnswer = onAnswer, primaryColor)
                            }
                        }
                    }
                    2 -> { // 2 tombol Ya/Tidak
                        Row(horizontalArrangement = Arrangement.spacedBy(12.dp), modifier = Modifier.fillMaxWidth()) {
                            options.forEach { option ->
                                ButtonOption(option = option, index = index, answers = answers, onAnswer = onAnswer, primaryColor)
                            }
                        }
                    }
                    3 -> { // 3 tombol sejajar
                        Row(horizontalArrangement = Arrangement.spacedBy(12.dp), modifier = Modifier.fillMaxWidth()) {
                            options.forEach { option ->
                                ButtonOption(option = option, index = index, answers = answers, onAnswer = onAnswer, primaryColor)
                            }
                        }
                    }
                }
            }
        }

        // Tombol Next
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
                Text("Selesai", color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.SemiBold)
            }
        }
    }
}

@Composable
private fun RowScope.ButtonOption(
    option: String,
    index: Int,
    answers: Map<Int, String>,
    onAnswer: (Int, String) -> Unit,
    primaryColor: Color
) {
    Button(
        onClick = { onAnswer(index, option) },
        modifier = Modifier.weight(1f).height(42.dp),
        shape = RoundedCornerShape(12.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = if (answers[index] == option) primaryColor else Color(0xFFE2E8F0),
            contentColor = if (answers[index] == option) Color.White else Color(0xFF2D3748)
        )
    ) {
        Text(option, fontWeight = FontWeight.SemiBold)
    }
}
