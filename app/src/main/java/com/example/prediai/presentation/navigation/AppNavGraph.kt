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
import com.example.prediai.presentation.main.HomeScreen
import com.example.prediai.presentation.main.education.EducationListScreen
import com.example.prediai.presentation.main.education.EducationViewModel
import com.example.prediai.presentation.main.education.VideoDetailScreen
import com.example.prediai.presentation.main.history.HistoryScreen
import com.example.prediai.presentation.main.notification.NotificationScreen
import com.example.prediai.presentation.main.progress.ProgressScreen
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
        composable("splash") {
            SplashScreen(navController = navController)
        }

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

        composable("home") {
            HomeScreen(navController = navController)
        }
        composable("progress") {
            ProgressScreen(navController = navController)
        }
        composable("history") {
            HistoryScreen(navController = navController)
        }

        // BARU: Menambahkan rute untuk fitur Notifikasi
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