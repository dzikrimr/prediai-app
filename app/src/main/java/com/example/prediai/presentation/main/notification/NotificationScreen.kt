package com.example.prediai.presentation.main.notification

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import com.example.prediai.presentation.main.notification.comps.NotificationItem
import com.example.prediai.presentation.theme.PrediAITheme

@Composable
fun NotificationScreen(
    navController: NavController,
    viewModel: NotificationViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            TopBar(
                title = "Notifikasi",
                onBackClick = { navController.popBackStack() }
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier.padding(paddingValues),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(uiState.notifications) { notificationItem ->
                NotificationItem(item = notificationItem)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun NotificationScreenPreview() {
    PrediAITheme {
        NotificationScreen(navController = rememberNavController())
    }
}