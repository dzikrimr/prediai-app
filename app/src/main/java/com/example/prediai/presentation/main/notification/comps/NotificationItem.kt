package com.example.prediai.presentation.main.notification.comps

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
// --- IMPORT BARU ---
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.outlined.CalendarToday
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.prediai.presentation.main.notification.NotificationItemData
import com.example.prediai.presentation.theme.PrediAITheme

@Composable
fun NotificationItem(
    item: NotificationItemData,
    // --- 1. TAMBAHKAN PARAMETER LAMBDA INI ---
    onDeleteClick: (String) -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp), // Kurangi padding vertikal sedikit
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Ikon Kiri (Tidak berubah)
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .background(color = Color(0xFFE8E0FF), shape = CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Outlined.CalendarToday,
                    contentDescription = "Notification Icon",
                    tint = Color(0xFF7C4DFF)
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            // Kolom Teks (Tidak berubah)
            Column(modifier = Modifier.weight(1f)) { // Beri weight agar tombol hapus bisa di ujung
                Text(
                    text = item.title,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 16.sp
                )
                Text(
                    text = item.dateTime,
                    color = Color.Gray,
                    fontSize = 14.sp
                )
            }

            // --- 2. TAMBAHKAN TOMBOL HAPUS DI SINI ---
            IconButton(
                onClick = { onDeleteClick(item.id) }, // Panggil lambda dengan ID item
                modifier = Modifier.size(24.dp) // Ukuran tombol
            ) {
                Icon(
                    imageVector = Icons.Default.Close, // Ikon 'X'
                    contentDescription = "Hapus Notifikasi",
                    tint = Color.Gray // Warna ikon
                )
            }
        }
    }
}

@Preview
@Composable
fun NotificationItemPreview() {
    PrediAITheme {
        NotificationItem(
            item = NotificationItemData(
                id = "dummy-id", // Tambah ID dummy
                title = "Ayo pergi ke dokter untuk kontrol gula darah",
                dateTime = "17 Agustus • 4:00 PM"
            ),
            onDeleteClick = {} // Tambahkan lambda kosong untuk preview
        )
    }
}