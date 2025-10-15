package com.example.prediai.presentation.onboarding

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.prediai.presentation.onboarding.comps.OnboardingButtons
import com.example.prediai.presentation.onboarding.comps.OnboardingPager
import com.example.prediai.presentation.onboarding.comps.OnboardingPage
import com.example.prediai.presentation.theme.PrediAITheme
import com.google.accompanist.pager.*
import kotlinx.coroutines.launch

@OptIn(ExperimentalPagerApi::class)
@Composable
fun OnboardingScreen(
    navController: NavController,
    onSkipClick: () -> Unit,
    onFinishClick: () -> Unit
) {
    val pages = listOf(
        OnboardingPage(
            image = com.example.prediai.R.drawable.onboarding1,
            title = "Scan Kuku & Lidah Sekaligus",
            description = "Deteksi tanda-tanda awal diabetes hanya dengan satu kali scan menggunakan kamera ponsel Anda"
        ),
        OnboardingPage(
            image = com.example.prediai.R.drawable.onboarding2,
            title = "Analisis AI yang Akurat",
            description = "PrediAI menganalisis pola pada kuku dan lidah menggunakan teknologi kecerdasan buatan untuk memperkirakan risiko diabetes Anda."
        ),
        OnboardingPage(
            image = com.example.prediai.R.drawable.onboarding3,
            title = "Rekomendasi & Konsultasi",
            description = "Dapatkan hasil instan lengkap dengan rekomendasi gaya hidup dan opsi konsultasi dokter terdekat."
        )
    )

    val pagerState = rememberPagerState()
    val scope = rememberCoroutineScope()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Top
    ) {
        // Lewati button
        TextButton(onClick = onSkipClick, modifier = Modifier.align(Alignment.End)) {
            Text("Lewati", color = Color.Gray)
        }
        // Pager
        OnboardingPager(
            pagerState = pagerState,
            pages = pages,
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Navigation buttons
        OnboardingButtons(
            onBackClick = {
                scope.launch {
                    pagerState.animateScrollToPage((pagerState.currentPage - 1).coerceAtLeast(0))
                }
            },
            onNextClick = {
                scope.launch {
                    if (pagerState.currentPage < pages.size - 1) {
                        pagerState.animateScrollToPage(pagerState.currentPage + 1)
                    } else {
                        onFinishClick()
                    }
                }
            }
        )
    }
}

@Preview(showBackground = true)
@Composable
fun OnboardingScreenPreview() {
    OnboardingScreen(
        navController = androidx.navigation.compose.rememberNavController(),
        onSkipClick = {},
        onFinishClick = {}
    )
}
