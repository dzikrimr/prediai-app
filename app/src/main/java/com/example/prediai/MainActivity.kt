package com.example.prediai

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.ui.graphics.Color
import com.example.prediai.ui.theme.PrediAITheme

// Define colors
val PrimaryColor = Color(0xFF00B4A3)
val SecondaryColor = Color(0xFF4CAF50)
val BackgroundColor = Color(0xFFF5F5F5)
val WarningColor = Color(0xFFFFC107)
val DangerColor = Color(0xFFFF5722)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PrediAITheme {
                MainScreen()
            }
        }
    }
}