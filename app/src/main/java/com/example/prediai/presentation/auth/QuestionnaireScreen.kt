package com.example.prediai.presentation.auth

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.prediai.presentation.auth.comps.DataFormStep
import com.example.prediai.presentation.auth.comps.QuestionnaireStep
import com.example.prediai.presentation.auth.comps.QuestionnaireStep2
import com.example.prediai.presentation.auth.comps.QuestionnaireStep3
import com.example.prediai.presentation.theme.PrediAITheme

@Composable
fun QuestionnaireScreen(
    step: Int,
    state: AuthUiState,
    onNameChange: (String) -> Unit,
    onBirthDateChange: (String) -> Unit,
    onHeightChange: (String) -> Unit,
    onWeightChange: (String) -> Unit,
    onCityChange: (String) -> Unit,
    onStep2Answer: (String, Boolean) -> Unit,
    onStep3Answer: (String, Boolean) -> Unit,
    onStep4Answer: (String, String) -> Unit,
    onNextClick: () -> Unit
) {
    Scaffold { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text(
                        text = "$step/4",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color(0xFF2D3748)
                    )
                    Box(
                        modifier = Modifier
                            .fillMaxWidth(0.6f)
                            .height(12.dp)
                            .clip(RoundedCornerShape(6.dp))
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

            if (step == 1) {
                DataFormStep(
                    name = state.name,
                    birthDate = state.birthDate,
                    height = state.height,
                    weight = state.weight,
                    city = state.city,
                    cityOptions = listOf("Malang", "Surabaya", "Jakarta", "Bandung"),
                    onNameChange = onNameChange,
                    onBirthDateChange = onBirthDateChange,
                    onHeightChange = onHeightChange,
                    onWeightChange = onWeightChange,
                    onCityChange = onCityChange,
                    onNextClick = onNextClick,
                    isLoading = state.isLoading
                )
            }

            if (step == 2) {
                QuestionnaireStep(
                    answers = state.step2Answers,
                    onAnswer = onStep2Answer,
                    onNextClick = onNextClick,
                    isLoading = state.isLoading
                )
            }

            if (step == 3) {
                QuestionnaireStep2(
                    answers = state.step3Answers,
                    onAnswer = onStep3Answer,
                    onNextClick = onNextClick,
                    isLoading = state.isLoading
                )
            }

            if (step == 4) {
                QuestionnaireStep3(
                    answers = state.step4Answers,
                    onAnswer = onStep4Answer,
                    onNextClick = onNextClick,
                    isLoading = state.isLoading
                )
            }
        }
    }
}