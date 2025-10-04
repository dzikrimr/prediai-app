package com.example.prediai.presentation.auth

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.prediai.presentation.auth.comps.DataFormStep
import com.example.prediai.presentation.auth.comps.QuestionnaireStep
import com.example.prediai.presentation.auth.comps.QuestionnaireStep2
import com.example.prediai.presentation.auth.comps.QuestionnaireStep3
import com.example.prediai.presentation.theme.PrediAITheme

@Composable
fun QuestionnaireScreen(
    navController: NavController,
    step: Int,
    name: String,
    birthDate: String,
    height: String,
    weight: String,
    city: String,
    onNameChange: (String) -> Unit,
    onBirthDateChange: (String) -> Unit,
    onHeightChange: (String) -> Unit,
    onWeightChange: (String) -> Unit,
    onCityChange: (String) -> Unit,
    onNextClick: () -> Unit,
    isLoading: Boolean
) {
    PrediAITheme {
        Scaffold { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(24.dp),
                verticalArrangement = Arrangement.spacedBy(24.dp)
            ) {
                // Progress Section
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center // ✅ Biar rata tengah
                ) {
                    // Biar teks dan bar ada spasi
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        // Keterangan step
                        Text(
                            text = "$step/4",
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color(0xFF2D3748)
                        )

                        // Progress Bar dengan radius
                        Box(
                            modifier = Modifier
                                .fillMaxWidth(0.6f) // lebih pendek
                                .height(12.dp)
                                .clip(RoundedCornerShape(6.dp)) // ✅ radius
                        ) {
                            LinearProgressIndicator(
                                progress = step / 4f,
                                modifier = Modifier.matchParentSize(),
                                trackColor = Color(0xFFE2E8F0),
                                color = Color(0xFF00B4A3)
                            )
                        }
                    }
                }

                // Step 1 form
                if (step == 1) {
                    DataFormStep(
                        name = name,
                        birthDate = birthDate,
                        height = height,
                        weight = weight,
                        city = city,
                        cityOptions = listOf("Malang", "Surabaya", "Jakarta", "Bandung"),
                        onNameChange = onNameChange,
                        onBirthDateChange = onBirthDateChange,
                        onHeightChange = onHeightChange,
                        onWeightChange = onWeightChange,
                        onCityChange = onCityChange,
                        onNextClick = onNextClick,
                        isLoading = isLoading
                    )
                }

                if (step == 2) {
                    QuestionnaireStep(
                        answers = mapOf(), // state jawaban kamu simpan di ViewModel atau remember
                        onAnswer = { index, answer -> /* update state jawaban */ },
                        onNextClick = onNextClick,
                        isLoading = isLoading
                    )
                }

                if (step == 3) {
                    QuestionnaireStep2(
                        answers = mapOf(),
                        onAnswer = { index, answer -> /* update state */ },
                        onNextClick = onNextClick,
                        isLoading = isLoading
                    )
                }

                if (step == 4) {
                    QuestionnaireStep3(
                        answers = mutableMapOf(),
                        onAnswer = { index, answer -> /* update state */ },
                        onNextClick = onNextClick,
                        isLoading = isLoading
                    )
                }

            }
        }
    }
}

@Preview()
@Composable
fun QuestionnaireScreenPreview() {
    val navController = rememberNavController()
    QuestionnaireScreen(
        navController = navController,
        step = 4,
        name = "",
        birthDate = "",
        height = "",
        weight = "",
        city = "",
        onNameChange = {},
        onBirthDateChange = {},
        onHeightChange = {},
        onWeightChange = {},
        onCityChange = {},
        onNextClick = {},
        isLoading = false
    )
}
