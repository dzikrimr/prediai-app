package com.example.prediai.presentation.main.education.comps

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun BulletedList(
    modifier: Modifier = Modifier,
    points: List<String>
) {
    Column(modifier = modifier) {
        points.forEach { point ->
            // Pastikan tidak ada poin yang kosong
            if (point.isNotBlank()) {
                Row {
                    Text(
                        text = "•",
                        textAlign = TextAlign.Center,
                        modifier = Modifier.size(20.dp),
                        fontSize = 14.sp
                    )
                    Text(
                        text = point.trim(),
                        modifier = Modifier.weight(1f), // <-- Kunci agar teks panjang turun ke bawah
                        fontSize = 14.sp
                    )
                }
            }
        }
    }
}