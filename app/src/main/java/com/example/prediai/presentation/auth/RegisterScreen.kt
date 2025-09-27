package com.example.prediai.presentation.auth

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.prediai.presentation.auth.comps.RegisterForm
import com.example.prediai.presentation.theme.PrediAITheme

@Composable
fun RegisterScreen(
    navController: NavController,
    email: String,
    password: String,
    confirmPassword: String,
    onEmailChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onConfirmPasswordChange: (String) -> Unit,
    onRegisterClick: () -> Unit,
    isLoading: Boolean,
    errorMessage: String?,
    onDismissError: () -> Unit,
    onLoginClick: () -> Unit,
) {
    PrediAITheme {
        Scaffold { paddingValues ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentAlignment = Alignment.Center
            ) {
                RegisterForm(
                    email = email,
                    password = password,
                    confirmPassword = confirmPassword,
                    onEmailChange = onEmailChange,
                    onPasswordChange = onPasswordChange,
                    onConfirmPasswordChange = onConfirmPasswordChange,
                    onRegisterClick = onRegisterClick,
                    isLoading = isLoading,
                    errorMessage = errorMessage,
                    onDismissError = onDismissError,
                    onForgotPasswordClick = { /* TODO */ },
                    onGoogleSignInClick = { /* TODO */ },
                    onLoginClick = onLoginClick
                )
            }
        }
    }
}

@Preview()
@Composable
fun RegisterScreenPreview() {
    val navController = rememberNavController()
    PrediAITheme {
        RegisterScreen(
            navController = navController,
            email = "",
            password = "",
            confirmPassword = "",
            onEmailChange = {},
            onPasswordChange = {},
            onConfirmPasswordChange = {},
            onRegisterClick = {},
            isLoading = false,
            errorMessage = null,
            onDismissError = {},
            onLoginClick = {}
        )
    }
}
