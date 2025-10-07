package com.example.prediai.presentation.splash

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
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
    val isOnboardingCompleted by viewModel.getOnboardingState().collectAsState(initial = null)
    val userUid by viewModel.getCachedUserUid().collectAsState(initial = null)

    LaunchedEffect(isOnboardingCompleted, userUid) {
        // Hanya jalankan jika kita sudah mendapat status onboarding
        if (isOnboardingCompleted != null) {
            if (isOnboardingCompleted == false) {
                navController.navigate("onboarding") { popUpTo("splash") { inclusive = true } }
            } else {
                // Onboarding selesai, sekarang cek status login dari UID
                if (userUid != null) { // Pengecekan selesai
                    val destination = if (userUid!!.isNotEmpty()) "home" else "login"
                    navController.navigate(destination) { popUpTo("splash") { inclusive = true } }
                }
            }
        }
    }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator()
    }
}