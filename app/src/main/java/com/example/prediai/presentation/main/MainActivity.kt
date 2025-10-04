package com.example.prediai.presentation.main

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.WindowCompat
import com.example.prediai.presentation.theme.PrediAITheme

val PrimaryColor = Color(0xFF00B4A3)
val SecondaryColor = Color(0xFF4CAF50)
val BackgroundColor = Color(0xFFFFFFFF)
val WarningColor = Color(0xFFFFC107)
val DangerColor = Color(0xFFFF5722)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()

        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        WindowCompat.setDecorFitsSystemWindows(window, false)

        setContent {
            PrediAITheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    MainScreen() // ✅ NavHost akan mulai dari onboarding
                }
            }
        }
    }
}