package com.example.prediai.presentation.navigation

import ContactUsScreen
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.prediai.presentation.main.history.HistoryScreen
import com.example.prediai.presentation.main.HomeScreen
import com.example.prediai.presentation.labs.LabsScreen
import com.example.prediai.presentation.profile.ProfileScreen
import com.example.prediai.presentation.profile.about.AboutScreen
import com.example.prediai.presentation.profile.edit.EditProfileScreen
import com.example.prediai.presentation.profile.help.HelpCenterScreen
import com.example.prediai.presentation.profile.security.SecurityScreen
import com.example.prediai.presentation.scan.ScanScreen

@Composable
fun MainNavGraph(
    // DIUBAH: Terima `mainNavController` untuk membangun graph, dan `rootNavController` untuk diberikan ke screen
    mainNavController: NavHostController,
    rootNavController: NavHostController
) {
    NavHost(
        navController = mainNavController,
        startDestination = "beranda"
    ) {
        composable("beranda") {
            // PENTING: Berikan `rootNavController` ke HomeScreen
            HomeScreen(navController = rootNavController)
        }
        composable("riwayat") {
            HistoryScreen(navController = rootNavController)
        }
        composable("scan") {
            ScanScreen()
        }
        composable("labs") {
            LabsScreen()
        }
        composable("profil") {
            ProfileScreen(navController = rootNavController)
        }

        // Rute-rute ini adalah bagian dari "profil" dan seharusnya dipanggil dari sana,
        // jadi mereka tetap menggunakan `rootNavController`.
        composable("edit_profile") {
            EditProfileScreen(navController = rootNavController)
        }
        composable("security") {
            SecurityScreen(navController = rootNavController)
        }
        composable("help_center") {
            HelpCenterScreen(navController = rootNavController)
        }
        composable("contact_us") {
            ContactUsScreen(navController = rootNavController)
        }
        composable("about") {
            AboutScreen(navController = rootNavController)
        }
    }
}