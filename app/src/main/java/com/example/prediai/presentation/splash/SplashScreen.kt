package com.example.prediai.presentation.splash

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import kotlinx.coroutines.flow.collectLatest

@Composable
fun SplashScreen(
    navController: NavController,
    viewModel: SplashViewModel = hiltViewModel()
) {
    // State untuk memastikan navigasi hanya terjadi sekali
    var hasNavigated by remember { mutableStateOf(false) }

    LaunchedEffect(key1 = true) {
        // Cek status onboarding terlebih dahulu
        viewModel.getOnboardingState().collectLatest { isCompleted ->
            if (hasNavigated) return@collectLatest // Hentikan jika sudah navigasi

            if (!isCompleted) {
                // Jika onboarding belum selesai, arahkan ke sana
                hasNavigated = true
                navController.navigate("onboarding") {
                    popUpTo("splash") { inclusive = true }
                }
            } else {
                // Jika onboarding sudah selesai, cek status login
                viewModel.getCachedUserUid().collectLatest { uid ->
                    if (hasNavigated) return@collectLatest // Hentikan jika sudah navigasi

                    val destination = if (!uid.isNullOrEmpty()) {
                        // Jika ada UID (sudah login), refresh data di background dan arahkan ke "main"
                        viewModel.refreshCache(uid)
                        "main" // DIUBAH: Menggunakan "main" bukan "home"
                    } else {
                        // Jika tidak ada UID, arahkan ke "login"
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

    // Tampilan loading
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator()
    }
}