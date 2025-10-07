package com.example.prediai.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.prediai.presentation.auth.LoginScreen
import com.example.prediai.presentation.auth.QuestionnaireRoot
import com.example.prediai.presentation.auth.RegisterScreen
import com.example.prediai.presentation.main.HomeScreen
import com.example.prediai.presentation.onboarding.OnboardingScreen
import com.example.prediai.presentation.onboarding.OnboardingViewModel
import com.example.prediai.presentation.splash.SplashScreen

@Composable
fun AppNavGraph(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = "splash"
    ) {
        composable("splash") {
            SplashScreen(navController = navController)
        }

        composable("onboarding") {
            // Dapatkan instance ViewModel
            val viewModel: OnboardingViewModel = hiltViewModel()

            OnboardingScreen(
                navController = navController,
                // Berikan logika untuk onSkipClick
                onSkipClick = {
                    viewModel.completeOnboarding() // Simpan status
                    navController.navigate("login") { // Navigasi
                        popUpTo("onboarding") { inclusive = true }
                    }
                },
                // Berikan logika untuk onFinishClick
                onFinishClick = {
                    viewModel.completeOnboarding() // Simpan status
                    navController.navigate("login") { // Navigasi
                        popUpTo("onboarding") { inclusive = true }
                    }
                }
            )
        }

        composable("login") {
            LoginScreen(navController)
        }

        composable("register") {
            RegisterScreen(navController)
        }

        composable("questionnaire") {
            QuestionnaireRoot(navController)
        }

        composable("home") {
            HomeScreen(navController)
        }
    }
}