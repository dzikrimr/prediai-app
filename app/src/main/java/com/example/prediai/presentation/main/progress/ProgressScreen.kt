package com.example.prediai.presentation.main.progress

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
import com.example.prediai.presentation.common.TopBar
import com.example.prediai.presentation.main.progress.comps.RecentScans
import com.example.prediai.presentation.main.progress.comps.RiskDevelopmentChart
import com.example.prediai.presentation.main.progress.comps.SummaryCards
import com.example.prediai.presentation.theme.PrediAITheme

@Composable
fun ProgressScreen(
    navController: NavController,
    viewModel: ProgressViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            TopBar(
                title = "Kemajuan Pelacakan",
                subtitle = "Progress Tracking",
                onBackClick = { navController.popBackStack() }
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier.padding(
                top = paddingValues.calculateTopPadding()
            ),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            item {
                SummaryCards(
                    totalScans = uiState.totalScans,
                    averageRisk = uiState.averageRisk
                )
            }
            item {
                RiskDevelopmentChart(
                    selectedFilter = uiState.selectedFilter,
                    onFilterChanged = viewModel::onFilterChanged,
                    data = uiState.chartData
                )
            }
            item {
                RecentScans(
                    scanResults = uiState.recentScans,
                    onSeeMoreClick = {
                        navController.navigate("history_detail")
                    },
                    onItemClick = { scanId ->
                        navController.navigate("scan_result?historyId=$scanId")
                    }
                )
            }
            item {
                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ProgressScreenPreview() {
    PrediAITheme {
        ProgressScreen(navController = rememberNavController())
    }
}