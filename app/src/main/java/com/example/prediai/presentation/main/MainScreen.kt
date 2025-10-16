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
import com.example.prediai.presentation.navigation.MainNavGraph

@Composable
fun MainScreen(rootNavController: NavHostController) {
    val mainNavController = rememberNavController()
    // DIUBAH: Mengambil route saat ini secara otomatis dari back stack
    val navBackStackEntry by mainNavController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    Scaffold(
        bottomBar = {
            BottomNavigationBar(
                // Gunakan route yang didapat dari back stack
                currentRoute = currentRoute ?: "beranda",
                onNavigate = { route ->
                    mainNavController.navigate(route) {
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
            MainNavGraph(
                mainNavController = mainNavController,
                // Teruskan rootNavController ke MainNavGraph
                rootNavController = rootNavController
            )
        }
    }
}