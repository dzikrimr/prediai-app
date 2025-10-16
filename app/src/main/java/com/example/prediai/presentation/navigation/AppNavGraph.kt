package com.example.prediai.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.prediai.presentation.auth.LoginScreen
import com.example.prediai.presentation.auth.QuestionnaireRoot
import com.example.prediai.presentation.auth.RegisterScreen
import com.example.prediai.presentation.main.MainScreen // Import MainScreen
import com.example.prediai.presentation.main.education.EducationListScreen
import com.example.prediai.presentation.main.education.EducationViewModel
import com.example.prediai.presentation.main.education.VideoDetailScreen
import com.example.prediai.presentation.main.notification.NotificationScreen
import com.example.prediai.presentation.onboarding.OnboardingScreen
import com.example.prediai.presentation.onboarding.OnboardingViewModel
import com.example.prediai.presentation.splash.SplashScreen

@Composable
fun AppNavGraph(navController: NavHostController) {
    val educationViewModel: EducationViewModel = hiltViewModel()
    val educationUiState by educationViewModel.uiState.collectAsState()

    NavHost(
        navController = navController,
        startDestination = "splash"
    ) {
        // Alur Pra-Login (tidak berubah)
        composable("splash") { SplashScreen(navController = navController) }
        composable("onboarding") {
            val viewModel: OnboardingViewModel = hiltViewModel()
            OnboardingScreen(
                navController = navController,
                onSkipClick = {
                    viewModel.completeOnboarding()
                    navController.navigate("login") { popUpTo("onboarding") { inclusive = true } }
                },
                onFinishClick = {
                    viewModel.completeOnboarding()
                    navController.navigate("login") { popUpTo("onboarding") { inclusive = true } }
                }
            )
        }
        composable("login") { LoginScreen(navController) }
        composable("register") { RegisterScreen(navController) }
        composable("questionnaire") { QuestionnaireRoot(navController) }

        // DIUBAH: Semua layar utama sekarang berada di dalam satu rute "main"
        composable("main") {
            MainScreen(rootNavController = navController)
        }

        // Rute-rute yang bisa diakses dari mana saja (di luar bottom nav)
        composable("notification") {
            NotificationScreen(navController = navController)
        }
        composable("education_list") {
            EducationListScreen(navController = navController, viewModel = educationViewModel)
        }
        composable("video_detail/{videoId}") { backStackEntry ->
            val videoId = backStackEntry.arguments?.getString("videoId")
            val video = educationUiState.videos.find { it.id == videoId }
            VideoDetailScreen(navController = navController, video = video)
        }
    }
}