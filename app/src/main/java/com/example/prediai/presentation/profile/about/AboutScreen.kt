package com.example.prediai.presentation.profile.about

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.prediai.R
import com.example.prediai.presentation.common.TopBar

@Composable
fun AboutScreen(navController: NavController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF8FAFC))
    ) {
        // Top Bar
        TopBar(
            title = "Tentang PrediAI",
            onBackClick = { navController.popBackStack() },
            backgroundColor = Color.Transparent
        )

        // Konten yang bisa di-scroll
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
        ) {
            // Header dengan Gradient
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(
                                Color(0xFF00B4A3).copy(alpha = 0.20f),
                                Color(0xFFF8FAFC)
                            )
                        )
                    )
                    .padding(vertical = 32.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.prediailogo),
                    contentDescription = "Logo PrediAI",
                    modifier = Modifier.size(80.dp)
                )
                Text(
                    text = "PrediAI",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF1E293B)
                )
                Text(
                    text = "Deteksi Dini Prediabetes dengan AI",
                    fontSize = 16.sp,
                    color = Color(0xFF475569)
                )
            }

            // Daftar Kartu Informasi
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp),
                verticalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                InfoCard(
                    iconResId = R.drawable.ic_lamp,
                    iconBgColor = Color(0xFFD9F7EA),
                    iconTint = Color(0xFF059669),
                    title = "Apa itu PrediAI?"
                ) {
                    Text(
                        text = "PrediAI adalah aplikasi berbasis Artificial Intelligence untuk membantu deteksi dini risiko prediabetes dan memberikan rekomendasi personal kesehatan.",
                        fontSize = 14.sp,
                        color = Color(0xFF475569),
                        lineHeight = 22.sp
                    )
                }

                InfoCard(
                    iconResId = R.drawable.ic_love,
                    iconBgColor = Color(0xFFFFF4DE),
                    iconTint = Color(0xFFF59E0B),
                    title = "Misi Kami"
                ) {
                    Text(
                        text = "Menyediakan teknologi yang mudah diakses untuk deteksi dini, edukasi, dan pencegahan diabetes melalui inovasi AI yang terpercaya.",
                        fontSize = 14.sp,
                        color = Color(0xFF475569),
                        lineHeight = 22.sp
                    )
                }

                InfoCard(
                    iconResId = R.drawable.ic_ai,
                    iconBgColor = Color(0xFFEFE6FF),
                    iconTint = Color(0xFF8B5CF6),
                    title = "Teknologi yang Digunakan"
                ) {
                    Text(
                        text = "PrediAI menggunakan analisis gambar berbasis AI untuk scan kuku & lidah, serta interpretasi dokumen dan hasil lab dengan akurasi tinggi.",
                        fontSize = 14.sp,
                        color = Color(0xFF475569),
                        lineHeight = 22.sp
                    )
                }

                InfoCard(
                    iconResId = R.drawable.ic_trust,
                    iconBgColor = Color(0xFFE0F3FF),
                    iconTint = Color(0xFF3B82F6),
                    title = "Manfaat untuk Anda"
                ) {
                    // Konten khusus untuk kartu "Manfaat" dengan bullet points
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        BulletPointText("Deteksi cepat dalam hitungan menit")
                        BulletPointText("Rekomendasi personal sesuai kondisi")
                        BulletPointText("Akses konsultasi dengan dokter")
                    }
                }
            }
            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

@Composable
fun InfoCard(
    iconResId: Int,
    iconBgColor: Color,
    iconTint: Color,
    title: String,
    content: @Composable () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier.padding(20.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Ikon dengan Latar Belakang Rounded
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(iconBgColor),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(id = iconResId),
                    contentDescription = title,
                    tint = iconTint,
                    modifier = Modifier.size(24.dp)
                )
            }
            // Kolom untuk Judul dan Konten
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(
                    text = title,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    color = Color(0xFF1E293B)
                )
                content()
            }
        }
    }
}

@Composable
fun BulletPointText(text: String) {
    Row(
        verticalAlignment = Alignment.Top,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Box(
            modifier = Modifier
                .padding(top = 8.dp)
                .size(6.dp)
                .clip(CircleShape)
                .background(Color(0xFF3B82F6))
        )
        Text(
            text = text,
            fontSize = 14.sp,
            color = Color(0xFF475569),
            lineHeight = 22.sp
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFF8FAFC)
@Composable
private fun AboutScreenPreview() {
    AboutScreen(navController = NavController(LocalContext.current))
}