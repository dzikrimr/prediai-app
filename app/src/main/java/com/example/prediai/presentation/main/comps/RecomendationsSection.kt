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
// 1. GANTI IMPORT INI:
// import com.example.prediai.presentation.main.Recommendation // <-- HAPUS
import com.example.prediai.domain.model.EducationVideo // <-- IMPORT MODEL BARU
import com.example.prediai.domain.model.VideoCategories // <-- Import untuk Preview
import com.example.prediai.presentation.theme.PrediAITheme

@Composable
fun RecommendationsSection(
    // 2. GANTI TIPE DATA DI PARAMETER INI:
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
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Cocok Untukmu",
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 18.sp
                )
                Text(
                    text = "Lihat Lainnya",
                    color = Color(0xFF00B4A3),
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier.clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = rememberRipple(),
                        onClick = onSeeMoreClick
                    )
                )
            }
            Spacer(modifier = Modifier.height(16.dp))

            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                recommendations.forEachIndexed { index, recommendation ->
                    // 'recommendation' sekarang adalah tipe EducationVideo
                    RecommendationItem(
                        recommendation = recommendation,
                        onClick = { onItemClick(recommendation.id) }
                    )
                    if (index < recommendations.lastIndex) {
                        HorizontalDivider(
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
    // 3. GANTI TIPE DATA DI PARAMETER INI JUGA:
    recommendation: EducationVideo,
    onClick: () -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(
                interactionSource = interactionSource,
                indication = rememberRipple(),
                onClick = onClick
            ),
        verticalAlignment = Alignment.CenterVertically
    ) {
        AsyncImage(
            // Nama properti (imageUrl, title, source, views)
            // sama antara Recommendation dan EducationVideo,
            // jadi tidak ada perubahan di sini.
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
        // 4. PERBARUI PREVIEW AGAR MENGGUNAKAN DATA DUMMY EducationVideo
        RecommendationsSection(
            recommendations = listOf(
                EducationVideo(
                    id = "1",
                    title = "Tips agar gula darah tidak naik",
                    source = "Ilmu Dokter",
                    views = "91rb x ditonton",
                    imageUrl = "https://picsum.photos/200",
                    youtubeVideoId = "dummy_id_1",
                    category = VideoCategories.NUTRISI
                ),
                EducationVideo(
                    id = "2",
                    title = "Kenali gejala diabetesmu melalui tangan",
                    source = "Ilmu Dokter",
                    views = "91rb x ditonton",
                    imageUrl = "https.picsum.photos/201",
                    youtubeVideoId = "dummy_id_2",
                    category = VideoCategories.GEJALA
                ),
            ),
            onSeeMoreClick = {},
            onItemClick = {}
        )
    }
}