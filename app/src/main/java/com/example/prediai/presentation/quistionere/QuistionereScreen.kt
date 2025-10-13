package com.example.prediai.presentation.quistionere

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.prediai.R
import com.example.prediai.presentation.common.TopBar
import com.example.prediai.presentation.quistionere.comps.SectionCard

@Composable
fun QuistionereScreen(
    onBackClick: () -> Unit = {},
    onSaveClick: (Map<String, String>) -> Unit = {},
    onCancelClick: () -> Unit = {}
) {
    val answers = remember { mutableStateMapOf<String, String>() }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF5F9FA))
    ) {
        TopBar(
            title = "Kuisioner",
            subtitle = "Quistionere",
            onBackClick = onBackClick
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            // Edit Kuisioner Card
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color.White
                ),
                elevation = CardDefaults.cardElevation(
                    defaultElevation = 2.dp
                )
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_info),
                        contentDescription = null,
                        tint = Color(0xFF00B4A3),
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Edit Kuisioner",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF2D3142)
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Section 1: Pertanyaan Gejala Fisik
            SectionCard(
                title = "Pertanyaan Gejala Fisik",
                questions = listOf(
                    "Apakah Anda sering merasa mudah lelah tanpa aktivitas berat?" to listOf("Ya", "Tidak"),
                    "Apakah luka pada tubuh Anda lama sembuhnya?" to listOf("Ya", "Tidak"),
                    "Apakah Anda sering mengalami penglihatan kabur?" to listOf("Ya", "Tidak"),
                    "Apakah Anda sering merasa kesemutan di tangan atau kaki?" to listOf("Ya", "Tidak"),
                    "Apakah Anda sering merasa haus yang berlebihan?" to listOf("Ya", "Tidak")
                ),
                answers = answers,
                keyPrefix = "gejala_"
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Section 2: Pertanyaan Riwayat Medis
            SectionCard(
                title = "Pertanyaan Riwayat Medis",
                questions = listOf(
                    "Apakah ada riwayat diabetes dalam keluarga Anda?" to listOf("Ya", "Tidak"),
                    "Apakah Anda pernah didiagnosis dengan penyakit jantung?" to listOf("Ya", "Tidak"),
                    "Apakah Anda memiliki tekanan darah tinggi?" to listOf("Ya", "Tidak"),
                    "Apakah Anda pernah mengalami komplikasi saat kehamilan?" to listOf("Ya", "Tidak")
                ),
                answers = answers,
                keyPrefix = "riwayat_"
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Section 3: Pertanyaan Gaya Hidup
            SectionCard(
                title = "Pertanyaan Gaya Hidup",
                questions = listOf(
                    "Seberapa sering Anda berolahraga dalam seminggu?" to listOf("Tidak pernah", "1-2 kali", "3-4 kali", "Lebih dari 4 kali"),
                    "Bagaimana pola makan Anda sehari-hari?" to listOf("Tidak teratur", "Cukup teratur", "Teratur", "Sangat teratur"),
                    "Apakah Anda merokok?" to listOf("Ya", "Tidak"),
                    "Berapa jam Anda tidur setiap malam?" to listOf("< 6 jam", "6-8 jam", "> 8 jam")
                ),
                answers = answers,
                keyPrefix = "gaya_hidup_"
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Bottom Buttons
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Button(
                    onClick = { onSaveClick(answers) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF00B4A3)
                    ),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text(
                        text = "Simpan",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color.White
                    )
                }

                OutlinedButton(
                    onClick = onCancelClick,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    colors = ButtonDefaults.outlinedButtonColors(
                        containerColor = Color.White,
                        contentColor = Color(0xFF2D3142)
                    ),
                    shape = RoundedCornerShape(12.dp),
                    border = ButtonDefaults.outlinedButtonBorder.copy(
                        brush = androidx.compose.ui.graphics.SolidColor(Color(0xFFE0E0E0))
                    )
                ) {
                    Text(
                        text = "Batal",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}


@Preview
@Composable
fun QuistionereScreenPreview() {
    QuistionereScreen(
        onBackClick = {},
        onSaveClick = {},
        onCancelClick = {}
    )
}