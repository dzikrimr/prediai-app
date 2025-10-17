package com.example.prediai.presentation.scan.tabs

import android.graphics.BitmapFactory
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.prediai.R
import com.example.prediai.data.remote.scan.AnalysisResponse

@Composable
fun OverviewScreen(
    data: AnalysisResponse?,
    nailImage: ByteArray?,
    tongueImage: ByteArray?,
    onExportClick: () -> Unit = {},
    onSaveToHistory: () -> Unit = {}
) {
    if (data == null) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("Memuat data hasil...")
        }
        return
    }

    val riskLevelText: String
    val riskIllustration: Int
    val riskIconColor: Color
    val riskBgColor: Color

    when {
        data.riskPercentage > 70 -> {
            riskLevelText = "Tinggi"
            riskIllustration = R.drawable.ils_high
            riskIconColor = Color(0xFFEF4444) // Merah
            riskBgColor = Color(0xFFFEE2E2)
        }
        data.riskPercentage > 50 -> {
            riskLevelText = "Sedang"
            riskIllustration = R.drawable.ils_moderate
            riskIconColor = Color(0xFFF59E0B) // Kuning/Amber
            riskBgColor = Color(0xFFFEF9C3)
        }
        else -> {
            riskLevelText = "Rendah"
            riskIllustration = R.drawable.ils_low
            riskIconColor = Color(0xFF10B981) // Hijau
            riskBgColor = Color(0xFFD1FAE5)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF5F5F5))
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 16.dp)
    ) {
        Spacer(modifier = Modifier.height(16.dp))

        Surface(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            color = Color.White,
            shadowElevation = 2.dp
        ) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(riskBgColor),
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        painter = painterResource(id = riskIllustration),
                        contentDescription = "Ilustrasi Risiko",
                        modifier = Modifier
                            .size(180.dp)
                    )
                }

                Column(
                    modifier = Modifier
                        .padding(horizontal = 24.dp, vertical = 20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Risiko $riskLevelText",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = riskIconColor
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = Color(0xFF00B4A3).copy(alpha = 0.1f)
                    ) {
                        Text(
                            text = "Confidence Score: %.2f".format(data.riskPercentage) + "%",
                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Medium,
                            color = Color(0xFF008B7D)
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // --- TEKS DIUBAH KE BAHASA INDONESIA ---
        Text(
            text = "Ringkasan Analisis",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF212121),
            modifier = Modifier.padding(bottom = 12.dp)
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            AnalysisImage(
                modifier = Modifier.weight(1f),
                imageBytes = nailImage,
                label = "Kuku", // Diubah
                riskColor = riskIconColor
            )
            AnalysisImage(
                modifier = Modifier.weight(1f),
                imageBytes = tongueImage,
                label = "Lidah", // Diubah
                riskColor = riskIconColor
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = "Area ini menunjukkan potensi indikator yang memerlukan perhatian",
            fontSize = 14.sp,
            color = Color(0xFF757575),
            textAlign = TextAlign.Center, // Diubah ke Center
            modifier = Modifier
                .fillMaxWidth() // Tambahkan fillMaxWidth agar textAlign berfungsi
                .padding(bottom = 16.dp)
        )

        // --- TOMBOL SAVE TO HISTORY DIUPDATE DENGAN IKON & TEKS BARU ---
        OutlinedButton(
            onClick = onSaveToHistory,
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.outlinedButtonColors(
                contentColor = Color(0xFF00B4A3)
            ),
            border = BorderStroke(1.dp, Color(0xFF00B4A3))
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_bookmark),
                contentDescription = "Simpan ke Riwayat",
                modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "Simpan ke Riwayat",
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        // --- TOMBOL EKSPOR HASIL DIUPDATE DENGAN IKON ---
        Button(
            onClick = onExportClick,
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF00B4A3)
            )
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_download),
                contentDescription = "Ekspor Hasil",
                modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "Ekspor Hasil",
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium
            )
        }

        Spacer(modifier = Modifier.height(24.dp))
    }
}


@Composable
fun AnalysisImage(
    modifier: Modifier = Modifier,
    imageBytes: ByteArray?,
    label: String,
    riskColor: Color
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1f)
        ) {
            val bitmap = remember(imageBytes) {
                imageBytes?.let { BitmapFactory.decodeByteArray(it, 0, it.size) }
            }

            if (bitmap != null) {
                Image(
                    bitmap = bitmap.asImageBitmap(),
                    contentDescription = "Pindaian $label",
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(RoundedCornerShape(12.dp)),
                    contentScale = ContentScale.Crop
                )
            } else {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color(0xFFE0E0E0), RoundedCornerShape(12.dp))
                )
            }

            Box(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(8.dp)
                    .size(24.dp)
                    .background(riskColor, CircleShape)
            )
        }

        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Pindaian $label", // Diubah
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium,
            color = Color.DarkGray,
            textAlign = TextAlign.Center
        )
    }
}