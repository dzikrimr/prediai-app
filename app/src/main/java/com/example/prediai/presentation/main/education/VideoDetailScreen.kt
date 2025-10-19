package com.example.prediai.presentation.main.education

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect // <-- IMPORT BARU
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.prediai.presentation.common.TopBar
import com.example.prediai.presentation.main.education.comps.VideoDescription
import com.example.prediai.presentation.main.education.comps.YoutubePlayerView
import com.example.prediai.presentation.theme.PrediAITheme

@Composable
fun VideoDetailScreen(
    navController: NavController,
    // --- GANTI CARA MENDAPATKAN VIEWMODEL ---
    // Cara 1: Jika EducationViewModel adalah shared ViewModel untuk NavGraph ini
    // viewModel: EducationViewModel = hiltViewModel(remember { navController.previousBackStackEntry!! })

    // Cara 2: Jika ViewModel dibuat per screen (lebih sederhana untuk kasus ini)
    viewModel: EducationViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    // --- AMBIL VIDEO DARI STATE VIEWMODEL ---
    val video = uiState.selectedVideo

    // --- (OPSIONAL TAPI BAGUS) Bersihkan state saat keluar ---
    DisposableEffect(Unit) {
        onDispose {
            viewModel.clearSelectedVideo()
        }
    }

    Scaffold(
        topBar = {
            TopBar(
                title = "Penjelasan Video",
                subtitle = "Video Explanation",
                onBackClick = { navController.popBackStack() }
            )
        }
    ) { paddingValues ->
        // Tampilkan loading jika video BELUM DIPILIH di ViewModel
        if (video == null) {
            Box(modifier = Modifier.fillMaxSize().padding(paddingValues), contentAlignment = Alignment.Center) {
                // Bisa jadi user langsung buka link detail,
                // perlu logic tambahan di ViewModel untuk load by ID jika 'selectedVideo' null
                Text("Pilih video dari daftar terlebih dahulu.")
            }
            return@Scaffold
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            YoutubePlayerView(youtubeVideoId = video.youtubeVideoId)
            VideoDescription(
                video = video,
                aiSummary = uiState.aiSummary,
                isSummaryLoading = uiState.isSummaryLoading,
                summaryError = uiState.summaryError,
                transcriptError = uiState.transcriptError
            )
        }
    }
}

// Preview mungkin perlu sedikit disesuaikan jika ViewModel butuh SavedStateHandle
@Preview(showBackground = true)
@Composable
fun VideoDetailScreenPreview() {
    PrediAITheme {
        // Preview tidak bisa meng-inject ViewModel dengan SavedStateHandle
        // Jadi, kita tampilkan saja strukturnya
        Scaffold(
            topBar = { TopBar(title = "Penjelasan Video", subtitle = "Video Explanation") }
        ) { padding ->
            Column(Modifier.padding(padding).padding(16.dp)) {
                Text("Preview: Player akan muncul di sini")
                Spacer(Modifier.height(16.dp))
                Text("Preview: Deskripsi akan muncul di sini")
            }
        }
    }
}