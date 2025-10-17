package com.example.prediai.presentation.main

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.prediai.presentation.common.BottomNavigationBar
import com.example.prediai.presentation.common.BottomNavItem
import com.example.prediai.presentation.navigation.MainNavGraph

@Composable
fun MainScreen(rootNavController: NavHostController) { // Menerima "Peta Dunia"
    val mainNavController = rememberNavController() // Membuat "Peta Kota"

    // Secara otomatis mendeteksi rute saat ini di "Peta Kota"
    val navBackStackEntry by mainNavController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    Scaffold(
        bottomBar = {
            BottomNavigationBar(
                currentRoute = currentRoute ?: "beranda",
                onNavigate = { route ->
                    val destination = if (route == BottomNavItem.GUIDE.route) {
                        "scan_flow"
                    } else {
                        route
                    }
                    // Navigasi di bottom bar HANYA menggunakan "Peta Kota"
                    mainNavController.navigate(destination) {
                        popUpTo(mainNavController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )
        }
    ) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding)) {
            // Berikan KEDUA peta ke MainNavGraph
            MainNavGraph(
                mainNavController = mainNavController,
                rootNavController = rootNavController,
                onUpdateRoute = { newRoute ->
                    // `onUpdateRoute` masih diperlukan oleh MainNavGraph Anda
                    // untuk menandai tab "scan" saat berada di dalam alur scan
                }
            )
        }
    }
}