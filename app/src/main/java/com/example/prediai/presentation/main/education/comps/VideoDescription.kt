package com.example.prediai.presentation.main.education.comps

import androidx.compose.foundation.layout.*
import androidx.compose.material3.* // <-- Make sure this import is correct
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.prediai.domain.model.EducationVideo

@Composable
fun VideoDescription(
    // --- ENSURE THESE PARAMETERS ARE EXACTLY LIKE THIS ---
    video: EducationVideo,
    aiSummary: String?,
    isSummaryLoading: Boolean,
    summaryError: String?,
    transcriptError: String?
    // --- END OF PARAMETERS ---
) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        // Judul Video
        Text(
            text = video.title,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold
        )
        // Sumber Video
        Text(
            text = video.source,
            fontSize = 14.sp,
            color = Color.Gray
        )

        HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))

        // --- BAGIAN RINGKASAN AI ---
        Text(
            text = "Ringkasan AI",
            fontSize = 16.sp,
            fontWeight = FontWeight.SemiBold
        )

        // Tampilkan loading, error, atau hasil
        when {
            // 1. Prioritas utama: Sedang loading summary?
            isSummaryLoading -> {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    CircularProgressIndicator(modifier = Modifier.size(16.dp), strokeWidth = 2.dp)
                    Spacer(Modifier.width(8.dp))
                    Text("Membuat rangkuman...", fontSize = 14.sp, color = Color.Gray)
                }
            }
            // 2. Jika tidak loading, apakah ada error saat membuat summary?
            summaryError != null -> {
                Text(summaryError, fontSize = 14.sp, color = MaterialTheme.colorScheme.error)
            }
            aiSummary != null -> {
                // --- INI PERUBAHANNYA ---
                // 1. Pecah string summary menjadi daftar (List) berdasarkan tanda hubung dan baris baru
                val points = aiSummary.split(Regex("[\\-\\n]")).filter { it.isNotBlank() }

                // 2. Tampilkan daftar tersebut menggunakan Composable baru kita
                BulletedList(points = points)
                // --- AKHIR PERUBAHAN ---
            }
            else -> {
                Text("Rangkuman akan muncul di sini.", fontSize = 14.sp, color = Color.Gray)
            }
        }
    }
}

// Preview mungkin perlu diperbarui untuk state baru
// @Preview ...