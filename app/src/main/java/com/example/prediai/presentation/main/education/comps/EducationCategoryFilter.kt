package com.example.prediai.presentation.main.education.comps

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.prediai.domain.model.VideoCategories
import com.example.prediai.presentation.theme.PrediAITheme

@Composable
fun EducationCategoryFilter(
    categories: List<String>,
    selectedCategory: String,
    onCategoryClick: (String) -> Unit
) {
    // Menggunakan Row dengan horizontalScroll, persis seperti HistoryFilterButtons
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .horizontalScroll(rememberScrollState())
            // Tambahkan padding agar tidak menempel di tepi layar
            .padding(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        // Ulangi berdasarkan daftar kategori yang diberikan
        categories.forEach { category ->
            FilterChip(
                text = category,
                isSelected = selectedCategory == category,
                onClick = { onCategoryClick(category) }
            )
        }
    }
}

/**
 * FilterChip kustom yang sama persis dengan yang ada di HistoryFilterButtons
 */
@Composable
private fun FilterChip(text: String, isSelected: Boolean, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        shape = RoundedCornerShape(50),
        colors = ButtonDefaults.buttonColors(
            containerColor = if (isSelected) Color(0xFF00B4A3) else Color(0xFFF3F4F6),
            contentColor = if (isSelected) Color.White else Color.Gray
        ),
        contentPadding = PaddingValues(horizontal = 20.dp, vertical = 10.dp)
    ) {
        Text(text, fontSize = 14.sp, fontWeight = FontWeight.Medium)
    }
}

@Preview
@Composable
fun EducationCategoryFilterPreview() {
    PrediAITheme {
        val categories = listOf(
            VideoCategories.SEMUA,
            VideoCategories.NUTRISI,
            VideoCategories.OLAHRAGA,
            VideoCategories.PERAWATAN,
            VideoCategories.GEJALA
        )
        EducationCategoryFilter(
            categories = categories,
            selectedCategory = VideoCategories.NUTRISI,
            onCategoryClick = {}
        )
    }
}