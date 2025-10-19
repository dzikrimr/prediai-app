package com.example.prediai.presentation.main.comps

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
import com.example.prediai.presentation.main.Recommendation
import com.example.prediai.presentation.theme.PrediAITheme

@Composable
fun RecommendationsSection(
    recommendations: List<Recommendation>,
    onSeeMoreClick: () -> Unit, // DIUBAH: Tambahkan parameter ini
    onItemClick: (String) -> Unit // DIUBAH: Tambahkan parameter ini
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
                    // DIUBAH: Buat teks bisa diklik (versi aman)
                    modifier = Modifier.clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = remember { ripple() },
                        onClick = onSeeMoreClick
                    )
                )
            }
            Spacer(modifier = Modifier.height(16.dp))

            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                recommendations.forEachIndexed { index, recommendation ->
                    // DIUBAH: Teruskan aksi klik dengan ID video
                    RecommendationItem(
                        recommendation = recommendation,
                        onClick = { onItemClick(recommendation.id) }
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
    recommendation: Recommendation,
    onClick: () -> Unit // DIUBAH: Terima parameter lambda
) {
    val interactionSource = remember { MutableInteractionSource() }
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(
                interactionSource = interactionSource,
                indication = remember { ripple() },
                onClick = onClick // Gunakan lambda yang diteruskan
            ),
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

@Preview(showBackground = true, backgroundColor = 0xFFF0F4F7)
@Composable
fun RecommendationsSectionPreview() {
    PrediAITheme {
        RecommendationsSection(
            recommendations = listOf(
                Recommendation("1", "Tips agar gula darah tidak naik", "Ilmu Dokter", "91rb x ditonton", "https://picsum.photos/200"),
                Recommendation("2", "Kenali gejala diabetesmu melalui tangan", "Ilmu Dokter", "91rb x ditonton", "https://picsum.photos/201"),
            ),
            onSeeMoreClick = {},
            onItemClick = {}
        )
    }
}