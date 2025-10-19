package com.example.prediai.presentation.splash

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.prediai.R // Pastikan import R sudah ada
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest

@Composable
fun SplashScreen(
    navController: NavController,
    viewModel: SplashViewModel = hiltViewModel()
) {
    var hasNavigated by remember { mutableStateOf(false) }

    // --- TAMPILAN (UI) SPLASH SCREEN DITAMBAHKAN DI SINI ---
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White), // Ganti dengan warna latar belakang splash screen Anda
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(id = R.drawable.prediailogo), // Asumsikan nama filenya adalah 'prediailogo'
            contentDescription = "PrediAI Logo",
            modifier = Modifier.size(150.dp) // Sesuaikan ukuran sesuai kebutuhan
        )
        // Jika Anda ingin menampilkan indikator loading di bawah logo
        /*
        CircularProgressIndicator(
            modifier = Modifier.align(Alignment.BottomCenter).padding(bottom = 50.dp),
            color = Color.Black // Sesuaikan warna
        )
        */
    }
    // --------------------------------------------------------

    LaunchedEffect(key1 = true) {
        delay(1000L)
        // Cek status onboarding terlebih dahulu
        viewModel.getOnboardingState().collectLatest { isCompleted ->
            if (hasNavigated) return@collectLatest

            if (!isCompleted) {
                // ... (Logika navigasi ke "onboarding")
                hasNavigated = true
                navController.navigate("onboarding") {
                    popUpTo("splash") { inclusive = true }
                }
            } else {
                // ... (Logika cek status login)
                viewModel.getCachedUserUid().collectLatest { uid ->
                    if (hasNavigated) return@collectLatest

                    val destination = if (!uid.isNullOrEmpty()) {
                        viewModel.refreshCache(uid)
                        "main"
                    } else {
                        "login"
                    }
                    hasNavigated = true
                    navController.navigate(destination) {
                        popUpTo("splash") { inclusive = true }
                    }
                }
            }
        }
    }
}