package com.example.prediai.presentation.main.comps

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter // Import Painter
import androidx.compose.ui.res.painterResource // Import painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.prediai.R

@Composable
fun ActionCardsSection() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        ActionCard(
            title = "Mulai Skrining",
            subtitle = "PrediAI siap membantu memeriksa gula darahmu",
            // UBAH INI: Gunakan painterResource untuk .png
            icon = painterResource(id = R.drawable.ic_screening),
            backgroundColor = Color(0xFFFFE0E0),
            onClick = { /*TODO: Navigate to Scan*/ }
        )
        ActionCard(
            title = "Butuh Bantuan Konsultasi",
            subtitle = "Konsultasi dengan chatbot untuk konsultasi kesehatan",
            // UBAH INI: Gunakan painterResource untuk .png
            icon = painterResource(id = R.drawable.ic_chatbot),
            backgroundColor = Color(0xFFD7F5D8),
            onClick = { /*TODO: Navigate to Chatbot*/ }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ActionCard(
    title: String,
    subtitle: String,
    icon: Painter, // UBAH TIPE PARAMETER MENJADI Painter
    backgroundColor: Color,
    onClick: () -> Unit
) {
    Card(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = backgroundColor)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(title, fontWeight = FontWeight.Bold, fontSize = 18.sp)
                Text(subtitle, fontSize = 14.sp, color = Color.Gray)
            }
            Spacer(modifier = Modifier.width(16.dp))
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .padding(12.dp), // Padding di dalam Box agar ikon tidak terlalu besar
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = icon, // Gunakan parameter painter
                    contentDescription = title,
                    tint = Color.Unspecified // Gunakan Color.Unspecified agar warna asli .png ditampilkan
                )
            }
        }
    }
}