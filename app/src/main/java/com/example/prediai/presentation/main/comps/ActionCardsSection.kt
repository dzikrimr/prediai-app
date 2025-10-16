package com.example.prediai.presentation.main.comps

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.prediai.R
import com.example.prediai.presentation.theme.PrediAITheme

@Composable
fun ActionCardsSection(navController: NavController) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        ActionCard(
            title = "Cari Dokter Spesialis",
            subtitle = "Temukan dokter spesialis yang siap membantumu",
            icon = painterResource(id = R.drawable.ic_screening),
            // DIUBAH: Warna background card pertama
            backgroundColor = Color(0xFFFFA4AE),
            onClick = {
                // Navigasi ke DoctorScreen
                navController.navigate("doctor")
            }
        )
        ActionCard(
            title = "Butuh Bantuan Konsultasi",
            subtitle = "Konsultasi dengan chatbot untuk konsultasi kesehatan",
            icon = painterResource(id = R.drawable.ic_chatbot),
            // DIUBAH: Warna background card kedua
            backgroundColor = Color(0xFF61DA65),
            onClick = { /*TODO: Navigate to Chatbot*/ }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ActionCard(
    title: String,
    subtitle: String,
    icon: Painter,
    backgroundColor: Color,
    onClick: () -> Unit
) {
    Card(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        // DIUBAH: Corner radius menjadi 24.dp
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = backgroundColor),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                // DIUBAH: Properti teks title
                Text(
                    text = title,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 16.sp,
                    color = Color.White
                )
                // DIUBAH: Properti teks subtitle
                Text(
                    text = subtitle,
                    fontSize = 14.sp,
                    color = Color.White,
                    fontWeight = FontWeight.Normal // Regular
                )
            }
            Spacer(modifier = Modifier.width(16.dp))

            // DIUBAH: Box untuk frame ikon
            Box(
                modifier = Modifier
                    .size(56.dp) // Ukuran frame lingkaran
                    .clip(CircleShape)
                    .background(Color.White.copy(alpha = 0.2f)), // Background putih 20% opacity
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = icon,
                    contentDescription = title,
                    tint = Color.White, // Ikon dibuat warna putih agar kontras
                    modifier = Modifier.size(28.dp) // Ukuran ikon di dalam frame
                )
            }
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFF0F4F7)
@Composable
fun ActionCardsSectionPreview() {
    PrediAITheme {
        val navController = rememberNavController()
        ActionCardsSection(navController = navController)
    }
}