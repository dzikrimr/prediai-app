package com.example.prediai.presentation.scan

import android.Manifest
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.prediai.R
import com.example.prediai.presentation.common.TopBar
import com.example.prediai.presentation.scan.comps.CameraPreview
import com.example.prediai.presentation.scan.comps.StepIndicator
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun ScanScreen(
    onBackClick: () -> Unit = {},
    onContinueClick: () -> Unit = {},
    onRetakeClick: () -> Unit = {},
    viewModel: ScanViewModel = viewModel()
) {
    var capturedImageUri by remember { mutableStateOf<String?>(null) }
    val uiState by viewModel.uiState.collectAsState()
    val cameraPermissionState = rememberPermissionState(permission = Manifest.permission.CAMERA)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF5F9FA))
    ) {
        // Top Bar
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
            // Step Indicator and Instruction in Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color.White
                ),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Step Indicator
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

                        // Progress Line
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
                            isCompleted = false
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Camera Preview / Captured Image
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
                    // Tampilkan pesan atau tombol untuk meminta izin
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text("Izin kamera diperlukan", color = Color.White)
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

                // Flip Camera Button
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
                        painter = painterResource(id = R.drawable.ic_refresh), // ganti dengan ikon flip camera
                        contentDescription = "Flip Camera",
                        tint = Color.White,
                        modifier = Modifier.size(24.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))


            // Status Message and Buttons Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color.White
                ),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Status Message
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(
                                Color(0xFFE0F7F4),
                                RoundedCornerShape(8.dp)
                            )
                            .padding(12.dp)
                    ) {
                        Text(
                            text = when {
                                uiState.statusMessage.isNotEmpty() -> uiState.statusMessage
                                capturedImageUri != null -> "Posisi sudah tepat"
                                else -> "Posisikan ${if (uiState.currentStep == 1) "kuku" else "lidah"} Anda"
                            },
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Medium,
                            color = Color(0xFF00B4A3),
                            textAlign = TextAlign.Center,
                            modifier = Modifier.fillMaxWidth()
                        )
}

                    Spacer(modifier = Modifier.height(16.dp))

                    // Bottom Buttons
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        // Retake Button - Left
                        TextButton(
                            onClick = {
                                capturedImageUri = null
                                onRetakeClick()
                            },
                            modifier = Modifier
                                .weight(1f)
                                .height(56.dp)
                        ) {
                            Text(
                                text = "Ambil Ulang Foto",
                                fontSize = 14.sp,
                                color = Color(0xFF2D3142)
                            )
                        }

                        Button(
                            onClick = {
                                if (uiState.currentStep == 1 && uiState.isDetectionSuccessful) {
                                    // Reset kamera & lanjut step lidah
                                    viewModel.proceedToNextStep()
                                } else if (uiState.currentStep == 2 && uiState.isDetectionSuccessful) {
                                    // Jika sudah selesai dua-duanya
                                    onContinueClick()
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
                                fontWeight = FontWeight.SemiBold,
                                color = if (capturedImageUri != null) Color.White else Color(0xFF9E9E9E)
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
    LaunchedEffect(Unit) {
        cameraPermissionState.launchPermissionRequest()
    }
}

@Preview(showBackground = true)
@Composable
fun ScanScreenPreview() {
    ScanScreen()
}