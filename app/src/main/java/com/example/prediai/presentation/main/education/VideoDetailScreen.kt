package com.example.prediai.presentation.main.education

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.prediai.presentation.common.TopBar
import com.example.prediai.presentation.main.education.comps.VideoDescription
import com.example.prediai.presentation.main.education.comps.YoutubePlayerView
import com.example.prediai.presentation.theme.PrediAITheme

@Composable
fun VideoDetailScreen(
    navController: NavController,
    viewModel: EducationViewModel
) {
    val uiState by viewModel.uiState.collectAsState()
    val video = uiState.selectedVideo

    // LaunchedEffect akan memicu proses summary saat 'video' berhasil dimuat
    LaunchedEffect(key1 = video) {
        video?.let {
            val youtubeUrl = "https://www.youtube.com/watch?v=${it.youtubeVideoId}"
            Log.d("VideoDebug", "VideoDetailScreen: Summarizing URL -> $youtubeUrl")
            viewModel.summarizeVideoFromUrl(youtubeUrl)
        }
    }

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
        // Tampilkan loading indicator sampai detail video berhasil dimuat
        if (video == null) {
            Box(
                modifier = Modifier.fillMaxSize().padding(paddingValues),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
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
            Log.d("VideoDebug", "VideoDetailScreen: Playing YouTube ID -> ${video.youtubeVideoId}")
            YoutubePlayerView(youtubeVideoId = video.youtubeVideoId)
            VideoDescription(
                video = video,
                aiSummary = uiState.aiSummary,
                isSummaryLoading = uiState.isSummaryLoading,
                summaryError = uiState.summaryError,
                // Hapus parameter transcriptError karena sudah tidak ada
                transcriptError = null
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