package com.example.prediai.presentation.main

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.prediai.presentation.common.BottomNavigationBar
import com.example.prediai.presentation.main.comps.*
import com.example.prediai.presentation.theme.PrediAITheme

@Composable
fun HomeScreen(
    mainNavController: NavHostController, // "Peta Kota"
    rootNavController: NavHostController, // "Peta Dunia"
    viewModel: MainViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    HomeScreenContent(
        uiState = uiState,
        mainNavController = mainNavController,
        rootNavController = rootNavController
    )
}

@Composable
fun HomeScreenContent(
    uiState: MainUiState,
    mainNavController: NavHostController, // "Peta Kota"
    rootNavController: NavHostController  // "Peta Dunia"
) {

    val statusBarPadding = with(LocalDensity.current) {
        WindowInsets.statusBars.getTop(this).toDp()
    }

    // Tambahkan handling untuk loading dan error state
    if (uiState.isLoading) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
    } else if (uiState.errorMessage != null) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(text = uiState.errorMessage ?: "Terjadi kesalahan tidak diketahui")
        }
    } else {
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(16.dp),
            contentPadding = PaddingValues(bottom = 16.dp, top = 16.dp + statusBarPadding,)
        ) {
            item {
                HeaderSection(
                    userName = uiState.userName,
                    onNotificationClick = { rootNavController.navigate("notification") },
                    onProfileClick = { mainNavController.navigate("profil") }
                )
            }

            item {
                RiskStatusCard(
                    riskPercentage = uiState.riskPercentage,
                    lastCheckDate = uiState.lastCheckDate,
                    lastCheckResult = uiState.lastCheckResult,
                    onSeeHistoryClick = { mainNavController.navigate("riwayat") }
                )
            }

            item {
                ActionCardsSection(
                    onFindDoctorClick = { mainNavController.navigate("doctor") },
                    // DIUBAH: Gunakan rootNavController untuk navigasi ke "chatbot"
                    onConsultationClick = { rootNavController.navigate("chatbot") }
                )
            }

            item {
                UpcomingRemindersSection(
                    reminders = uiState.reminders,
                    onAddScheduleClick = { rootNavController.navigate("schedule") },
                    onSeeAllClick = { rootNavController.navigate("schedule") }
                )
            }

            item {
                RecommendationsSection(
                    recommendations = uiState.recommendations,
                    onSeeMoreClick = { rootNavController.navigate("education_list") },
                    onItemClick = { videoId -> rootNavController.navigate("video_detail/$videoId") }
                )
            }
        }
    }
}

@Preview(showBackground = true, name = "Home Screen (Filled State)")
@Composable
fun HomeScreenFilledPreview() {
    PrediAITheme {
        Scaffold(
            bottomBar = {
                BottomNavigationBar(currentRoute = "beranda", onNavigate = {})
            }
        ) { paddingValues ->
            Box(modifier = Modifier.padding(paddingValues)) {
                HomeScreenContent(
                    uiState = MainUiState(),
                    mainNavController = rememberNavController(),
                    rootNavController = rememberNavController()
                )
            }
        }
    }
}

@Preview(showBackground = true, name = "Home Screen (Empty State)")
@Composable
fun HomeScreenEmptyPreview() {
    PrediAITheme {
        Scaffold(
            bottomBar = {
                BottomNavigationBar(currentRoute = "beranda", onNavigate = {})
            }
        ) { paddingValues ->
            Box(modifier = Modifier.padding(paddingValues)) {
                HomeScreenContent(
                    uiState = MainUiState(
                        riskPercentage = null,
                        lastCheckDate = null,
                        lastCheckResult = null,
                        reminders = emptyList()
                    ),
                    mainNavController = rememberNavController(),
                    rootNavController = rememberNavController()
                )
            }
        }
    }
}