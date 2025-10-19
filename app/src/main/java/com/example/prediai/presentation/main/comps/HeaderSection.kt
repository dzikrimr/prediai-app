package com.example.prediai.presentation.main.comps

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.prediai.presentation.theme.PrediAITheme
// --- 1. TAMBAHKAN IMPORT INI UNTUK MENGAMBIL WAKTU ---
import java.util.Calendar

@Composable
fun HeaderSection(
    userName: String,
    onNotificationClick: () -> Unit,
    onProfileClick: () -> Unit
) {
    // --- 2. TAMBAHKAN LOGIKA WAKTU DI SINI ---
    // 'remember' dipakai agar kalkulasi ini tidak berjalan
    // setiap kali terjadi recomposition (pergerakan UI).
    val greeting = remember {
        val calendar = Calendar.getInstance()
        val hour = calendar.get(Calendar.HOUR_OF_DAY) // Ambil jam (format 0-23)

        // Tentukan ucapan berdasarkan jam
        when (hour) {
            in 4..9 -> "Selamat Pagi"   // 04:00 - 09:59
            in 10..14 -> "Selamat Siang"  // 10:00 - 14:59
            in 15..18 -> "Selamat Sore"  // 15:00 - 18:59
            else -> "Selamat Malam"     // 19:00 - 03:59
        }
    }

    // --- 3. TERJEMAHKAN SUBTITLE ---
    val subtitle = "Bagaimana kabarmu hari ini?"

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        IconButton(onClick = onNotificationClick) {
            Icon(
                imageVector = Icons.Outlined.Notifications,
                contentDescription = "Notifications",
                modifier = Modifier.size(28.dp),
                tint = Color.Gray
            )
        }

        Card(
            modifier = Modifier
                .weight(1f)
                .height(81.dp),
            shape = RoundedCornerShape(topStart = 20.dp, bottomStart = 20.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight()
                    .padding(horizontal = 16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .padding(end = 8.dp),
                    horizontalAlignment = Alignment.End
                ) {
                    Text(
                        // --- 4. GUNAKAN TEKS YANG SUDAH DINAMIS ---
                        text = "$greeting, $userName!",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color(0xFF1E293B)
                    )
                    Text(
                        // --- 5. GUNAKAN TEKS YANG SUDAH DITERJEMAHKAN ---
                        text = subtitle,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color(0xFF1E293B)
                    )
                }

                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .background(color = Color(0xFF00B4A3), shape = CircleShape)
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = rememberRipple(bounded = false),
                            onClick = onProfileClick
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = "Profile",
                        tint = Color.White,
                        modifier = Modifier.size(28.dp)
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFF0F4F7)
@Composable
fun HeaderSectionPreview() {
    PrediAITheme {
        HeaderSection(
            userName = "Sarah",
            onNotificationClick = {},
            onProfileClick = {}
        )
    }
}