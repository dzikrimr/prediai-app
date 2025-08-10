package com.example.prediai

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScanDetectionScreen(navController: NavController) {
    var selectedTab by remember { mutableStateOf("Kuku") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF8F9FA))
    ) {
        // Top App Bar
        TopAppBar(
            title = {
                Text(
                    text = "Scan Deteksi",
                    fontWeight = FontWeight.Medium,
                    color = Color.Black
                )
            },
            navigationIcon = {
                IconButton(onClick = { navController.navigateUp() }) {
                    Icon(
                        Icons.Default.ArrowBack,
                        contentDescription = "Back",
                        tint = Color.Black
                    )
                }
            },
            actions = {
                IconButton(onClick = { /* Handle help */ }) {
                    Icon(
                        Icons.Default.Help,
                        contentDescription = "Help",
                        tint = Color.Gray
                    )
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = Color.White
            )
        )

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Feature Card
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFE8F5F3))
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(48.dp)
                                .background(Color(0xFF00BFA5), CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                Icons.Default.PhotoCamera,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(24.dp)
                            )
                        }

                        Spacer(modifier = Modifier.width(12.dp))

                        Column {
                            Text(
                                text = "Scan Kuku & Lidah",
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 16.sp
                            )
                            Text(
                                text = "Deteksi dini potensi diabetes",
                                fontSize = 14.sp,
                                color = Color.Gray
                            )
                        }
                    }

                    Text(
                        text = "Ambil foto kuku dan lidah Anda dalam satu frame untuk analisis komprehensif menggunakan AI.",
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                        fontSize = 14.sp,
                        color = Color.Gray
                    )

                    Spacer(modifier = Modifier.height(8.dp))
                }
            }

            // Scan Guide Section
            item {
                Text(
                    text = "Panduan Scan",
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 18.sp,
                    color = Color.Black
                )
            }

            // Guide Steps
            item {
                Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                    GuideStep(
                        number = "1",
                        title = "Posisi Tangan",
                        description = "Letakkan tangan dengan kuku menghadap kamera, pastikan pencahayaan cukup"
                    )

                    GuideStep(
                        number = "2",
                        title = "Posisi Lidah",
                        description = "Julurkan lidah dengan jelas, pastikan terlihat dalam frame yang sama"
                    )

                    GuideStep(
                        number = "3",
                        title = "Ambil Foto",
                        description = "Tekan tombol scan untuk mengambil foto dan memulai analisis"
                    )
                }
            }

            // Photo Section in Card (Tab Selection + Camera Section)
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {
                        // Tab Selection
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .border(
                                    2.dp,
                                    Color(0xFF00BFA5),
                                    RoundedCornerShape(8.dp)
                                )
                                .clip(RoundedCornerShape(8.dp)),
                            horizontalArrangement = Arrangement.SpaceEvenly
                        ) {
                            TabButton(
                                text = "Kuku",
                                isSelected = selectedTab == "Kuku",
                                onClick = { selectedTab = "Kuku" },
                                modifier = Modifier.weight(1f)
                            )
                            TabButton(
                                text = "Lidah",
                                isSelected = selectedTab == "Lidah",
                                onClick = { selectedTab = "Lidah" },
                                modifier = Modifier.weight(1f)
                            )
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        // Camera Section
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            // Camera Placeholder
                            Box(
                                modifier = Modifier
                                    .size(80.dp)
                                    .background(Color.Gray.copy(alpha = 0.3f), CircleShape),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    Icons.Default.PhotoCamera,
                                    contentDescription = null,
                                    tint = Color.Gray,
                                    modifier = Modifier.size(32.dp)
                                )
                            }

                            Spacer(modifier = Modifier.height(12.dp))

                            Text(
                                text = "Kamera akan aktif saat tombol scan ditekan",
                                fontSize = 14.sp,
                                color = Color.Gray,
                                textAlign = TextAlign.Center
                            )

                            Spacer(modifier = Modifier.height(24.dp))

                            // Action Buttons
                            Row(
                                horizontalArrangement = Arrangement.spacedBy(16.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                IconButton(
                                    onClick = { /* Handle gallery */ },
                                    modifier = Modifier
                                        .size(56.dp)
                                        .background(Color.Gray.copy(alpha = 0.2f), CircleShape)
                                ) {
                                    Icon(
                                        Icons.Default.Image,
                                        contentDescription = "Gallery",
                                        tint = Color.Gray
                                    )
                                }

                                // Main Camera Button
                                IconButton(
                                    onClick = { /* Handle camera */ },
                                    modifier = Modifier
                                        .size(80.dp)
                                        .background(Color(0xFF00BFA5), CircleShape)
                                ) {
                                    Icon(
                                        Icons.Default.PhotoCamera,
                                        contentDescription = "Take Photo",
                                        tint = Color.White,
                                        modifier = Modifier.size(32.dp)
                                    )
                                }

                                IconButton(
                                    onClick = { /* Handle refresh */ },
                                    modifier = Modifier
                                        .size(56.dp)
                                        .background(Color.Gray.copy(alpha = 0.2f), CircleShape)
                                ) {
                                    Icon(
                                        Icons.Default.Refresh,
                                        contentDescription = "Refresh",
                                        tint = Color.Gray
                                    )
                                }
                            }
                        }
                    }
                }
            }

            // Tips Section
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFFFF8E1))
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                Icons.Default.Lightbulb,
                                contentDescription = null,
                                tint = Color(0xFFFF8F00),
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Tips untuk Hasil Terbaik",
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 16.sp,
                                color = Color(0xFFFF8F00)
                            )
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        val tips = listOf(
                            "Pastikan pencahayaan yang cukup",
                            "Hindari bayangan pada kuku dan lidah",
                            "Jaga kamera tetap stabil",
                            "Pastikan kuku dan lidah terlihat jelas"
                        )

                        tips.forEach { tip ->
                            Row(
                                modifier = Modifier.padding(vertical = 2.dp),
                                verticalAlignment = Alignment.Top
                            ) {
                                Text(
                                    text = "• ",
                                    color = Color(0xFFFF8F00),
                                    fontSize = 14.sp
                                )
                                Text(
                                    text = tip,
                                    fontSize = 14.sp,
                                    color = Color(0xFF5D4037)
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
fun GuideStep(
    number: String,
    title: String,
    description: String
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.Top
    ) {
        Box(
            modifier = Modifier
                .size(32.dp)
                .background(Color(0xFF00BFA5), CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = number,
                color = Color.White,
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp
            )
        }

        Spacer(modifier = Modifier.width(16.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                fontWeight = FontWeight.SemiBold,
                fontSize = 16.sp
            )
            Text(
                text = description,
                fontSize = 14.sp,
                color = Color.Gray,
                modifier = Modifier.padding(top = 4.dp)
            )
        }
    }
}

@Composable
fun TabButton(
    text: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Button(
        onClick = onClick,
        modifier = modifier.fillMaxWidth(),
        colors = ButtonDefaults.buttonColors(
            containerColor = if (isSelected) Color(0xFF00BFA5) else Color.Transparent,
            contentColor = if (isSelected) Color.White else Color(0xFF00BFA5)
        ),
        elevation = ButtonDefaults.buttonElevation(0.dp),
        shape = RoundedCornerShape(6.dp)
    ) {
        Text(
            text = text,
            fontWeight = FontWeight.Medium,
            fontSize = 14.sp
        )
    }
}