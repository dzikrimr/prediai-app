package com.example.prediai.presentation.main

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.prediai.presentation.main.components.RiskStatusCard
import com.example.prediai.presentation.main.comps.*
import com.example.prediai.presentation.theme.PrediAITheme

// ✅ Komponen Stateful: Mengambil data dari ViewModel
@Composable
fun HomeScreen(
    navController: NavController,
    viewModel: MainViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    // Kirim data ke komponen tampilan
    HomeScreenContent(
        uiState = uiState,
        navController = navController
    )
}

// ✅ Komponen Stateless: Menampilkan UI berdasarkan data dari ViewModel
@Composable
fun HomeScreenContent(
    uiState: MainUiState,
    navController: NavController
) {
    LazyColumn(
        modifier = Modifier.padding()
    ) {
        item {
            HeaderSection(userName = uiState.userName)
        }
        item {
            RiskStatusCard(
                riskPercentage = uiState.riskPercentage,
                lastCheckDate = uiState.lastCheckDate,
                lastCheckResult = uiState.lastCheckResult
            )
        }
        item {
            ActionCardsSection()
        }
        item {
            UpcomingRemindersSection(reminders = uiState.reminders)
        }
        item {
            RecommendationsSection(recommendations = uiState.recommendations)
        }
    }
}

// ✅ Preview dengan data dummy
@Preview(showBackground = true, name = "Home Screen (Filled State)")
@Composable
fun HomeScreenFilledPreview() {
    HomeScreenContent(
        uiState = MainUiState(), // Default state
        navController = rememberNavController()
    )
}

@Preview(showBackground = true, name = "Home Screen (Empty State)")
@Composable
fun HomeScreenEmptyPreview() {
    HomeScreenContent(
        uiState = MainUiState(
            riskPercentage = null,
            lastCheckDate = null,
            lastCheckResult = null,
            reminders = emptyList()
        ),
        navController = rememberNavController()
    )
}
