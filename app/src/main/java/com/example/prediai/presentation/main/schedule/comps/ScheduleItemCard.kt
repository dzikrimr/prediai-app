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
// HAPUS import 'ScheduleStatus'
import com.example.prediai.domain.model.ScheduleType
import com.example.prediai.presentation.theme.PrediAITheme
// --- IMPORT BARU UNTUK WAKTU ---
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter

@Composable
fun ScheduleItemCard(
    item: ScheduleItem,
    index: Int
) {
    // --- 1. LOGIKA KALKULASI STATUS BARU ---
    // Logika ini menghitung status berdasarkan waktu sekarang.
    val (statusText, statusBgColor, statusTextColor) = try {
        // Parse tanggal ("yyyy-MM-dd") dan waktu ("HH:mm")
        val itemDate = LocalDate.parse(item.date, DateTimeFormatter.ISO_LOCAL_DATE)
        val itemTime = LocalTime.parse(item.time, DateTimeFormatter.ISO_LOCAL_TIME)

        val itemDateTime = LocalDateTime.of(itemDate, itemTime)
        val now = LocalDateTime.now()

        if (itemDateTime.isBefore(now)) {
            // Waktu sudah lewat -> Selesai
            Triple("Selesai", Color(0xFFDCFCE7), Color(0xFF16A34A))
        } else {
            // Waktu di masa depan -> Mendatang
            Triple("Mendatang", Color(0xFFDBEAFE), Color(0xFF2563EB))
        }
    } catch (e: Exception) {
        // Fallback jika format data salah
        Triple("Error", Color(0xFFFFEBEE), Color(0xFFE53935))
    }

    // --- 2. LOGIKA IKON & NAMA TIPE (Tambahkan tipe baru) ---
    val iconId = when (item.type) {
        ScheduleType.CEK_GULA -> R.drawable.ic_blood_drop
        ScheduleType.KONSULTASI -> R.drawable.ic_consultation
        ScheduleType.OLAHRAGA -> R.drawable.ic_exercise
        ScheduleType.MINUM_OBAT -> R.drawable.ic_pills
        ScheduleType.SKRINING_AI -> R.drawable.ic_camera // (Asumsi nama ikon)
        ScheduleType.JADWAL_MAKAN -> R.drawable.ic_food_avoid // (Asumsi nama ikon)
        ScheduleType.CEK_TENSI -> R.drawable.ic_blood_pressure // (Asumsi nama ikon)
    }

    val typeDisplayName = when (item.type) {
        ScheduleType.CEK_GULA -> "Cek Gula"
        ScheduleType.KONSULTASI -> "Konsultasi"
        ScheduleType.OLAHRAGA -> "Olahraga"
        ScheduleType.MINUM_OBAT -> "Minum Obat"
        ScheduleType.SKRINING_AI -> "Skrining AI"
        ScheduleType.JADWAL_MAKAN -> "Jadwal Makan"
        ScheduleType.CEK_TENSI -> "Cek Tensi Darah"
    }

    // --- 3. LOGIKA WARNA BERURUTAN (Tetap Sama) ---
    val colorPairs = remember {
        listOf(
            Pair(Color(0xFF00B4A3), Color(0xFFE0F2F1)),
            Pair(Color(0xFFA78BFA), Color(0xFFF5F3FF)),
            Pair(Color(0xFFFB923C), Color(0xFFFFEEDF)),
            Pair(Color(0xFFC084FC), Color(0xFFFAF5FF))
        )
    }
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
            // Bar Vertikal di Kiri
            Box(
                modifier = Modifier
                    .width(6.dp)
                    .fillMaxHeight()
                    .background(
                        typeColor,
                        shape = RoundedCornerShape(topStart = 20.dp, bottomStart = 20.dp)
                    )
            )

            // Konten Utama
            Row(
                modifier = Modifier
                    .padding(horizontal = 16.dp, vertical = 12.dp)
                    .weight(1f),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Kotak Ikon
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .background(iconBgColor, shape = RoundedCornerShape(12.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        painter = painterResource(id = iconId),
                        contentDescription = typeDisplayName,
                        tint = typeColor,
                        modifier = Modifier.size(24.dp)
                    )
                }

                Spacer(Modifier.width(16.dp))

                // Kolom Teks
                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text(
                        text = typeDisplayName,
                        fontWeight = FontWeight.SemiBold,
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

                        // --- 5. CHIP STATUS DENGAN WARNA BARU ---
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(50))
                                .background(statusBgColor) // <- Warna baru
                                .padding(horizontal = 10.dp, vertical = 4.dp)
                        ) {
                            Text(
                                text = statusText, // <- Teks baru
                                color = statusTextColor, // <- Warna baru
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