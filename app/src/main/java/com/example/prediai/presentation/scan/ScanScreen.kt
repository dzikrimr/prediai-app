package com.example.prediai.presentation.scan

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
import coil.compose.AsyncImage
import com.example.prediai.R
import com.example.prediai.presentation.common.TopBar
import com.example.prediai.presentation.scan.comps.StepIndicator

@Composable
fun ScreeningPage(
    onBackClick: () -> Unit = {},
    onContinueClick: () -> Unit = {},
    onRetakeClick: () -> Unit = {}
) {
    var currentStep by remember { mutableStateOf(1) } // 1 = Nail Scan, 2 = Tongue Scan
    var capturedImageUri by remember { mutableStateOf<String?>(null) }

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
            Spacer(modifier = Modifier.height(16.dp))

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
                            isActive = currentStep == 1,
                            isCompleted = currentStep > 1
                        )

                        Spacer(modifier = Modifier.width(8.dp))

                        // Progress Line
                        Box(
                            modifier = Modifier
                                .width(40.dp)
                                .height(4.dp)
                                .background(
                                    if (currentStep > 1) Color(0xFF00B4A3) else Color(0xFFE0E0E0),
                                    RoundedCornerShape(2.dp)
                                )
                        )

                        Spacer(modifier = Modifier.width(8.dp))

                        StepIndicator(
                            number = 2,
                            label = "Tongue Scan",
                            isActive = currentStep == 2,
                            isCompleted = false
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Instruction Text
                    Text(
                        text = if (currentStep == 1)
                            "Position your nails in the frame"
                        else
                            "Position your tongue in the frame",
                        fontSize = 14.sp,
                        color = Color(0xFF757575),
                        textAlign = TextAlign.Center
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Camera Preview / Captured Image
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(0.80f)
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color(0xFF424242))
            ) {
                if (capturedImageUri != null) {
                    AsyncImage(
                        model = capturedImageUri,
                        contentDescription = "Captured Image",
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                } else {
                    // Placeholder camera preview
                    Icon(
                        painter = painterResource(id = R.drawable.ic_camera),
                        contentDescription = "Camera",
                        tint = Color.White,
                        modifier = Modifier
                            .size(48.dp)
                            .align(Alignment.Center)
                    )
                }

                Icon(
                    painter = painterResource(
                        id = if (currentStep == 1) R.drawable.guide_nail else R.drawable.guide_tongue
                    ),
                    contentDescription = "Guide Frame",
                    tint = Color.Unspecified,
                    modifier = Modifier
                        .size(
                            if (currentStep == 1) 300.dp else 250.dp  // ukuran bisa beda per step
                        )
                        .align(Alignment.Center)
                )

                // Refresh Button - Bottom Right Corner
                FloatingActionButton(
                    onClick = {
                        // Simulate capture
                        capturedImageUri = "captured_image_${System.currentTimeMillis()}"
                    },
                    containerColor = Color(0xFF00B4A3),
                    shape = CircleShape,
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .padding(16.dp)
                        .size(56.dp)
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_refresh),
                        contentDescription = "Capture",
                        tint = Color.White,
                        modifier = Modifier.size(24.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.weight(1f))


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
                            text = if (capturedImageUri != null)
                                "Posisi sudah tepat"
                            else
                                "Posisikan ${if (currentStep == 1) "kuku" else "lidah"} Anda",
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

                        // Continue Button - Right
                        Button(
                            onClick = {
                                if (currentStep == 1) {
                                    currentStep = 2
                                    capturedImageUri = null
                                } else {
                                    onContinueClick()
                                }
                            },
                            modifier = Modifier
                                .weight(1f)
                                .height(56.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(0xFF00B4A3),
                                disabledContainerColor = Color(0xFFE0E0E0)
                            ),
                            shape = RoundedCornerShape(12.dp),
                            enabled = capturedImageUri != null
                        ) {
                            Text(
                                text = if (currentStep == 1) "Lanjutkan" else "Lihat Hasil",
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
}

@Preview(showBackground = true)
@Composable
fun ScreeningPagePreview() {
    ScreeningPage()
}