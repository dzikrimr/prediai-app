package com.example.prediai.presentation.scan.comps

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun StepIndicator(
    number: Int,
    label: String,
    isActive: Boolean,
    isCompleted: Boolean
) {
    Row(
        verticalAlignment = Alignment.CenterVertically // teks sejajar tengah lingkaran
    ) {
        // Lingkaran nomor
        Box(
            modifier = Modifier
                .size(32.dp)
                .background(
                    when {
                        isCompleted -> Color(0xFF00B4A3)
                        isActive -> Color(0xFF00B4A3)
                        else -> Color(0xFFE0E0E0)
                    },
                    CircleShape
                ),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = number.toString(),
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = if (isActive || isCompleted) Color.White else Color(0xFF6B7280)
            )
        }

        Spacer(modifier = Modifier.width(8.dp)) // jarak lingkaran ke teks

        // Label teks di sebelah kanan lingkaran
        Text(
            text = label,
            fontSize = 12.sp,
            color = if (isActive) Color(0xFF374151) else Color(0xFF6B7280),
            fontWeight = if (isActive) FontWeight.SemiBold else FontWeight.Normal
        )
    }
}
