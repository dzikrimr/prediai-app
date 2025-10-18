package com.example.prediai.presentation.main.education

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
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
// Import composable filter yang baru
import com.example.prediai.presentation.main.education.comps.EducationCategoryFilter
import com.example.prediai.presentation.main.education.comps.EducationVideoItem
import com.example.prediai.presentation.theme.PrediAITheme

// @OptIn(ExperimentalMaterial3Api::class) // <-- Sudah tidak perlu
@Composable
fun EducationListScreen(
    navController: NavController,
    viewModel: EducationViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            TopBar(
                title = "Video Edukasi",
                subtitle = "Education Video",
                onBackClick = { navController.popBackStack() }
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier.padding(paddingValues),
            contentPadding = PaddingValues(vertical = 16.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {

            // --- GANTI UI FILTER DENGAN COMPOSABLE BARU ---
            item {
                EducationCategoryFilter(
                    categories = uiState.categories,
                    selectedCategory = uiState.selectedCategory,
                    onCategoryClick = { category ->
                        viewModel.selectCategory(category)
                    }
                )
            }
            // --- AKHIR PENGGANTIAN ---

            item { HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp)) }

            items(uiState.videos) { video ->
                EducationVideoItem(
                    video = video,
                    onClick = { navController.navigate("video_detail/${video.id}") }
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun EducationListScreenPreview() {
    PrediAITheme {
        EducationListScreen(navController = rememberNavController())
    }
}