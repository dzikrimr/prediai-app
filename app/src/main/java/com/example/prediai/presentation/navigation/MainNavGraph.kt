package com.example.prediai.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.prediai.presentation.main.history.HistoryScreen
import com.example.prediai.presentation.main.HomeScreen
import com.example.prediai.presentation.labs.LabsScreen
import com.example.prediai.presentation.profile.ProfileScreen
import com.example.prediai.presentation.scan.ScanScreen

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
        composable("riwayat") {
            onUpdateRoute("riwayat")
            HistoryScreen(navController)
        }
        composable("scan") {
            onUpdateRoute("scan")
            ScanScreen()
        }
        composable("labs") {
            onUpdateRoute("labs")
            LabsScreen()
        }
        composable("profil") {
            onUpdateRoute("profil")
            ProfileScreen()
        }
    }
}
