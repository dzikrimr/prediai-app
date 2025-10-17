package com.example.prediai.presentation.navigation

import ContactUsScreen
import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.hilt.navigation.compose.hiltViewModel
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
import com.example.prediai.presentation.main.progress.ProgressScreen
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
    mainNavController: NavHostController,
    rootNavController: NavHostController,
    onUpdateRoute: (String) -> Unit
) {

    NavHost(
        navController = mainNavController,
        startDestination = "beranda"
    ) {
        composable("beranda") {
            onUpdateRoute("beranda")
            HomeScreen(mainNavController, rootNavController)
        }
        composable("doctor") {
            // DIUBAH: Berikan mainNavController agar bisa kembali ke HomeScreen
            DoctorScreen(mainNavController)
        }
        composable("riwayat") {
            onUpdateRoute("riwayat")
            // DIUBAH: Berikan mainNavController agar bisa kembali ke HomeScreen
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
                onUpdateRoute("scan")
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
                            mainNavController.navigate("scan_result")
                        }
                    }
                )
            }
            composable("scan_result") { backStackEntry ->
                val parentEntry = remember(backStackEntry) {
                    mainNavController.getBackStackEntry("scan_flow")
                }
                val scanResultViewModel: ScanResultViewModel = hiltViewModel(parentEntry)

                ScanResultScreen(
                    viewModel = scanResultViewModel,
                    onBackClick = { mainNavController.popBackStack() }
                )
            }
        }

        composable("quistionere") {
            QuistionereScreen(
                onBackClick = { rootNavController.popBackStack() },
                onCancelClick = { rootNavController.popBackStack() },
                onSaveClick = { answers ->
                    Log.d("Quistionere", "Jawaban disimpan: $answers")
                    rootNavController.popBackStack()
                }
            )
        }

        composable("labs") {
            onUpdateRoute("labs")
            LabsScreen(
                onBackClick = { mainNavController.popBackStack() },
                onNavigateToResult = { fileName, uploadDate ->
                    val encodedFileName = URLEncoder.encode(fileName, StandardCharsets.UTF_8.toString())
                    val encodedDate = URLEncoder.encode(uploadDate, StandardCharsets.UTF_8.toString())
                    rootNavController.navigate("lab_result/$encodedFileName/$encodedDate")
                }
            )
        }
        composable("profil") {
            onUpdateRoute("profil")
            ProfileScreen(navController = rootNavController)
        }

        // Rute-rute ini adalah bagian dari "profil" dan harusnya dipanggil dari sana,
        // jadi mereka tetap menggunakan rootNavController.
        composable("edit_profile") { EditProfileScreen(navController = rootNavController) }
        composable("security") { SecurityScreen(navController = rootNavController) }
        composable("help_center") { HelpCenterScreen(navController = rootNavController) }
        composable("contact_us") { ContactUsScreen(navController = rootNavController) }
        composable("about") { AboutScreen(navController = rootNavController) }
    }
}