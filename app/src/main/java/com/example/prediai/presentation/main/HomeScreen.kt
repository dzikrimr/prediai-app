package com.example.prediai.presentation.main

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.prediai.presentation.common.BottomNavigationBar
import com.example.prediai.presentation.main.comps.RiskStatusCard
import com.example.prediai.presentation.main.comps.*
import com.example.prediai.presentation.theme.PrediAITheme

// 1. Komponen Stateful: Mengambil data dari ViewModel
@Composable
fun HomeScreen(
    navController: NavController,
    viewModel: MainViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    // Memanggil komponen stateless dengan data dari ViewModel
    HomeScreenContent(
        uiState = uiState,
        navController = navController
    )
}

// 2. Komponen Stateless: Hanya menampilkan UI berdasarkan data yang diterima
@Composable
fun HomeScreenContent(
    uiState: MainUiState,
    navController: NavController
) {
    Scaffold(
        bottomBar = {
            BottomNavigationBar(
                currentRoute = "beranda",
                onNavigate = { route -> navController.navigate(route) }
            )
        }
    ) { paddingValues ->
        // DIUBAH: Tambahkan verticalArrangement di sini
        LazyColumn(
            modifier = Modifier.padding(paddingValues),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                Spacer(modifier = Modifier.height(0.dp))
            }

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

            item {
                Spacer(modifier = Modifier.height(0.dp))
            }
        }
    }
}

// 3. Preview: Memanggil komponen stateless dengan data palsu (mock)
@Preview(showBackground = true, name = "Home Screen (Filled State)")
@Composable
fun HomeScreenFilledPreview() {
    PrediAITheme {
        HomeScreenContent(
            uiState = MainUiState(), // Menggunakan data default dari MainUiState
            navController = rememberNavController()
        )
    }
}

@Preview(showBackground = true, name = "Home Screen (Empty State)")
@Composable
fun HomeScreenEmptyPreview() {
    PrediAITheme {
        HomeScreenContent(
            // Data palsu untuk kondisi kosong
            uiState = MainUiState(
                riskPercentage = null,
                lastCheckDate = null,
                lastCheckResult = null,
                reminders = emptyList()
            ),
            navController = rememberNavController()
        )
    }
}