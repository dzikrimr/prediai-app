package com.example.prediai.presentation.scan.tabs

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.prediai.R
import com.example.prediai.data.remote.scan.AnalysisResponse
import com.example.prediai.presentation.scan.comps.DetailItemWithCircle

@Composable
fun DetailsScreen(data: AnalysisResponse?) {
    if (data == null) {
        return
    }

    val nextStepsText: String
    val conclusionText: String

    when {
        data.riskPercentage > 70 -> { // Risiko Tinggi
            nextStepsText = "Segera jadwalkan konsultasi dengan dokter Anda. Tes glukosa darah dan pemeriksaan komprehensif sangat disarankan untuk diagnosis yang akurat."
            conclusionText = "Berdasarkan analisis citra dan kuesioner, ditemukan indikasi kuat prediabetes/diabetes. Konsultasi medis diperlukan untuk diagnosis dan penanganan lebih lanjut."
        }
        data.riskPercentage > 50 -> { // Risiko Sedang
            nextStepsText = "Pantau perubahan selama 2-3 minggu ke depan. Jadwalkan pemeriksaan rutin dengan penyedia layanan kesehatan Anda untuk mendiskusikan temuan ini."
            conclusionText = "Berdasarkan analisis citra dan kuesioner, ditemukan beberapa indikator yang mengarah pada prediabetes. Disarankan untuk menjaga gaya hidup sehat dan melakukan pemeriksaan medis."
        }
        else -> { // Risiko Rendah
            nextStepsText = "Pantau perubahan selama 2-3 minggu ke depan. Jadwalkan pemeriksaan rutin dengan penyedia layanan kesehatan Anda untuk mendapatkan panduan profesional."
            conclusionText = "Berdasarkan analisis citra dan kuesioner, tidak ditemukan indikasi kuat diabetes. Namun, tetap disarankan untuk melakukan pemeriksaan rutin untuk pencegahan."
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF5F5F5))
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        Spacer(modifier = Modifier.height(8.dp))

        // Risk Factors Identified
        Surface(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            color = Color.White,
            shadowElevation = 2.dp
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(bottom = 12.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Warning,
                        contentDescription = "Warning",
                        tint = Color(0xFFFFC107),
                        modifier = Modifier
                            .size(20.dp)
                            .padding(end = 8.dp)
                    )
                    Text(
                        text = "Faktor Risiko Teridentifikasi", // Diubah ke Bahasa Indonesia
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color(0xFF212121)
                    )
                }
                data.riskFactors.forEach { factor ->
                    DetailItemWithCircle(
                        text = factor,
                        circleColor = Color(0xFFFACC15)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Lifestyle Suggestions (Tidak ada di kode sebelumnya, saya tambahkan sebagai contoh)
        Surface(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            color = Color.White,
            shadowElevation = 2.dp
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(bottom = 12.dp)
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_heart),
                        contentDescription = "Heart",
                        tint = Color(0xFF00B4A3),
                        modifier = Modifier
                            .size(20.dp)
                            .padding(end = 8.dp)
                    )
                    Text(
                        text = "Saran Gaya Hidup", // Diubah ke Bahasa Indonesia
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color(0xFF212121)
                    )
                }

                DetailItemWithCircle(
                    text = "Jaga nutrisi seimbang dan perbanyak vitamin",
                    circleColor = Color(0xFF00B4A3)
                )
                DetailItemWithCircle(
                    text = "Olahraga teratur dan kelola stres",
                    circleColor = Color(0xFF00B4A3)
                )
                DetailItemWithCircle(
                    text = "Hindari kebiasaan dan bahan kimia berbahaya",
                    circleColor = Color(0xFF00B4A3)
                )
            }
        }


        Spacer(modifier = Modifier.height(16.dp))

        // --- CARD BARU: NEXT STEPS ---
        Surface(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            color = Color.White,
            shadowElevation = 2.dp
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(bottom = 12.dp)
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_steps),
                        contentDescription = "Steps",
                        tint = Color(0xFF7E57C2),
                        modifier = Modifier
                            .size(20.dp)
                            .padding(end = 8.dp)
                    )
                    Text(
                        text = "Langkah Selanjutnya",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color(0xFF212121)
                    )
                }

                Text(
                    text = nextStepsText, // Menggunakan teks dinamis
                    fontSize = 14.sp,
                    color = Color(0xFF424242),
                    lineHeight = 20.sp
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // --- CARD BARU: KESIMPULAN ---
        Surface(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            color = Color(0xFFE3F2FD),
            shadowElevation = 1.dp
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_conclusion),
                        contentDescription = "Conclusion",
                        tint = Color(0xFF1976D2),
                        modifier = Modifier
                            .size(24.dp)
                            .padding(end = 8.dp)
                    )
                    Text(
                        text = "Kesimpulan",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color(0xFF1976D2)
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = conclusionText, // Menggunakan teks dinamis
                    fontSize = 14.sp,
                    color = Color(0xFF424242),
                    lineHeight = 20.sp
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))
    }
}