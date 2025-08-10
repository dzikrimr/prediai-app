package com.example.prediai

import ProgressTrackingScreen
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument

@Composable
fun MainScreen() {
    val navController = rememberNavController()

    Scaffold(
        bottomBar = {
            val currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route
            if (currentRoute != "progress" && !(currentRoute?.startsWith("historyDetail/") ?: false)) {
                BottomNavigationBar(navController = navController)
            }
        }
    ) { paddingValues ->
        NavHost(
            navController = navController,
            startDestination = "home",
            modifier = Modifier.padding(paddingValues)
        ) {
            composable("home") { HomeScreen(navController = navController) }
            composable("history") { HistoryScreen(navController = navController) }
            composable("scan") { ScanDetectionScreen(navController = navController) }
            composable("chatbot") { ChatbotScreen(navController = navController) }
            composable("profile") { ProfileScreen() }
            composable("progress") { ProgressTrackingScreen(navController = navController) }
            composable(
                "historyDetail/{scanId}",
                arguments = listOf(navArgument("scanId") { type = NavType.IntType })
            ) { backStackEntry ->
                val scanId = backStackEntry.arguments?.getInt("scanId") ?: 0
                HistoryDetailScreen(scanId = scanId, navController = navController)
            }
        }
    }
}

@Composable
fun BottomNavigationBar(navController: NavController) {
    val items = listOf(
        NavItem("Beranda", Icons.Default.Home, "home"),
        NavItem("Riwayat", Icons.Default.History, "history"),
        NavItem("Scan", Icons.Default.CameraAlt, "scan"),
        NavItem("Chatbot", Icons.Default.Chat, "chatbot"),
        NavItem("Profil", Icons.Default.Person, "profile")
    )

    NavigationBar(
        containerColor = Color.White,
        contentColor = PrimaryColor
    ) {
        val currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route

        items.forEach { item ->
            NavigationBarItem(
                icon = {
                    if (item.route == "scan") { // Scan button
                        Box(
                            modifier = Modifier
                                .size(56.dp)
                                .background(PrimaryColor, CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = item.icon,
                                contentDescription = item.label,
                                tint = Color.White,
                                modifier = Modifier.size(24.dp)
                            )
                        }
                    } else {
                        Icon(
                            imageVector = item.icon,
                            contentDescription = item.label,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                },
                label = {
                    Text(
                        text = item.label,
                        fontSize = 10.sp
                    )
                },
                selected = currentRoute == item.route,
                onClick = {
                    navController.navigate(item.route) {
                        popUpTo(navController.graph.startDestinationId) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = PrimaryColor,
                    selectedTextColor = PrimaryColor,
                    unselectedIconColor = Color.Gray,
                    unselectedTextColor = Color.Gray,
                    indicatorColor = Color.Transparent
                )
            )
        }
    }
}

data class NavItem(
    val label: String,
    val icon: androidx.compose.ui.graphics.vector.ImageVector,
    val route: String
)