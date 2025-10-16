package com.example.prediai.presentation.profile.help

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.KeyboardArrowDown
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.prediai.R
import com.example.prediai.presentation.common.TopBar

// 1. Data Model diubah untuk menggunakan ID resource (Int) dan warna tint
data class HelpItem(
    val iconResId: Int,
    val iconTint: Color,
    val iconBackgroundColor: Color,
    val title: String,
    val subtitle: String,
    val content: List<String>
)

// 2. Sample data diupdate dengan resource drawable dan warna tint baru
val helpItemsList = listOf(
    HelpItem(
        iconResId = R.drawable.ic_camera,
        iconTint = Color(0xFF9B59B6),
        iconBackgroundColor = Color(0xFFEFE6FF),
        title = "Cara Menggunakan Scan Kuku & Lidah",
        subtitle = "Panduan lengkap fitur screening",
        content = listOf(
            "Pergi ke halaman kamera dan ikuti petunjuk",
            "Pastikan izin kamera telah diizinkan pada aplikasi",
            "Ikuti kotak panduan yang terdapat pada preview kamera",
            "Biarkan aplikasi mengangkap gambar ketika objek yang terdeteksi dianggap sesuai",
            "Ubah kamera depan atau kamera belakang dengan tombol rotate di sebelah tombol kamera",
            "Hasil analisa akan muncul apabila telah melakukan scan lidah dan kuku"
        )
    ),
    HelpItem(
        iconResId = R.drawable.ic_file,
        iconTint = Color(0xFFF39C12),
        iconBackgroundColor = Color(0xFFFFF4DE),
        title = "Mengunggah Dokumen di Labs",
        subtitle = "Upload hasil lab dan dokumen medis",
        content = listOf(
            "Pergi ke halaman labs",
            "Pastikan izin unggah file telah diizinkan pada perangkat",
            "File yang bisa diunggah adalah pdf, doc, txt, jpg, png dengan maksimal jumlah file adalah satu",
            "Lakukan mulai analisa jika file telah diunggah",
            "Tunggu proses analisa hingga selesai."
        )
    ),
    HelpItem(
        iconResId = R.drawable.ic_calendar_square,
        iconTint = Color(0xFF3498DB),
        iconBackgroundColor = Color(0xFFE0F3FF),
        title = "Mengatur Jadwal & Pengingat",
        subtitle = "Notifikasi dan reminder kesehatan",
        content = listOf(
            "Untuk memulai, ketuk tombol 'Tambahkan Jadwal' di halaman beranda.",
            "Selanjutnya, ketuk ikon tambah (+) di pojok kanan bawah untuk membuat jadwal baru.",
            "Pilih tanggal yang Anda inginkan dari kalender yang ditampilkan.",
            "Isi informasi yang diperlukan seperti Jenis Jadwal dan Waktu. Anda juga bisa menambahkan Catatan (opsional).",
            "Setelah semua data terisi, ketuk tombol 'Simpan' untuk menyelesaikan."
        )
    ),
    HelpItem(
        iconResId = R.drawable.ic_consult,
        iconTint = Color(0xFF2ECC71),
        iconBackgroundColor = Color(0xFFD9F7EA),
        title = "Konsultasi & Dokter Terdekat",
        subtitle = "Terhubung dengan tenaga medis",
        content = listOf(
            "Untuk memulai konsultasi, buka halaman Chatbot dan ketuk tombol 'Konsultasi dengan dokter langsung'.",
            "Sebagai alternatif, Anda juga bisa memulai konsultasi setelah menyelesaikan analisis di halaman Labs.",
            "Harap diperhatikan, Anda akan diarahkan ke tautan eksternal (di luar aplikasi) untuk melanjutkan sesi konsultasi."
        )
    )
)

@Composable
fun HelpCenterScreen(navController: NavController) {
    // State untuk melacak item mana yang sedang terbuka (expanded)
    var expandedItemIndex by remember { mutableStateOf<Int?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF5F5F5)) // Warna background utama
    ) {
        // Menggunakan TopBar kustom yang Anda sediakan
        TopBar(
            title = "Pusat Bantuan",
            onBackClick = { navController.popBackStack() }
        )

        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            contentPadding = PaddingValues(top = 16.dp, bottom = 24.dp)
        ) {
            itemsIndexed(helpItemsList) { index, item ->
                ExpandableHelpCard(
                    item = item,
                    isExpanded = expandedItemIndex == index,
                    onClick = {
                        expandedItemIndex = if (expandedItemIndex == index) null else index
                    }
                )
            }
        }
    }
}

@Composable
fun ExpandableHelpCard(
    item: HelpItem,
    isExpanded: Boolean,
    onClick: () -> Unit
) {
    // Animasi untuk rotasi ikon panah
    val rotationAngle by animateFloatAsState(targetValue = if (isExpanded) 180f else 0f)

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = LocalIndication.current,
                    onClick = onClick
                )
                .padding(16.dp)
        ) {
            // Bagian Header Kartu (Ikon, Judul, Panah)
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(item.iconBackgroundColor),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        painter = painterResource(id = item.iconResId),
                        contentDescription = item.title,
                        tint = item.iconTint,
                        modifier = Modifier.size(24.dp)
                    )
                }
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = item.title,
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                        color = Color.DarkGray
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = item.subtitle,
                        fontSize = 14.sp,
                        color = Color.Gray
                    )
                }
                Icon(
                    imageVector = Icons.Rounded.KeyboardArrowDown,
                    contentDescription = "Expand",
                    modifier = Modifier.rotate(rotationAngle)
                )
            }

            // Konten yang bisa diperluas (muncul saat isExpanded true)
            AnimatedVisibility(visible = isExpanded) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp)
                ) {
                    // --- PERUBAHAN DI SINI ---
                    item.content.forEachIndexed { index, text ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 8.dp)
                        ) {
                            // Teks untuk Nomor
                            Text(
                                text = "${index + 1}.",
                                fontSize = 14.sp,
                                color = Color.Gray,
                                modifier = Modifier.width(24.dp) // Beri lebar tetap
                            )

                            // Teks untuk Isi
                            Text(
                                text = text,
                                fontSize = 14.sp,
                                color = Color.Gray,
                                lineHeight = 20.sp
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
private fun HelpCenterScreenPreview() {
    HelpCenterScreen(navController = NavController(LocalContext.current))
}