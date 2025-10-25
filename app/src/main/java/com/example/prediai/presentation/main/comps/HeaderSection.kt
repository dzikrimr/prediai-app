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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.prediai.presentation.theme.PrediAITheme
import java.util.Calendar

@Composable
fun HeaderSection(
    userName: String,
    profileImageUrl: String?,
    onNotificationClick: () -> Unit,
    onProfileClick: () -> Unit
) {
    val greeting = remember {
        val calendar = Calendar.getInstance()
        val hour = calendar.get(Calendar.HOUR_OF_DAY)

        when (hour) {
            in 4..9 -> "Selamat Pagi"
            in 10..14 -> "Selamat Siang"
            in 15..18 -> "Selamat Sore"
            else -> "Selamat Malam"
        }
    }

    val fullTitle = "$greeting, $userName!"
    val maxLength = 21

    val trimmedTitle = if (fullTitle.length > maxLength) {
        fullTitle.take(maxLength - 3) + "..."
    } else {
        fullTitle
    }

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
                        // 🔑 GUNAKAN trimmedTitle
                        text = trimmedTitle,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color(0xFF1E293B)
                    )
                    Text(
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
                            onClick = onProfileClick
                        )
                        // --- KLIP UNTUK MEMASTIKAN GAMBAR BERBENTUK LINGKARAN ---
                        .clip(CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    // --- LOGIKA DINAMIS UNTUK FOTO PROFIL ---
                    if (!profileImageUrl.isNullOrEmpty()) {
                        AsyncImage(
                            model = profileImageUrl,
                            contentDescription = "Profile Photo",
                            modifier = Modifier.fillMaxSize(), // Isi seluruh Box
                            contentScale = ContentScale.Crop // Memastikan gambar mengisi area
                        )
                    } else {
                        // Placeholder default jika URL kosong atau null
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
}

@Preview(showBackground = true, backgroundColor = 0xFFF0F4F7)
@Composable
fun HeaderSectionPreview() {
    PrediAITheme {
        HeaderSection(
            userName = "Sarah",
            onNotificationClick = {},
            onProfileClick = {},
            profileImageUrl = null,
        )
    }
}