package com.example.prediai.presentation.main

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.WindowCompat
import androidx.navigation.compose.rememberNavController
import com.example.prediai.presentation.navigation.AppNavGraph
import com.example.prediai.presentation.theme.PrediAITheme
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import dagger.hilt.android.AndroidEntryPoint

val PrimaryColor = Color(0xFF00B4A3)
val SecondaryColor = Color(0xFF4CAF50)
val BackgroundColor = Color(0xFFFFFFFF)
val WarningColor = Color(0xFFFFC107)
val DangerColor = Color(0xFFFF5722)

// Buat Local Composition untuk akses GoogleSignInClient & Launcher di seluruh screen
val LocalGoogleSignInClient = staticCompositionLocalOf<GoogleSignInClient> {
    error("GoogleSignInClient not provided")
}

val LocalGoogleSignInLauncher = staticCompositionLocalOf<((Intent) -> Unit)> {
    error("GoogleSignInLauncher not provided")
}

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        WindowCompat.setDecorFitsSystemWindows(window, false)

        // --- Inisialisasi Google Sign In ---
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(com.example.prediai.R.string.default_web_client_id)) // pastikan string ini sudah ada
            .requestEmail()
            .build()

        val googleSignInClient = GoogleSignIn.getClient(this, gso)

        setContent {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()

                    // Launcher untuk menangani hasil Google Sign In
                    val googleSignInLauncher = rememberLauncherForActivityResult(
                        contract = ActivityResultContracts.StartActivityForResult()
                    ) { result ->
                        val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
                        try {
                            val account = task.getResult(ApiException::class.java)
                            // ✅ Berhasil login Google
                            // Nanti proses Firebase Auth bisa dilakukan di AuthViewModel (signInWithCredential)
                        } catch (e: ApiException) {
                            e.printStackTrace()
                        }
                    }

                    // Kirim ke semua screen pakai CompositionLocalProvider
                    CompositionLocalProvider(
                        LocalGoogleSignInClient provides googleSignInClient,
                        LocalGoogleSignInLauncher provides { intent ->
                            googleSignInLauncher.launch(intent)
                        }
                    ) {
                        AppNavGraph(navController = navController)
                    }
                }
        }
    }
}
