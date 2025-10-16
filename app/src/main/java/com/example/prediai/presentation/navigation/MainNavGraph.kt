package com.example.prediai.presentation.navigation

import ContactUsScreen
import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.navArgument
import com.example.prediai.presentation.doctor.DoctorScreen
import com.example.prediai.presentation.guide.GuideScreen
import com.example.prediai.presentation.labs.LabResultScreen
import com.example.prediai.presentation.main.history.HistoryScreen
import com.example.prediai.presentation.main.HomeScreen
import com.example.prediai.presentation.labs.LabsScreen
import com.example.prediai.presentation.profile.ProfileScreen
import com.example.prediai.presentation.profile.about.AboutScreen
import com.example.prediai.presentation.profile.edit.EditProfileScreen
import com.example.prediai.presentation.profile.help.HelpCenterScreen
import com.example.prediai.presentation.profile.security.SecurityScreen
import com.example.prediai.presentation.quistionere.QuistionereScreen
import com.example.prediai.presentation.scan.ScanResultScreen
import com.example.prediai.presentation.scan.ScanResultViewModel
import com.example.prediai.presentation.scan.ScanScreen
import com.example.prediai.presentation.scan.ScanViewModel
import java.net.URLDecoder
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

@Composable
fun MainNavGraph(
    navController: NavHostController,
    onUpdateRoute: (String) -> Unit
) {

    NavHost(
        navController = navController,
        startDestination = "beranda"
    ) {
        composable("beranda") {
            onUpdateRoute("beranda")
            HomeScreen(navController)
        }
        composable("doctor") {
            DoctorScreen(navController)
        }
        composable("riwayat") {
            onUpdateRoute("riwayat")
            HistoryScreen(navController)
        }

        navigation(
            startDestination = "guide",
            route = "scan_flow"
        ) {
            composable("guide") {
                onUpdateRoute("scan")
                GuideScreen(
                    onBackClick = { navController.popBackStack() },
                    onContinueClick = { navController.navigate("scan") },
                    onChangeQuestionnaireClick = { navController.navigate("quistionere") }
                )
            }

            composable("scan") { backStackEntry ->
                val parentEntry = remember(backStackEntry) {
                    navController.getBackStackEntry("scan_flow")
                }
                val scanResultViewModel: ScanResultViewModel = hiltViewModel(parentEntry)
                val scanViewModel: ScanViewModel = hiltViewModel()

                ScanScreen(
                    viewModel = scanViewModel,
                    onBackClick = { navController.popBackStack() },
                    onContinueClick = { analysisResult, nailPhoto, tonguePhoto ->
                        if (nailPhoto != null && tonguePhoto != null) {
                            scanResultViewModel.setData(analysisResult, nailPhoto, tonguePhoto)
                            navController.navigate("scan_result")
                        }
                    }
                )
            }

            composable("scan_result") { backStackEntry ->
                val parentEntry = remember(backStackEntry) {
                    navController.getBackStackEntry("scan_flow")
                }
                val scanResultViewModel: ScanResultViewModel = hiltViewModel(parentEntry)

                ScanResultScreen(
                    viewModel = scanResultViewModel,
                    onBackClick = { navController.popBackStack() }
                )
            }
        }

        composable("quistionere") {
            QuistionereScreen(
                onBackClick = { navController.popBackStack() },
                onCancelClick = { navController.popBackStack() },
                onSaveClick = { answers ->
                    Log.d("Quistionere", "Jawaban disimpan: $answers")
                    navController.popBackStack()
                }
            )
        }

        composable("labs") {
            onUpdateRoute("labs")
            LabsScreen(
                onBackClick = { navController.popBackStack() },
                onNavigateToResult = { fileName, uploadDate ->
                    val encodedFileName = URLEncoder.encode(fileName, StandardCharsets.UTF_8.toString())
                    val encodedDate = URLEncoder.encode(uploadDate, StandardCharsets.UTF_8.toString())
                    navController.navigate("lab_result/$encodedFileName/$encodedDate")
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

            val decodedFileName = URLDecoder.decode(fileName, StandardCharsets.UTF_8.toString())
            val decodedDate = URLDecoder.decode(uploadDate, StandardCharsets.UTF_8.toString())

            LabResultScreen(
                fileName = decodedFileName,
                uploadDate = decodedDate,
                onBackClick = { navController.popBackStack() }
            )
        }
        composable("profil") {
            onUpdateRoute("profil")
            ProfileScreen(navController = navController)
        }
        composable("edit_profile") {
            EditProfileScreen(navController = navController)
        }
        composable("security") {
            SecurityScreen(navController = navController)
        }
        composable("help_center") {
            HelpCenterScreen(navController = navController)
        }
        composable("contact_us") {
            ContactUsScreen(navController = navController)
        }
        composable("about") {
            AboutScreen(navController = navController)
        }
    }
}