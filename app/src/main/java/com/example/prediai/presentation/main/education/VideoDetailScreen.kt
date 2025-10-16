package com.example.prediai.presentation.main.education

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.prediai.presentation.common.TopBar
import com.example.prediai.presentation.main.education.comps.VideoDescription
import com.example.prediai.presentation.main.education.comps.YoutubePlayerView
import com.example.prediai.presentation.theme.PrediAITheme

@Composable
fun VideoDetailScreen(
    navController: NavController,
    video: EducationVideo?
) {
    Scaffold(
        topBar = {
            TopBar(
                title = "Penjelasan Video",
                subtitle = "Video Explanation",
                onBackClick = { navController.popBackStack() }
            )
        }
    ) { paddingValues ->
        if (video == null) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("Video tidak ditemukan.")
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
            VideoDescription(video = video)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun VideoDetailScreenPreview() {
    PrediAITheme {
        VideoDetailScreen(
            navController = rememberNavController(),
            video = EducationVideo("1", "Penyebab, Ciri-Ciri dan Pencegahan Penyakit Gula", "RS Premier Jatinegara", "", "", "S5-zKft4-nI")
        )
    }
}