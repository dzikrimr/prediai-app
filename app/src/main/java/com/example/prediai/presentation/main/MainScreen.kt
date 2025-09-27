package com.example.prediai.presentation.main

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import androidx.navigation.compose.composable
import com.example.prediai.presentation.auth.LoginScreen
import com.example.prediai.presentation.onboarding.OnboardingScreen
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import com.example.prediai.presentation.auth.RegisterScreen

@Composable
fun MainScreen() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "onboarding") {
        composable("onboarding") {
            OnboardingScreen(
                navController = navController,
                onSkipClick = {
                    navController.navigate("auth") {
                        popUpTo("onboarding") { inclusive = true }
                    }
                },
                onFinishClick = {
                    navController.navigate("auth") {
                        popUpTo("onboarding") { inclusive = true }
                    }
                }
            )
        }

        composable("auth") {
            var email by remember { mutableStateOf("") }
            var password by remember { mutableStateOf("") }
            var isLoading by remember { mutableStateOf(false) }
            var errorMessage by remember { mutableStateOf<String?>(null) }

            LoginScreen(
                navController = navController,
                email = email,
                password = password,
                onEmailChange = { email = it },
                onPasswordChange = { password = it },
                onLoginClick = { /* TODO: login logic */ },
                isLoading = isLoading,
                errorMessage = errorMessage,
                onDismissError = { errorMessage = null },
                onRegisterClick = { navController.navigate("register") } // ✅ Navigasi ke register
            )
        }

        composable("register") {
            var email by remember { mutableStateOf("") }
            var password by remember { mutableStateOf("") }
            var confirmPassword by remember { mutableStateOf("") }
            var isLoading by remember { mutableStateOf(false) }
            var errorMessage by remember { mutableStateOf<String?>(null) }

            RegisterScreen(
                navController = navController,
                email = email,
                password = password,
                confirmPassword = confirmPassword,
                onEmailChange = { email = it },
                onPasswordChange = { password = it },
                onConfirmPasswordChange = { confirmPassword = it },
                onRegisterClick = { /* TODO: register logic */ },
                isLoading = isLoading,
                errorMessage = errorMessage,
                onDismissError = { errorMessage = null },
                onLoginClick = { navController.navigate("auth") { popUpTo("register") { inclusive = true } } } // Kembali ke login
            )
        }
    }
}
