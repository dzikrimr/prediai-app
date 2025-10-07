package com.example.prediai.presentation.auth

import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController

@Composable
fun QuestionnaireRoot(
    navController: NavController,
    viewModel: AuthViewModel = hiltViewModel()
) {
    var step by remember { mutableIntStateOf(1) }
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current

    LaunchedEffect(uiState.isQuestionnaireSuccess) {
        if (uiState.isQuestionnaireSuccess) {
            navController.navigate("home") {
                popUpTo(navController.graph.startDestinationId) { inclusive = true }
            }
        }
    }

    LaunchedEffect(uiState.questionnaireErrorMessage) {
        uiState.questionnaireErrorMessage?.let {
            Toast.makeText(context, it, Toast.LENGTH_LONG).show()
        }
    }

    QuestionnaireScreen(
        step = step,
        state = uiState,
        onNameChange = viewModel::onNameChange,
        onBirthDateChange = viewModel::onBirthDateChange,
        onHeightChange = viewModel::onHeightChange,
        onWeightChange = viewModel::onWeightChange,
        onCityChange = viewModel::onCityChange,
        onStep2Answer = viewModel::onStep2Answer,
        onStep3Answer = viewModel::onStep3Answer,
        onStep4Answer = viewModel::onStep4Answer,
        onNextClick = {
            if (step < 4) {
                step++
            } else {
                viewModel.submitQuestionnaire()
            }
        }
    )
}