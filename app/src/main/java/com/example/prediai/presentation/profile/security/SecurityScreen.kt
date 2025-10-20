package com.example.prediai.presentation.profile.security

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.prediai.R
import com.example.prediai.presentation.common.TopBar
import android.Manifest
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat

// Fungsi pembantu untuk membuka halaman pengaturan aplikasi
fun openAppSettings(context: Context) {
    val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
        data = Uri.fromParts("package", context.packageName, null)
    }
    context.startActivity(intent)
}

// Fungsi Baru: Cek Izin Kamera
fun isCameraPermissionGranted(context: Context): Boolean {
    return ContextCompat.checkSelfPermission(
        context,
        Manifest.permission.CAMERA
    ) == PackageManager.PERMISSION_GRANTED
}

// Fungsi Baru: Cek Izin Lokasi
fun isLocationPermissionGranted(context: Context): Boolean {
    return (ContextCompat.checkSelfPermission(
        context,
        Manifest.permission.ACCESS_FINE_LOCATION
    ) == PackageManager.PERMISSION_GRANTED) ||
            (ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED)
}

@Composable
fun SecurityScreen(navController: NavController, viewModel: SecurityViewModel = hiltViewModel()) {
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current
    val snackbarHostState = remember { SnackbarHostState() }

    // Launcher untuk kembali dari Settings
    val permissionResultLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        // Setelah kembali dari settings, muat ulang status izin
        val isCameraEnabled = isCameraPermissionGranted(context)
        val isLocationEnabled = isLocationPermissionGranted(context)
        viewModel.updateCameraPermissionState(isCameraEnabled)
        viewModel.updateLocationState(isLocationEnabled)
    }

    // LaunchedEffect untuk memuat status izin saat pertama kali
    LaunchedEffect(Unit) {
        // Panggil fungsi cek izin real-time dan update ViewModel
        val isCameraEnabled = isCameraPermissionGranted(context)
        val isLocationEnabled = isLocationPermissionGranted(context)
        viewModel.loadInitialPermissions(isCameraEnabled, isLocationEnabled)
    }

    // LaunchedEffect untuk menampilkan Snackbar setelah reset password
    LaunchedEffect(uiState.message, uiState.isSuccess) {
        if (uiState.message != null) {
            snackbarHostState.showSnackbar(
                message = uiState.message!!,
                actionLabel = if (uiState.isSuccess) "OK" else "TUTUP",
                duration = SnackbarDuration.Short
            )
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        modifier = Modifier.fillMaxSize()
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFF5F5F5))
        ) {
            // TopBar
            TopBar(
                title = "Keamanan",
                onBackClick = { navController.popBackStack() }
            )

            // Content
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(paddingValues)
                    .padding(16.dp)
            ) {
                // Reset Password Section
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column(modifier = Modifier.padding(20.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            // ... (Icon dan Text "Reset Password") ...
                            Box(
                                modifier = Modifier
                                    .size(40.dp)
                                    .clip(CircleShape)
                                    .background(Color(0xFF00B4A3).copy(alpha = 0.10f)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    painter = painterResource(id = R.drawable.ic_key),
                                    contentDescription = "Reset Password",
                                    tint = Color(0xFF00B4A3),
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                            Spacer(modifier = Modifier.width(12.dp))
                            Text(
                                text = "Reset Password",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = Color(0xFF2D3142)
                            )
                        }

                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = "Masukkan email Anda untuk mengatur ulang kata sandi.",
                            fontSize = 14.sp,
                            color = Color(0xFF6B7280),
                            lineHeight = 20.sp
                        )
                        Spacer(modifier = Modifier.height(16.dp))

                        // Email Input
                        OutlinedTextField(
                            value = uiState.emailInput,
                            onValueChange = viewModel::updateEmail,
                            modifier = Modifier.fillMaxWidth(),
                            placeholder = { Text(text = "Masukkan alamat email", color = Color(0xFF9CA3AF), fontSize = 14.sp) },
                            leadingIcon = { Icon(painter = painterResource(id = R.drawable.ic_email), contentDescription = "Email", tint = Color(0xFF9CA3AF), modifier = Modifier.size(20.dp)) },
                            shape = RoundedCornerShape(8.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                unfocusedBorderColor = Color(0xFFE5E7EB),
                                focusedBorderColor = Color(0xFF00BFA5),
                                unfocusedContainerColor = Color(0xFFF9FAFB),
                                focusedContainerColor = Color(0xFFF9FAFB)
                            ),
                            singleLine = true
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        // Send Button
                        Button(
                            onClick = viewModel::sendPasswordReset,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(50.dp),
                            shape = RoundedCornerShape(8.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF00BFA5)),
                            enabled = !uiState.isLoading
                        ) {
                            if (uiState.isLoading) {
                                CircularProgressIndicator(color = Color.White, modifier = Modifier.size(20.dp))
                            } else {
                                Text(text = "Kirim", fontSize = 16.sp, fontWeight = FontWeight.SemiBold, color = Color.White)
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // App Permissions Section (MEMPERTAHANKAN STYLE CARD INI)
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column(modifier = Modifier.padding(20.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            // ... (Icon dan Text "Izin Aplikasi") ...
                            Box(
                                modifier = Modifier
                                    .size(40.dp)
                                    .clip(CircleShape)
                                    .background(Color(0xFFA78BFA).copy(alpha = 0.10f)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    painter = painterResource(id = R.drawable.ic_security),
                                    contentDescription = "App Permissions",
                                    tint = Color(0xFF8B5CF6),
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                            Spacer(modifier = Modifier.width(12.dp))
                            Text(text = "Izin Aplikasi", fontSize = 16.sp, fontWeight = FontWeight.SemiBold, color = Color(0xFF2D3142))
                        }

                        Spacer(modifier = Modifier.height(20.dp))

                        // Camera Permission Item
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(8.dp),
                            colors = CardDefaults.cardColors(containerColor = Color(0xFFF9FAFB)),
                            elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
                        ) {
                            Box(modifier = Modifier.padding(16.dp)) {
                                PermissionItem(
                                    icon = R.drawable.ic_camera,
                                    iconTint = Color(0xFF00BFA5),
                                    title = "Izin Kamera",
                                    description = "Diperlukan untuk melakukan scan kuku dan lidah.",
                                    isEnabled = uiState.isCameraPermissionEnabled,
                                    onToggle = {
                                        // Aksi: Buka pengaturan aplikasi
                                        permissionResultLauncher.launch(
                                            Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                                                data = Uri.fromParts("package", context.packageName, null)
                                            }
                                        )
                                    }
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        // 🔑 LOKASI Permission Item (Menggantikan Notifikasi)
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(8.dp),
                            colors = CardDefaults.cardColors(containerColor = Color(0xFFF9FAFB)),
                            elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
                        ) {
                            Box(modifier = Modifier.padding(16.dp)) {
                                PermissionItem(
                                    icon = R.drawable.ic_location,
                                    iconTint = Color(0xFF1E88E5),
                                    title = "Izin Lokasi",
                                    description = "Diperlukan untuk mencari fasilitas kesehatan terdekat.",
                                    isEnabled = uiState.isLocationEnabled,
                                    onToggle = {
                                        // Aksi: Buka pengaturan aplikasi
                                        permissionResultLauncher.launch(
                                            Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                                                data = Uri.fromParts("package", context.packageName, null)
                                            }
                                        )
                                    }
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun PermissionItem(
    icon: Int,
    iconTint: Color,
    title: String,
    description: String,
    isEnabled: Boolean,
    onToggle: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            modifier = Modifier.weight(1f),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painterResource(id = icon),
                contentDescription = title,
                tint = iconTint,
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.width(12.dp))
            Column {
                Text(
                    text = title,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color(0xFF2D3142)
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = description,
                    fontSize = 12.sp,
                    color = Color(0xFF6B7280),
                    lineHeight = 16.sp
                )
            }
        }

        Spacer(modifier = Modifier.width(12.dp))

        Switch(
            checked = isEnabled,
            onCheckedChange = onToggle, // onToggle membuka settings
            colors = SwitchDefaults.colors(
                checkedThumbColor = Color.White,
                checkedTrackColor = Color(0xFF00BFA5),
                uncheckedThumbColor = Color.White,
                uncheckedTrackColor = Color(0xFFE5E7EB)
            )
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun SecurityScreenPreview() {
    SecurityScreen(navController = NavController(LocalContext.current))
}
// Catatan: Fungsi isCameraPermissionGranted, isLocationPermissionGranted, dan openAppSettings
// yang baru dibuat harus berada di luar Composable atau di file yang sama (seperti di atas)
// untuk resolusi yang benar.