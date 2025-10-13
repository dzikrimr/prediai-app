package com.example.prediai.presentation.scan.comps

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun DetailItem(
    bullet: String,
    text: String,
    bulletColor: Color
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        verticalAlignment = Alignment.Top
    ) {
        Text(
            text = bullet,
            fontSize = 16.sp,
            color = bulletColor,
            modifier = Modifier.padding(end = 8.dp)
        )
        Text(
            text = text,
            fontSize = 14.sp,
            color = Color(0xFF424242),
            lineHeight = 20.sp
        )
    }
}