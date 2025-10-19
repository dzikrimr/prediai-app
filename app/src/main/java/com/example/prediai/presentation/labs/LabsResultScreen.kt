package com.example.prediai.presentation.labs

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.prediai.domain.model.LabAnalysisResult
import com.example.prediai.presentation.common.TopBar
import com.example.prediai.presentation.labs.comps.AIExplanationCard
import com.example.prediai.presentation.labs.comps.FileInfoCard

@Composable
fun LabResultScreen(
    fileName: String,
    uploadDate: String,
    navController: NavController,
    viewModel: LabsViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val analysisResult = uiState.analysisResult

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF5F5F5))
    ) {
        TopBar(
            title = "Lab Analysis",
            onBackClick = { navController.popBackStack() }
        )

        if (uiState.isLoading || analysisResult == null) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    CircularProgressIndicator()
                    Spacer(modifier = Modifier.height(16.dp))
                    Text("AI sedang menganalisis dokumen Anda...")
                }
            }
        } else {
            ResultContent(
                fileName = fileName,
                uploadDate = uploadDate,
                analysisResult = analysisResult,
                onConsultClick = { navController.navigate("doctor") }
            )
        }
    }
}

@Composable
private fun ResultContent(
    fileName: String,
    uploadDate: String,
    analysisResult: LabAnalysisResult,
    onConsultClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        Spacer(modifier = Modifier.height(16.dp))
        FileInfoCard(fileName = fileName, uploadDate = uploadDate)
        Spacer(modifier = Modifier.height(16.dp))
        AIExplanationCard(
            summary = analysisResult.summary,
            findings = analysisResult.key_findings,
            nextSteps = analysisResult.next_steps,
            onConsultClick = onConsultClick
        )
        Spacer(modifier = Modifier.height(16.dp))
    }
}