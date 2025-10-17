package com.example.prediai.presentation.navigation

import ContactUsScreen
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.prediai.presentation.auth.LoginScreen
import com.example.prediai.presentation.auth.QuestionnaireRoot
import com.example.prediai.presentation.auth.RegisterScreen
import com.example.prediai.presentation.doctor.DoctorScreen
import com.example.prediai.presentation.labs.LabResultScreen
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
import java.net.URLDecoder
import java.nio.charset.StandardCharsets

@Composable
fun AppNavGraph(navController: NavHostController) {
    val educationViewModel: EducationViewModel = hiltViewModel()
    val educationUiState by educationViewModel.uiState.collectAsState()

    NavHost(
        navController = navController,
        startDestination = "splash"
    ) {
        // Alur Pra-Login
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

        // Alur Utama Setelah Login
        composable("main") {
            MainScreen(rootNavController = navController)
        }

        // Rute-rute lain di luar alur utama (tidak punya bottom nav bar)
        composable("notification") {
            NotificationScreen(navController = navController)
        }
        composable("schedule") {
            ScheduleScreen(navController = navController)
        }

        // BARU: Menambahkan rute untuk ChatbotScreen
        composable("chatbot") {
            ChatbotScreen(navController = navController)
        }

        composable("education_list") {
            EducationListScreen(navController = navController, viewModel = educationViewModel)
        }
        composable("video_detail/{videoId}") { backStackEntry ->
            val videoId = backStackEntry.arguments?.getString("videoId")
            val video = educationUiState.videos.find { it.id == videoId }
            VideoDetailScreen(navController = navController, video = video)
        }

        // Rute-rute dari MainNavGraph yang perlu diakses oleh rootNavController
        composable("doctor") {
            DoctorScreen(navController = navController)
        }
        composable("quistionere") {
            QuistionereScreen(
                onBackClick = { navController.popBackStack() },
                onCancelClick = { navController.popBackStack() },
                onSaveClick = { answers ->
                    navController.popBackStack()
                }
            )
        }
        composable(
            route = "lab_result/{fileName}/{uploadDate}",
            arguments = listOf(
                navArgument("fileName") { type = NavType.StringType },
                navArgument("uploadDate") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val fileName = backStackEntry.arguments?.getString("fileName") ?: "N/A"
            val uploadDate = backStackEntry.arguments?.getString("uploadDate") ?: "N/A"
            LabResultScreen(
                fileName = URLDecoder.decode(fileName, StandardCharsets.UTF_8.toString()),
                uploadDate = URLDecoder.decode(uploadDate, StandardCharsets.UTF_8.toString()),
                onBackClick = { navController.popBackStack() }
            )
        }
        composable("edit_profile") { EditProfileScreen(navController = navController) }
        composable("security") { SecurityScreen(navController = navController) }
        composable("help_center") { HelpCenterScreen(navController = navController) }
        composable("contact_us") { ContactUsScreen(navController = navController) }
        composable("about") { AboutScreen(navController = navController) }
    }
}