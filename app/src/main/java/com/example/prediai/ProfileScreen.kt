package com.example.prediai

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen() {
    val tealColor = Color(0xFF00BFA5)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF5F5F5))
    ) {
        // Header Section
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(bottomStart = 24.dp, bottomEnd = 24.dp),
            colors = CardDefaults.cardColors(containerColor = tealColor),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
        ) {
            Column(
                modifier = Modifier.padding(24.dp)
            ) {
                // Top bar with title and settings
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Profil",
                        color = Color.White,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Icon(
                        imageVector = Icons.Default.Settings,
                        contentDescription = "Settings",
                        tint = Color.White,
                        modifier = Modifier.size(24.dp)
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Profile info
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Profile image placeholder
                    Box(
                        modifier = Modifier
                            .size(60.dp)
                            .clip(CircleShape)
                            .background(Color.White.copy(alpha = 0.2f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = "Profile",
                            tint = Color.White,
                            modifier = Modifier.size(30.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(16.dp))

                    Column {
                        Text(
                            text = "John Santoso",
                            color = Color.White,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "john.santoso@email.com",
                            color = Color.White.copy(alpha = 0.8f),
                            fontSize = 14.sp
                        )
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.DateRange,
                                contentDescription = "Date",
                                tint = Color.White.copy(alpha = 0.8f),
                                modifier = Modifier.size(14.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "Bergabung sejak Jan 2024",
                                color = Color.White.copy(alpha = 0.8f),
                                fontSize = 12.sp
                            )
                        }
                    }
                }
            }
        }

        // Statistics Cards
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
                .offset(y = (-20).dp),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                StatItem(
                    number = "12",
                    label = "Total Scan",
                    numberColor = Color(0xFF00BFA5)
                )

                // Divider
                Box(
                    modifier = Modifier
                        .width(1.dp)
                        .height(40.dp)
                        .background(Color(0xFFE0E0E0))
                )

                StatItem(
                    number = "3",
                    label = "Perlu\nPerhatian",
                    numberColor = Color(0xFFFF6B35)
                )

                // Divider
                Box(
                    modifier = Modifier
                        .width(1.dp)
                        .height(40.dp)
                        .background(Color(0xFFE0E0E0))
                )

                StatItem(
                    number = "9",
                    label = "Normal",
                    numberColor = Color(0xFF4CAF50)
                )
            }
        }

        // Main Content
        LazyColumn(
            modifier = Modifier.padding(horizontal = 24.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            item { Spacer(modifier = Modifier.height(16.dp)) }

            // Kesehatan Section
            item {
                MenuSection(
                    title = "Kesehatan",
                    items = listOf(
                        MenuItem("Data Kesehatan", "Tinggi, berat badan, dll", Icons.Default.Favorite, Color(0xFF00BFA5)),
                        MenuItem("Riwayat Scan", "Lihat semua hasil scan", Icons.Default.Timeline, Color(0xFF2196F3)),
                        MenuItem("Pengingat", "Atur jadwal scan rutin", Icons.Default.Notifications, Color(0xFF9C27B0))
                    )
                )
            }

            // Akun Section
            item {
                MenuSection(
                    title = "Akun",
                    items = listOf(
                        MenuItem("Edit Profil", "Ubah foto dan info pribadi", Icons.Default.Person, Color(0xFF757575)),
                        MenuItem("Keamanan", "Password dan autentikasi", Icons.Default.Security, Color(0xFF4CAF50)),
                        MenuItem("Export Data", "Unduh data kesehatan", Icons.Default.Download, Color(0xFFFF9800))
                    )
                )
            }

            // Bantuan & Dukungan Section
            item {
                MenuSection(
                    title = "Bantuan & Dukungan",
                    items = listOf(
                        MenuItem("Pusat Bantuan", "FAQ dan panduan", Icons.Default.Help, Color(0xFF2196F3)),
                        MenuItem("Hubungi Kami", "Support dan feedback", Icons.Default.Headset, Color(0xFF00BCD4)),
                        MenuItem("Tentang PrediAI", "Versi 1.2.0", Icons.Default.Info, Color(0xFF9C27B0))
                    )
                )
            }

            // Logout Button
            item {
                Spacer(modifier = Modifier.height(16.dp))
                OutlinedButton(
                    onClick = { /* Handle logout */ },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = Color(0xFFE53E3E)
                    ),
                    border = androidx.compose.foundation.BorderStroke(
                        width = 1.dp,
                        color = Color(0xFFE53E3E)
                    )
                ) {
                    Icon(
                        imageVector = Icons.Default.ExitToApp,
                        contentDescription = "Logout",
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Keluar",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }

            item { Spacer(modifier = Modifier.height(24.dp)) }
        }
    }
}

@Composable
fun StatItem(
    number: String,
    label: String,
    numberColor: Color = Color(0xFF00BFA5)
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = number,
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            color = numberColor
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = label,
            fontSize = 12.sp,
            color = Color(0xFF757575),
            textAlign = TextAlign.Center,
            lineHeight = 14.sp
        )
    }
}

@Composable
fun MenuSection(
    title: String,
    items: List<MenuItem>
) {
    Column {
        Text(
            text = title,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF212121),
            modifier = Modifier.padding(bottom = 12.dp)
        )

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Column {
                items.forEachIndexed { index, item ->
                    MenuItemRow(item = item)
                    if (index < items.size - 1) {
                        Divider(
                            modifier = Modifier.padding(horizontal = 16.dp),
                            color = Color(0xFFE0E0E0),
                            thickness = 0.5.dp
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun MenuItemRow(item: MenuItem) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
                .background(item.iconColor.copy(alpha = 0.1f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = item.icon,
                contentDescription = item.title,
                tint = item.iconColor,
                modifier = Modifier.size(20.dp)
            )
        }

        Spacer(modifier = Modifier.width(16.dp))

        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = item.title,
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                color = Color(0xFF212121)
            )
            Text(
                text = item.subtitle,
                fontSize = 12.sp,
                color = Color(0xFF757575)
            )
        }

        Icon(
            imageVector = Icons.Default.ChevronRight,
            contentDescription = "Arrow",
            tint = Color(0xFF9E9E9E),
            modifier = Modifier.size(20.dp)
        )
    }
}

data class MenuItem(
    val title: String,
    val subtitle: String,
    val icon: ImageVector,
    val iconColor: Color
)
