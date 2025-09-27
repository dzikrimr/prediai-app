package com.example.prediai.presentation.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.prediai.presentation.auth.comps.LoginForm
import com.example.prediai.presentation.theme.PrediAITheme

/**
 * Layar login utama untuk autentikasi pengguna.
 *
 * @param navController NavController untuk navigasi ke layar lain (misalnya, ke main screen setelah login berhasil).
 */
@Composable
fun LoginScreen(
    navController: NavController,
    email: String,
    password: String,
    onEmailChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onLoginClick: () -> Unit,
    isLoading: Boolean,
    errorMessage: String?,
    onDismissError: () -> Unit,
    onRegisterClick: () -> Unit, // <-- Tambahkan
) {
    PrediAITheme {
        Scaffold { paddingValues ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentAlignment = Alignment.Center
            ) {
                LoginForm(
                    email = email,
                    password = password,
                    onEmailChange = onEmailChange,
                    onPasswordChange = onPasswordChange,
                    onLoginClick = onLoginClick,
                    isLoading = isLoading,
                    errorMessage = errorMessage,
                    onDismissError = onDismissError,
                    onForgotPasswordClick = { /* TODO */ },
                    onRegisterClick = onRegisterClick, // <-- Gunakan parameter
                    onGoogleSignInClick = { /* TODO */ }
                )
            }
        }
    }
}

@Preview()
@Composable
fun LoginScreenPreview() {
    val navController = rememberNavController()
    PrediAITheme { // ← Theme juga membungkus preview
        LoginScreen(
            navController = navController,
            email = "preview@example.com",
            password = "password",
            onEmailChange = {},
            onPasswordChange = {},
            onLoginClick = {},
            isLoading = false,
            errorMessage = null,
            onDismissError = {},
            onRegisterClick = {} // <-- Gunakan parameter
        )
    }
}