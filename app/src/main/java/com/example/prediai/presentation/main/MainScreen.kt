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
fun MainScreen(rootNavController: NavHostController) {
    val mainNavController = rememberNavController()

    val navBackStackEntry by mainNavController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    val bottomNavRoutes = listOf(
        BottomNavItem.BERANDA.route,
        BottomNavItem.RIWAYAT.route,
        BottomNavItem.LABS.route,
        "labs_upload",
        BottomNavItem.PROFIL.route,
        BottomNavItem.GUIDE.route
    )

    val isBottomNavVisible = currentRoute in bottomNavRoutes

    Scaffold(
        bottomBar = {
            if (isBottomNavVisible) {
                BottomNavigationBar(
                    currentRoute = when (currentRoute) {
                        "scan", "scan_result" -> "guide"
                        else -> currentRoute ?: BottomNavItem.BERANDA.route
                    },
                    onNavigate = { route ->
                        val destination = if (route == BottomNavItem.GUIDE.route) {
                            "scan_flow"
                        } else {
                            route
                        }
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
        }
    ) { innerPadding ->
        Box(modifier = Modifier.padding(bottom = innerPadding.calculateBottomPadding())) {
            MainNavGraph(
                mainNavController = mainNavController,
                rootNavController = rootNavController,
            )
        }
    }
}