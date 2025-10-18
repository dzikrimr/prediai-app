package com.example.prediai.presentation.main.history

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.prediai.presentation.common.TopBar
import com.example.prediai.presentation.main.history.comps.HistoryFilterButtons
import com.example.prediai.presentation.main.history.comps.HistoryItemCard
import com.example.prediai.presentation.theme.PrediAITheme

@Composable
fun HistoryScreen(
    navController: NavController,
    viewModel: HistoryViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            TopBar(
                title = "Riwayat Deteksi",
                subtitle = "Scan History",
                onBackClick = { navController.popBackStack() }
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier.padding(paddingValues),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Filter Buttons
            item {
                HistoryFilterButtons(
                    selectedFilter = uiState.selectedFilter,
                    onFilterClick = viewModel::onFilterChange
                )
            }

            // List Header
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Riwayat Scan", fontWeight = FontWeight.Bold, fontSize = 18.sp)
                    Spacer(modifier = Modifier.weight(1f))
                    Text("${uiState.totalScans} total scan", color = Color.Gray, fontSize = 14.sp)
                }
            }

            // History Items
            items(uiState.historyItems) { item ->
                HistoryItemCard(
                    item = item,
                    onClick = {
                        // Navigasi ke ScanResultScreen dengan membawa ID item
                        navController.navigate("scan_result?historyId=${item.id}")
                    }
                )
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun HistoryScreenPreview() {
    PrediAITheme {
        HistoryScreen(navController = rememberNavController())
    }
}