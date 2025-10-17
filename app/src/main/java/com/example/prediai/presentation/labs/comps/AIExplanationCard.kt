package com.example.prediai.presentation.labs.comps

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
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

@Composable
fun AIExplanationCard(
    onConsultClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White // ganti backgroundColor -> containerColor
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 2.dp // ganti elevation = 2.dp
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .background(
                            color = Color(0x33A78BFA),
                            shape = RoundedCornerShape(8.dp)
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_ai),
                        contentDescription = "AI",
                        tint = Color(0xFF7C4DFF),
                        modifier = Modifier.size(24.dp)
                    )
                }
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "AI Explanation & Next Steps",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF2D3142)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Berdasarkan hasil analisis, kadar glukosa Anda menunjukkan Anda berada di rentang pradiabetes. " +
                        "Ini adalah sebuah tanda peringatan penting, di mana kadar gula darah Anda lebih tinggi dari normal, namun belum cukup tinggi untuk didiagnosis sebagai diabetes tipe 2. Anggap ini sebagai kesempatan untuk mengambil tindakan pencegahan.\n\n" +
                        "Kabar baiknya, kondisi ini seringkali dapat dikelola dan bahkan dikembalikan ke rentang normal melalui perubahan gaya hidup yang konsisten, seperti memperbaiki pola makan, meningkatkan aktivitas fisik, dan menjaga berat badan ideal.\n\n" +
                        "Kami sangat menyarankan Anda untuk segera berkonsultasi dengan dokter atau tenaga kesehatan profesional. Mereka dapat memberikan diagnosis yang akurat, menyusun rencana pencegahan yang dipersonalisasi sesuai kondisi Anda, dan menentukan apakah diperlukan pemantauan atau tes lebih lanjut.",
                fontSize = 14.sp,
                lineHeight = 22.sp,
                color = Color(0xFF5D5D5D)
            )

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = onConsultClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF00BFA5) // ganti backgroundColor -> containerColor
                ),
                shape = RoundedCornerShape(10.dp),
                elevation = ButtonDefaults.buttonElevation(
                    defaultElevation = 0.dp // ganti elevation() -> buttonElevation()
                )
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_consult),
                    contentDescription = "Doctor",
                    tint = Color.White,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Konsultasi ke Dokter",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.White
                )
            }
        }
    }
}