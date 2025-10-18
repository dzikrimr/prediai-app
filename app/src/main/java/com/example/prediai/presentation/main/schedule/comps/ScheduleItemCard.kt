package com.example.prediai.presentation.main.schedule.comps

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.prediai.R
import com.example.prediai.domain.model.ScheduleItem
import com.example.prediai.domain.model.ScheduleStatus
import com.example.prediai.domain.model.ScheduleType
import com.example.prediai.presentation.theme.PrediAITheme

@Composable
fun ScheduleItemCard(
    item: ScheduleItem,
    index: Int // <-- 1. TAMBAHKAN PARAMETER INDEX
) {
    // --- 1. LOGIKA STATUS (Tetap Sama) ---
    val (statusText, statusBgColor, statusTextColor) = when (item.status) {
        ScheduleStatus.MENDATANG -> Triple("Mendatang", Color(0xFFE0F2F1), Color(0xFF00B4A3))
        ScheduleStatus.SELESAI -> Triple("Selesai", Color(0xFFE8F5E9), Color(0xFF4CAF50))
        ScheduleStatus.TERLEWAT -> Triple("Terlewat", Color(0xFFFFEBEE), Color(0xFFE53935))
    }

    // --- 2. LOGIKA IKON & NAMA TIPE (Tetap Sama) ---
    val iconId = when (item.type) {
        ScheduleType.CEK_GULA -> R.drawable.ic_blood_drop
        ScheduleType.KONSULTASI -> R.drawable.ic_consultation
        ScheduleType.OLAHRAGA -> R.drawable.ic_exercise
        ScheduleType.MINUM_OBAT -> R.drawable.ic_pills
        ScheduleType.SKRINING_AI -> R.drawable.ic_camera // (Asumsi nama ikon)
        ScheduleType.JADWAL_MAKAN -> R.drawable.ic_food_avoid // <-- TAMBAHKAN INI (Ganti dgn ikonmu)
        ScheduleType.CEK_TENSI -> R.drawable.ic_blood_pressure // <-- TAMBAHKAN INI (Ganti dgn ikonmu)
    }

    val typeDisplayName = when (item.type) {
        ScheduleType.CEK_GULA -> "Cek Gula"
        ScheduleType.KONSULTASI -> "Konsultasi"
        ScheduleType.OLAHRAGA -> "Olahraga"
        ScheduleType.MINUM_OBAT -> "Minum Obat"
        ScheduleType.SKRINING_AI -> "Skrining AI"
        ScheduleType.JADWAL_MAKAN -> "Jadwal Makan" // <-- TAMBAHKAN INI
        ScheduleType.CEK_TENSI -> "Cek Tensi Darah" // <-- TAMBAHKAN INI
    }

    // --- 3. LOGIKA WARNA BERURUTAN (Perubahan di sini) ---

    // Daftar warna kustom yang kamu berikan
    val colorPairs = remember {
        listOf(
            // 1. 00B4A3 (Teal)
            Pair(Color(0xFF00B4A3), Color(0xFFE0F2F1)),
            // 2. A78BFA (Purple)
            Pair(Color(0xFFA78BFA), Color(0xFFF5F3FF)),
            // 3. FB923C (Orange)
            Pair(Color(0xFFFB923C), Color(0xFFFFEEDF)),
            // 4. C084FC (Pink-Purple)
            Pair(Color(0xFFC084FC), Color(0xFFFAF5FF))
        )
    }

    // Ambil warna berdasarkan index % ukuran list (misal index 4 jadi 0, 5 jadi 1, dst.)
    val (typeColor, iconBgColor) = colorPairs[index % colorPairs.size]

    // --- 4. LAYOUT CARD ---
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(IntrinsicSize.Min),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Bar Vertikal di Kiri (Warna berurutan)
            Box(
                modifier = Modifier
                    .width(6.dp) // <-- 4. GARIS LEBIH TIPIS (sebelumnya 8.dp)
                    .fillMaxHeight()
                    .background(
                        typeColor, // <-- Menggunakan warna berurutan
                        shape = RoundedCornerShape(topStart = 20.dp, bottomStart = 20.dp)
                    )
            )

            // Konten Utama (Ikon + Teks)
            Row(
                modifier = Modifier
                    .padding(horizontal = 16.dp, vertical = 12.dp) // Padding vertikal kecil
                    .weight(1f),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Kotak Ikon (Warna berurutan)
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .background(iconBgColor, shape = RoundedCornerShape(12.dp)), // <-- Latar berurutan
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        painter = painterResource(id = iconId), // <-- Ikon tetap sesuai Tipe
                        contentDescription = typeDisplayName,
                        tint = typeColor, // <-- Warna ikon berurutan
                        modifier = Modifier.size(24.dp)
                    )
                }

                Spacer(Modifier.width(16.dp))

                // Kolom Teks (Tidak berubah)
                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text(
                        text = typeDisplayName, // <-- Nama tetap sesuai Tipe
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                        color = Color.Black
                    )
                    Text(
                        text = item.description,
                        fontSize = 14.sp,
                        color = Color.Gray
                    )
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(top = 4.dp)
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_time),
                            contentDescription = "Time",
                            tint = Color.Gray,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(Modifier.width(4.dp))
                        Text(
                            text = item.time,
                            fontSize = 12.sp,
                            color = Color.Gray
                        )
                        Spacer(Modifier.width(16.dp))
                        // Chip Status
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(50))
                                .background(statusBgColor)
                                .padding(horizontal = 10.dp, vertical = 4.dp)
                        ) {
                            Text(
                                text = statusText,
                                color = statusTextColor,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }
                }
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun ScheduleItemCardPreview() {
    PrediAITheme {
        Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
            ScheduleItemCard(
                item = ScheduleItem(
                    type = ScheduleType.CEK_GULA,
                    description = "Pagi hari",
                    time = "07:00",
                    status = ScheduleStatus.MENDATANG
                ),
                index = 0 // Contoh index 0 (Teal)
            )
            ScheduleItemCard(
                item = ScheduleItem(
                    type = ScheduleType.OLAHRAGA,
                    description = "Sore hari",
                    time = "16:00",
                    status = ScheduleStatus.SELESAI
                ),
                index = 1 // Contoh index 1 (Purple)
            )
            ScheduleItemCard(
                item = ScheduleItem(
                    type = ScheduleType.MINUM_OBAT,
                    description = "Malam hari",
                    time = "20:00",
                    status = ScheduleStatus.TERLEWAT
                ),
                index = 2 // Contoh index 2 (Orange)
            )
        }
    }
}