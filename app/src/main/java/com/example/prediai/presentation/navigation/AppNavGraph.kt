package com.example.prediai.presentation.navigation

import ContactUsScreen
import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.prediai.presentation.auth.AuthViewModel
import com.example.prediai.presentation.auth.LoginScreen
import com.example.prediai.presentation.auth.QuestionnaireScreen
import com.example.prediai.presentation.auth.RegisterScreen
import com.example.prediai.presentation.auth.RegistrationSuccessScreen
import com.example.prediai.presentation.doctor.DoctorScreen
import com.example.prediai.presentation.main.MainScreen
import com.example.prediai.presentation.main.chatbot.ChatbotScreen // IMPORT INI
import com.example.prediai.presentation.main.education.EducationListScreen
import com.example.prediai.presentation.main.education.EducationViewModel
import com.example.prediai.presentation.main.education.VideoDetailScreen
import com.example.prediai.presentation.main.notification.NotificationScreen
import com.example.prediai.presentation.main.schedule.ScheduleScreen
import com.example.prediai.presentation.onboarding.OnboardingScreen
import com.example.prediai.presentation.onboarding.OnboardingViewModel
import com.example.prediai.presentation.profile.about.AboutScreen
import com.example.prediai.presentation.profile.edit.EditProfileScreen
import com.example.prediai.presentation.profile.help.HelpCenterScreen
import com.example.prediai.presentation.profile.security.SecurityScreen
import com.example.prediai.presentation.quistionere.QuistionereScreen
import com.example.prediai.presentation.splash.SplashScreen

@Composable
fun AppNavGraph(navController: NavHostController) {
    val educationViewModel: EducationViewModel = hiltViewModel()
    val educationUiState by educationViewModel.uiState.collectAsState()

    NavHost(
        navController = navController,
        startDestination = "splash"
    ) {
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

        composable("questionnaire") {
            val viewModel: AuthViewModel = hiltViewModel()
            val state by viewModel.uiState.collectAsState()
            val step by viewModel.step.collectAsState()

            LaunchedEffect(key1 = state.isQuestionnaireSuccess) {
                if (state.isQuestionnaireSuccess) {
                    navController.navigate("registration_success") {
                        popUpTo("questionnaire") { inclusive = true }
                    }
                }
            }

            QuestionnaireScreen(
                step = step,           // Now 'step' is an Int
                state = state,         // And 'state' is an AuthUiState
                onNameChange = viewModel::onNameChange,
                onBirthDateChange = viewModel::onBirthDateChange,
                onHeightChange = viewModel::onHeightChange,
                onWeightChange = viewModel::onWeightChange,
                onCityChange = viewModel::onCityChange,
                onStep2Answer = viewModel::onStep2Answer,
                onStep3Answer = viewModel::onStep3Answer,
                onStep4Answer = viewModel::onStep4Answer,
                onNextClick = viewModel::onNextClick
            )
        }

        composable("registration_success") {
            RegistrationSuccessScreen(
                onStartClick = {
                    navController.navigate("main") {
                        popUpTo(navController.graph.findStartDestination().id) {
                            inclusive = true
                        }
                    }
                }
            )
        }

        composable("main") {
            MainScreen(rootNavController = navController)
        }

        composable("notification") {
            NotificationScreen(navController = navController)
        }
        composable("schedule") {
            ScheduleScreen(navController = navController)
        }

        composable("chatbot") {
            ChatbotScreen(navController = navController)
        }

        composable("education_list") {
            EducationListScreen(navController = navController, viewModel = educationViewModel)
        }

        composable(
            route = "video_detail" // Hapus argumen {videoId} dari route
        ) {
            VideoDetailScreen(
                navController = navController,
                viewModel = educationViewModel // Berikan instance ViewModel yang sama
            )
        }

        composable("doctor") {
            DoctorScreen(navController = navController)
        }

        composable("quistionere") {
            QuistionereScreen(
                onBackClick = { navController.popBackStack() },
                onCancelClick = { navController.popBackStack() },
                onSaveClick = {
                    Log.d("Quistionere", "Jawaban berhasil disimpan, kembali ke halaman sebelumnya.")
                    navController.popBackStack()
                }
            )
        }

        composable("edit_profile") { EditProfileScreen(navController = navController) }
        composable("security") { SecurityScreen(navController = navController) }
        composable("help_center") { HelpCenterScreen(navController = navController) }
        composable("contact_us") { ContactUsScreen(navController = navController) }
        composable("about") { AboutScreen(navController = navController) }
    }
}