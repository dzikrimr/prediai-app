package com.example.prediai.presentation.scan

import android.Manifest
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.prediai.R
import com.example.prediai.data.remote.scan.AnalysisResponse
import com.example.prediai.presentation.common.TopBar
import com.example.prediai.presentation.scan.comps.CameraPreview
import com.example.prediai.presentation.scan.comps.StepIndicator
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun ScanScreen(
    // Lambda onContinueClick diubah untuk membawa data hasil analisis dan gambar
    onContinueClick: (AnalysisResponse, ByteArray?, ByteArray?) -> Unit = { _, _, _ -> },
    onBackClick: () -> Unit = {},
    viewModel: ScanViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val cameraPermissionState = rememberPermissionState(permission = Manifest.permission.CAMERA)

    // LaunchedEffect untuk menangani navigasi secara otomatis setelah analisis selesai
    LaunchedEffect(uiState.analysisResult) {
        uiState.analysisResult?.let { result ->
            onContinueClick(result, viewModel.capturedNailImage, viewModel.capturedTongueImage)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF5F9FA))
    ) {
        TopBar(
            title = "Skrining",
            subtitle = "Screening",
            onBackClick = onBackClick
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Card untuk Step Indicator
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        StepIndicator(
                            number = 1,
                            label = "Nail Scan",
                            isActive = uiState.currentStep == 1,
                            isCompleted = uiState.currentStep > 1
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Box(
                            modifier = Modifier
                                .width(40.dp)
                                .height(4.dp)
                                .background(
                                    if (uiState.currentStep > 1) Color(0xFF00B4A3) else Color(0xFFE0E0E0),
                                    RoundedCornerShape(2.dp)
                                )
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        StepIndicator(
                            number = 2,
                            label = "Tongue Scan",
                            isActive = uiState.currentStep == 2,
                            isCompleted = false // Step 2 tidak pernah ditandai selesai di layar ini
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Box untuk Camera Preview
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(0.85f)
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color(0xFF424242))
            ) {
                if (cameraPermissionState.status.isGranted) {
                    CameraPreview(
                        modifier = Modifier.fillMaxSize(),
                        cameraLens = uiState.cameraLens,
                        imageAnalyzer = viewModel.imageAnalyzer
                    )
                } else {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text("Izin kamera diperlukan untuk skrining", color = Color.White)
                        Spacer(modifier = Modifier.height(8.dp))
                        Button(onClick = { cameraPermissionState.launchPermissionRequest() }) {
                            Text("Beri Izin")
                        }
                    }
                }

                // Guide Frame
                Icon(
                    painter = painterResource(
                        id = if (uiState.currentStep == 1) R.drawable.guide_nail else R.drawable.guide_tongue
                    ),
                    contentDescription = "Guide Frame",
                    tint = Color.Unspecified,
                    modifier = Modifier
                        .size(if (uiState.currentStep == 1) 300.dp else 250.dp)
                        .align(Alignment.Center)
                )

                // Tombol Flip Camera
                FloatingActionButton(
                    onClick = { viewModel.flipCamera() },
                    containerColor = Color(0xFF00B4A3),
                    shape = CircleShape,
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .padding(16.dp)
                        .size(56.dp)
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_refresh),
                        contentDescription = "Flip Camera",
                        tint = Color.White,
                        modifier = Modifier.size(24.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Card untuk Status dan Tombol Aksi
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Pesan Status
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(
                                color = when {
                                    uiState.isLoading -> Color(0xFFE0E0E0)
                                    uiState.isDetectionSuccessful -> Color(0xFFE0F7F4)
                                    else -> Color(0xFFFFF3E0)
                                },
                                shape = RoundedCornerShape(8.dp)
                            )
                            .padding(12.dp)
                    ) {
                        Text(
                            text = uiState.statusMessage,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Medium,
                            color = when {
                                uiState.isLoading -> Color.Black
                                uiState.isDetectionSuccessful -> Color(0xFF00B4A3)
                                else -> Color(0xFFE65100)
                            },
                            textAlign = TextAlign.Center,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Tombol Aksi Bawah
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        // Tombol Ambil Ulang
                        TextButton(
                            onClick = { viewModel.resetDetection() },
                            modifier = Modifier
                                .weight(1f)
                                .height(56.dp)
                        ) {
                            Text(
                                text = "Ambil Ulang",
                                fontSize = 14.sp,
                                color = Color(0xFF2D3142)
                            )
                        }

                        // Tombol Lanjutkan / Lihat Hasil
                        Button(
                            onClick = {
                                if (uiState.currentStep == 1) {
                                    viewModel.proceedToNextStep()
                                } else if (uiState.currentStep == 2) {
                                    viewModel.performAnalysis()
                                }
                            },
                            enabled = uiState.isDetectionSuccessful,
                            modifier = Modifier
                                .weight(1f)
                                .height(56.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(0xFF00B4A3),
                                disabledContainerColor = Color(0xFFE0E0E0)
                            ),
                            shape = RoundedCornerShape(12.dp),
                        ) {
                            Text(
                                text = if (uiState.currentStep == 1) "Lanjutkan" else "Lihat Hasil",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }
                }
            }
        }
    }

    // Meminta izin kamera saat layar pertama kali ditampilkan
    LaunchedEffect(Unit) {
        cameraPermissionState.launchPermissionRequest()
    }
}

@Preview(showBackground = true)
@Composable
fun ScanScreenPreview() {
    // Preview tidak akan berfungsi penuh karena butuh ViewModel dan izin kamera
    ScanScreen()
}