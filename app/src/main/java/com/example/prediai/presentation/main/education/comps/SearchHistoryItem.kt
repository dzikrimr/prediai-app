package com.example.prediai.presentation.main.education.comps

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.History
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun SearchHistoryItem(text: String, onClearClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            // DIUBAH: Mengurangi padding vertikal untuk jarak lebih rapat
            .padding(vertical = 2.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(Icons.Default.History, contentDescription = "History", tint = Color.Gray)
        Spacer(Modifier.width(16.dp))
        // DIUBAH: Properti teks sesuai permintaan
        Text(
            text = text,
            modifier = Modifier.weight(1f),
            color = Color.Gray,
            fontSize = 14.sp,
            fontWeight = FontWeight.Normal // Regular
        )
        IconButton(onClick = onClearClick) {
            Icon(Icons.Default.Close, contentDescription = "Clear", tint = Color.Gray)
        }
    }
}