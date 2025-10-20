package com.example.prediai.presentation.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.material3.LocalTextStyle
import androidx.compose.ui.text.TextStyle

private val DarkColorScheme = darkColorScheme(
    primary = Purple80,
    secondary = PurpleGrey80,
    tertiary = Pink80
)

private val LightColorScheme = lightColorScheme(
    primary = Color(0xFF00B4A3),      // PrimaryColor
    secondary = Color(0xFF4CAF50),    // SecondaryColor
    background = Color(0xFFFFFFFF),   // BackgroundColor
    error = Color(0xFFFF5722),        // DangerColor
)

@Composable
fun PrediAITheme(
    // Nilai ini diabaikan dan diatur ke false di dalam fungsi untuk menolak tema gelap
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    // Tetapkan `useDarkTheme` secara eksplisit ke false
    val useDarkTheme = false

    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            // Gunakan skema warna terang dinamis
            dynamicLightColorScheme(context)
        }
        // Selalu gunakan LightColorScheme karena useDarkTheme selalu false
        useDarkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography
    ) {
        CompositionLocalProvider(
            LocalTextStyle provides MaterialTheme.typography.bodyLarge.merge(
                TextStyle(fontFamily = PoppinsFontFamily)
            )
        ) {
            content()
        }
    }
}