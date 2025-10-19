package com.example.prediai.presentation.main.comps

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
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
import com.example.prediai.domain.model.EducationVideo
import com.example.prediai.presentation.theme.PrediAITheme

@Composable
fun RecommendationsSection(
    recommendations: List<EducationVideo>,
    onSeeMoreClick: () -> Unit,
    onItemClick: (String) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // ... (Bagian Row Header tidak berubah)
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Cocok Untukmu",
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 16.sp
                )
                Text(
                    text = "Lihat Lainnya",
                    color = Color(0xFF00B4A3),
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier.clickable(
                        onClick = onSeeMoreClick
                    )
                )
            }
            Spacer(modifier = Modifier.height(16.dp))

            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                recommendations.forEachIndexed { index, recommendation ->
                    RecommendationItem(
                        recommendation = recommendation,
                        // --- PERBAIKAN UTAMA ADA DI SINI ---
                        // Logika dipindahkan kembali ke sini, di mana 'onItemClick' dikenal
                        onClick = {
                            Log.d("VideoDebug", "RecommendationsSection: Clicked ID -> ${recommendation.id}")
                            onItemClick(recommendation.id)
                        }
                    )
                    if (index < recommendations.lastIndex) {
                        HorizontalDivider(
                            modifier = Modifier.padding(top = 12.dp),
                            thickness = 1.dp,
                            color = Color.Gray.copy(alpha = 0.1f)
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun RecommendationItem(
    recommendation: EducationVideo,
    onClick: () -> Unit // <-- Parameter ini adalah tombol "Lakukan Pekerjaan"
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            // Cukup panggil 'onClick' yang sudah diberikan
            .clickable(onClick = onClick),
        verticalAlignment = Alignment.CenterVertically
    ) {
        AsyncImage(
            model = recommendation.imageUrl,
            contentDescription = recommendation.title,
            modifier = Modifier
                .size(80.dp)
                .clip(RoundedCornerShape(8.dp)),
            contentScale = ContentScale.Crop
        )
        Spacer(modifier = Modifier.width(16.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = recommendation.title,
                fontWeight = FontWeight.SemiBold,
                fontSize = 12.sp,
                maxLines = 2
            )
            Text(
                text = recommendation.source,
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                fontWeight = FontWeight.Normal
            )
            Text(
                text = recommendation.views,
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                fontWeight = FontWeight.Normal
            )
        }
    }
}