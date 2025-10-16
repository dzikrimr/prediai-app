package com.example.prediai.presentation.main.education.comps

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.prediai.presentation.main.education.EducationVideo

@Composable
fun VideoDescription(video: EducationVideo) {
    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        Column {
            Text(
                text = video.title,
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp
            )
            Text(
                text = "oleh ${video.source}",
                fontSize = 14.sp,
                color = Color.Gray
            )
        }

        Column {
            Text(
                text = "Ringkasan AI",
                fontWeight = FontWeight.SemiBold,
                fontSize = 16.sp,
                color = Color(0xFF00B4A3)
            )
            Text(
                text = "Apakah kamu sering merasa gampang lelah dan tidak segar? Video ini membahas Penyebab, Ciri-Ciri dan Pencegahan Penyakit Gula.",
                fontSize = 14.sp,
                color = Color.DarkGray
            )
        }
    }
}