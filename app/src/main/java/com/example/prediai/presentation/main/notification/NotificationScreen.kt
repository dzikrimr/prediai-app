package com.example.prediai.presentation.main.notification

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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.prediai.R
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
        if (uiState.notifications.isEmpty()) {
            Box {
                EmptyNotificationView()
            }
        } else {
            LazyColumn(
                modifier = Modifier.padding(paddingValues),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(uiState.notifications, key = { it.id }) { notificationItem ->
                    NotificationItem(
                        item = notificationItem,
                        // --- GANTI PEMANGGILAN FUNGSI DI SINI ---
                        onDeleteClick = { id -> viewModel.dismissNotification(id) } // Panggil dismiss
                    )
                }
            }
        }
    }
}

// Composable EmptyNotificationView (TIDAK PERLU DIUBAH)
// Padding vertikalnya bisa dikurangi karena Box sudah menengahkan
@Composable
private fun EmptyNotificationView() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            // Kurangi padding vertikal di sini karena Box sudah center
            .padding(horizontal = 16.dp, vertical = 32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Icon(
            painter = painterResource(id = R.drawable.ic_notification_empty), // Ganti dengan ikonmu
            contentDescription = "Tidak Ada Notifikasi",
            modifier = Modifier.size(64.dp),
            tint = Color.LightGray
        )
        Text(
            text = "Belum ada notifikasi baru",
            fontSize = 16.sp,
            color = Color.Gray
        )
    }
}

// --- Preview (Tidak perlu diubah, tapi Empty Preview jadi lebih akurat) ---
@Preview(showBackground = true)
@Composable
fun NotificationScreenPreview() {
    PrediAITheme {
        NotificationScreen(navController = rememberNavController())
    }
}

@Preview(showBackground = true, name = "Notification Screen Empty")
@Composable
fun NotificationScreenEmptyPreview() {
    PrediAITheme {
        val emptyState = NotificationUiState(notifications = emptyList())
        Scaffold(
            topBar = { TopBar(title = "Notifikasi") }
        ) { paddingValues ->
            // Preview sekarang lebih akurat mensimulasikan kondisi kosong
            Box(
                modifier = Modifier.fillMaxSize().padding(paddingValues),
                contentAlignment = Alignment.Center
            ) {
                EmptyNotificationView()
            }
        }
    }
}