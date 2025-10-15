package com.example.prediai.presentation.profile

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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.prediai.R
import com.example.prediai.presentation.common.TopBar

@Composable
fun ProfileScreen() {
    val tealColor = Color(0xFF00BFA5)
    val lightTealBg = Color(0xFFB2F5EA)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        // Top Bar
        TopBar(
            title = "Profil",
            subtitle = "Profile",
            onBackClick = { /* Handle back */ }
        )

        // Profile Header Card
        Card(
            modifier = Modifier
                .fillMaxWidth(),
            shape = RoundedCornerShape(bottomEnd = 30.dp),
            colors = CardDefaults.cardColors(containerColor = lightTealBg),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Profile Image
                Box(
                    modifier = Modifier
                        .size(80.dp)
                        .clip(CircleShape)
                        .background(Color.White),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = "Profile",
                        tint = tealColor,
                        modifier = Modifier.size(40.dp)
                    )
                }

                Spacer(modifier = Modifier.width(16.dp))

                Column {
                    Text(
                        text = "John Santoso",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = tealColor
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "john.santoso@gmail.com",
                        fontSize = 14.sp,
                        color = tealColor
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.DateRange,
                            contentDescription = "Date",
                            tint = tealColor,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "16 Agustus 2000",
                            fontSize = 12.sp,
                            color = tealColor
                        )
                    }
                }
            }
        }

        LazyColumn(
            modifier = Modifier.fillMaxSize()
        ) {

            // Akun Section
            item {
                Spacer(modifier = Modifier.height(24.dp))
                Text(
                    text = "Akun",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF212121),
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                )
            }

            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
                ) {
                    Column {
                        SimpleMenuItem(
                            title = "Edit Profil",
                            subtitle = "Ubah foto dan info pribadi",
                            icon = R.drawable.ic_profile,
                            iconColor = Color(0xFF424242),
                            iconBgColor = Color(0xFFEEEEEE)
                        )
                        HorizontalDivider(
                            modifier = Modifier.padding(horizontal = 16.dp),
                            color = Color(0xFFEEEEEE)
                        )
                        SimpleMenuItem(
                            title = "Keamanan",
                            subtitle = "Password dan izin",
                            icon = R.drawable.ic_security,
                            iconColor = Color(0xFF4CAF50),
                            iconBgColor = Color(0xFFE8F5E9)
                        )
                    }
                }
            }

            // Bantuan & Dukungan Section
            item {
                Spacer(modifier = Modifier.height(24.dp))
                Text(
                    text = "Bantuan & Dukungan",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF212121),
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                )
            }

            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
                ) {
                    Column {
                        SimpleMenuItem(
                            title = "Pusat Bantuan",
                            subtitle = "FAQ dan panduan",
                            icon = R.drawable.ic_help,
                            iconColor = Color(0xFF2196F3),
                            iconBgColor = Color(0xFFE3F2FD)
                        )
                        HorizontalDivider(
                            modifier = Modifier.padding(horizontal = 16.dp),
                            color = Color(0xFFEEEEEE)
                        )
                        SimpleMenuItem(
                            title = "Hubungi Kami",
                            subtitle = "Support dan feedback",
                            icon = R.drawable.ic_support,
                            iconColor = tealColor,
                            iconBgColor = lightTealBg
                        )
                        HorizontalDivider(
                            modifier = Modifier.padding(horizontal = 16.dp),
                            color = Color(0xFFEEEEEE)
                        )
                        SimpleMenuItem(
                            title = "Tentang PrediAI",
                            subtitle = "Versi 1.2.0",
                            icon = R.drawable.ic_info2,
                            iconColor = Color(0xFF9C27B0),
                            iconBgColor = Color(0xFFF3E5F5)
                        )
                    }
                }
            }

            // Logout Button
            item {
                Spacer(modifier = Modifier.height(32.dp))
                Button(
                    onClick = { /* Handle logout */ },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                        .height(50.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFFFFF5F5),
                        contentColor = Color(0xFFE53E3E)
                    ),
                    shape = RoundedCornerShape(12.dp),
                    elevation = ButtonDefaults.buttonElevation(defaultElevation = 0.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Logout,
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
                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }
}

@Composable
fun SimpleMenuItem(
    title: String,
    subtitle: String,
    icon: Int,
    iconColor: Color,
    iconBgColor: Color
) {
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
                .background(iconBgColor),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                painter = painterResource(id = icon),
                contentDescription = title,
                tint = iconColor,
                modifier = Modifier.size(20.dp)
            )
        }

        Spacer(modifier = Modifier.width(16.dp))

        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = title,
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                color = Color(0xFF212121)
            )
            Text(
                text = subtitle,
                fontSize = 12.sp,
                color = Color(0xFF9E9E9E)
            )
        }

        Icon(
            imageVector = Icons.Default.ChevronRight,
            contentDescription = "Arrow",
            tint = Color(0xFFBDBDBD),
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

@Preview
@Composable
private fun ProfileScreenPreview() {
    ProfileScreen()
}