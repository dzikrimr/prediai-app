package com.example.prediai.presentation.navigation

import ContactUsScreen
import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.navArgument
import com.example.prediai.presentation.auth.AuthViewModel
import com.example.prediai.presentation.auth.QuestionnaireScreen
import com.example.prediai.presentation.doctor.DoctorScreen
import com.example.prediai.presentation.guide.GuideScreen
import com.example.prediai.presentation.labs.LabResultScreen
import com.example.prediai.presentation.main.history.HistoryScreen
import com.example.prediai.presentation.main.HomeScreen
import com.example.prediai.presentation.labs.LabsScreen
import com.example.prediai.presentation.labs.LabsViewModel
import com.example.prediai.presentation.main.progress.ProgressScreen
import com.example.prediai.presentation.profile.ProfileScreen
import com.example.prediai.presentation.profile.about.AboutScreen
import com.example.prediai.presentation.profile.edit.EditProfileScreen
import com.example.prediai.presentation.profile.help.HelpCenterScreen
import com.example.prediai.presentation.profile.security.SecurityScreen
import com.example.prediai.presentation.scan.ScanResultScreen
import com.example.prediai.presentation.scan.ScanResultViewModel
import com.example.prediai.presentation.scan.ScanScreen
import com.example.prediai.presentation.scan.ScanViewModel
import java.net.URLDecoder
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

@Composable
fun MainNavGraph(
    mainNavController: NavHostController,
    rootNavController: NavHostController,
) {

    NavHost(
        navController = mainNavController,
        startDestination = "beranda"
    ) {
        composable("beranda") {
            HomeScreen(mainNavController, rootNavController)
        }
        composable("doctor") {
            DoctorScreen(mainNavController)
        }
        composable("riwayat") {
            ProgressScreen(mainNavController)
        }
        composable("history_detail") {
            HistoryScreen(mainNavController)
        }

        navigation(
            startDestination = "guide",
            route = "scan_flow"
        ) {
            composable("guide") {
                GuideScreen(
                    onBackClick = { mainNavController.popBackStack() },
                    onContinueClick = { mainNavController.navigate("scan") },
                    onChangeQuestionnaireClick = { rootNavController.navigate("quistionere") }
                )
            }
            composable("scan") { backStackEntry ->
                val parentEntry = remember(backStackEntry) {
                    mainNavController.getBackStackEntry("scan_flow")
                }
                val scanResultViewModel: ScanResultViewModel = hiltViewModel(parentEntry)
                val scanViewModel: ScanViewModel = hiltViewModel()

                ScanScreen(
                    viewModel = scanViewModel,
                    onBackClick = { mainNavController.popBackStack() },
                    onContinueClick = { analysisResult, nailPhoto, tonguePhoto ->
                        if (nailPhoto != null && tonguePhoto != null) {
                            scanResultViewModel.setData(analysisResult, nailPhoto, tonguePhoto)
                            mainNavController.navigate("scan_result") {
                                popUpTo("scan") { inclusive = true }
                            }
                        }
                    }
                )
            }
            composable(
                route = "scan_result?historyId={historyId}",
                arguments = listOf(navArgument("historyId") {
                    type = NavType.StringType
                    nullable = true
                })
            ) { backStackEntry ->
                // --- LOGIKA BARU UNTUK MEMILIH VIEWMODEL YANG TEPAT ---
                val historyId = backStackEntry.arguments?.getString("historyId")

                val scanResultViewModel: ScanResultViewModel = if (historyId == null) {
                    // Mode Scan Baru: Ambil ViewModel yang sama dari "scan_flow" (Papan Tulis A)
                    val parentEntry = remember(backStackEntry) {
                        mainNavController.getBackStackEntry("scan_flow")
                    }
                    hiltViewModel(parentEntry)
                } else {
                    // Mode Lihat Riwayat: Buat ViewModel baru yang akan memuat data dari Firestore
                    hiltViewModel()
                }

                ScanResultScreen(
                    viewModel = scanResultViewModel,
                    onBackClick = { mainNavController.popBackStack() }
                )
            }
        }

        navigation(
            startDestination = "labs_upload",
            route = "labs"
        ) {
            composable("labs_upload") { backStackEntry ->
                // Buat ViewModel yang akan dibagikan dalam alur "labs"
                val parentEntry = remember(backStackEntry) {
                    mainNavController.getBackStackEntry("labs")
                }
                val labsViewModel: LabsViewModel = hiltViewModel(parentEntry)

                LabsScreen(
                    viewModel = labsViewModel,
                    onBackClick = { mainNavController.popBackStack() },
                    onNavigateToResult = { fileName, uploadDate ->
                        val encodedFileName = URLEncoder.encode(fileName, StandardCharsets.UTF_8.toString())
                        val encodedDate = URLEncoder.encode(uploadDate, StandardCharsets.UTF_8.toString())
                        mainNavController.navigate("lab_result/$encodedFileName/$encodedDate")
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
                val parentEntry = remember(backStackEntry) {
                    mainNavController.getBackStackEntry("labs")
                }
                val labsViewModel: LabsViewModel = hiltViewModel(parentEntry)

                val fileName = backStackEntry.arguments?.getString("fileName") ?: "N/A"
                val uploadDate = backStackEntry.arguments?.getString("uploadDate") ?: "N/A"
                val decodedFileName = URLDecoder.decode(fileName, StandardCharsets.UTF_8.toString())
                val decodedDate = URLDecoder.decode(uploadDate, StandardCharsets.UTF_8.toString())

                LabResultScreen(
                    fileName = decodedFileName,
                    uploadDate = decodedDate,
                    navController = mainNavController,
                    viewModel = labsViewModel
                )
            }
        }

        composable("profil") {
            ProfileScreen(navController = rootNavController)
        }

        composable("edit_profile") { EditProfileScreen(navController = rootNavController) }
        composable("security") { SecurityScreen(navController = rootNavController) }
        composable("help_center") { HelpCenterScreen(navController = rootNavController) }
        composable("contact_us") { ContactUsScreen(navController = rootNavController) }
        composable("about") { AboutScreen(navController = rootNavController) }
    }
}