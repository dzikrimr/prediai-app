package com.example.prediai.presentation.profile

import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.prediai.R
import com.example.prediai.presentation.common.TopBar

data class ProfileMenuItem(
    val title: String,
    val subtitle: String,
    val icon: Int,
    val iconColor: Color,
    val iconBgColor: Color,
    val route: String
)

// Daftar menu untuk seksi "Akun"
val accountMenuItems = listOf(
    ProfileMenuItem(
        title = "Edit Profil",
        subtitle = "Ubah foto dan info pribadi",
        icon = R.drawable.ic_profile,
        iconColor = Color(0xFF424242),
        iconBgColor = Color(0xFFEEEEEE),
        route = "edit_profile" // Rute ke EditProfileScreen
    ),
    ProfileMenuItem(
        title = "Keamanan",
        subtitle = "Password dan izin",
        icon = R.drawable.ic_security,
        iconColor = Color(0xFF4CAF50),
        iconBgColor = Color(0xFFE8F5E9),
        route = "security" // Rute ke SecurityScreen
    )
)

// Daftar menu untuk seksi "Bantuan & Dukungan"
val supportMenuItems = listOf(
    ProfileMenuItem(
        title = "Pusat Bantuan",
        subtitle = "FAQ dan panduan",
        icon = R.drawable.ic_help,
        iconColor = Color(0xFF2196F3),
        iconBgColor = Color(0xFFE3F2FD),
        route = "help_center" // Rute ke HelpCenterScreen
    ),
    ProfileMenuItem(
        title = "Hubungi Kami",
        subtitle = "Support dan feedback",
        icon = R.drawable.ic_support,
        iconColor = Color(0xFF00BFA5),
        iconBgColor = Color(0xFFB2F5EA),
        route = "contact_us" // Rute ke ContactUsScreen
    ),
    ProfileMenuItem(
        title = "Tentang PrediAI",
        subtitle = "Versi 1.2.0",
        icon = R.drawable.ic_info2,
        iconColor = Color(0xFF9C27B0),
        iconBgColor = Color(0xFFF3E5F5),
        route = "about" // Rute ke AboutScreen
    )
)

@Composable
fun ProfileScreen(navController: NavController) {
    val tealColor = Color(0xFF00BFA5)
    val lightTealBg = Color(0xFFB2F5EA)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF8FAFC)) // Latar belakang sedikit abu-abu
    ) {
        // Top Bar
        TopBar(
            title = "Profil",
            onBackClick = { navController.popBackStack() } // Aksi kembali ke halaman sebelumnya
        )

        // Profile Header Card
        Card(
            modifier = Modifier
                .fillMaxWidth(),
            shape = RoundedCornerShape(bottomEnd = 36.dp), // Shape lebih halus
            colors = CardDefaults.cardColors(containerColor = lightTealBg.copy(alpha = 0.5f)),
            elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
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
                        color = Color(0xFF0F766E) // Warna lebih gelap
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "john.santoso@gmail.com",
                        fontSize = 14.sp,
                        color = Color(0xFF0F766E).copy(alpha = 0.8f)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.DateRange,
                            contentDescription = "Date",
                            tint = Color(0xFF0F766E),
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "16 Agustus 2000",
                            fontSize = 12.sp,
                            color = Color(0xFF0F766E).copy(alpha = 0.8f)
                        )
                    }
                }
            }
        }

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(vertical = 16.dp)
        ) {

            // Akun Section
            item {
                Text(
                    text = "Akun",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF475569),
                    modifier = Modifier.padding(horizontal = 24.dp, vertical = 8.dp)
                )
            }

            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column {
                        accountMenuItems.forEachIndexed { index, item ->
                            SimpleMenuItem(
                                title = item.title,
                                subtitle = item.subtitle,
                                icon = item.icon,
                                iconColor = item.iconColor,
                                iconBgColor = item.iconBgColor,
                                onClick = { navController.navigate(item.route) }
                            )
                            if (index < accountMenuItems.lastIndex) {
                                HorizontalDivider(
                                    modifier = Modifier.padding(start = 72.dp, end = 16.dp),
                                    color = Color(0xFFF1F5F9)
                                )
                            }
                        }
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
                    color = Color(0xFF475569),
                    modifier = Modifier.padding(horizontal = 24.dp, vertical = 8.dp)
                )
            }

            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column {
                        supportMenuItems.forEachIndexed { index, item ->
                            SimpleMenuItem(
                                title = item.title,
                                subtitle = item.subtitle,
                                icon = item.icon,
                                iconColor = item.iconColor,
                                iconBgColor = item.iconBgColor,
                                onClick = { navController.navigate(item.route) }
                            )
                            if (index < supportMenuItems.lastIndex) {
                                HorizontalDivider(
                                    modifier = Modifier.padding(start = 72.dp, end = 16.dp),
                                    color = Color(0xFFF1F5F9)
                                )
                            }
                        }
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
                        containerColor = Color(0xFFFEF2F2),
                        contentColor = Color(0xFFEF4444)
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
    iconBgColor: Color,
    onClick: () -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(
                interactionSource = interactionSource,
                indication = LocalIndication.current,
                onClick = onClick
            )
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
                color = Color(0xFF334155)
            )
            Text(
                text = subtitle,
                fontSize = 12.sp,
                color = Color(0xFF64748B)
            )
        }

        Icon(
            imageVector = Icons.Default.ChevronRight,
            contentDescription = "Arrow",
            tint = Color(0xFF94A3B8),
            modifier = Modifier.size(24.dp)
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun ProfileScreenPreview() {
    val navController = rememberNavController()
    ProfileScreen(navController = navController)
}