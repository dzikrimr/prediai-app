package com.example.prediai.presentation.splash

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.filter

@Composable
fun SplashScreen(
    navController: NavController,
    viewModel: SplashViewModel = hiltViewModel()
) {
    val isOnboardingCompleted by viewModel.getOnboardingState().collectAsState(initial = null)
    val userUid by viewModel.getCachedUserUid().collectAsState(initial = null)

    // ✅ Navigasi aman setelah data siap & layout attach
    LaunchedEffect(Unit) {
        snapshotFlow { isOnboardingCompleted to userUid }
            .filter { (onboarding, _) -> onboarding != null } // tunggu data siap
            .collect { (onboarding, uid) ->
                delay(300) // beri waktu untuk transisi halus

                if (onboarding == false) {
                    // Belum pernah onboarding → ke onboarding
                    navController.navigate("main") {
                        popUpTo("splash") { inclusive = true }
                    }
                } else {
                    // Sudah onboarding → cek apakah user login
                    if (uid.isNullOrEmpty()) {
                        // User belum login → tetap arahkan ke onboarding (bukan login)
                        navController.navigate("main") {
                            popUpTo("splash") { inclusive = true }
                        }
                    } else {
                        // User sudah login → ke home
                        navController.navigate("main") {
                            popUpTo("splash") { inclusive = true }
                        }
                    }
                }
            }
    }
}
