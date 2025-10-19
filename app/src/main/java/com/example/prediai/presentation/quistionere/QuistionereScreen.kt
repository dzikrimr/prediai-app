package com.example.prediai.presentation.quistionere

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.prediai.R
import com.example.prediai.presentation.common.TopBar
import com.example.prediai.presentation.quistionere.comps.SectionCard

@Composable
fun QuistionereScreen(
    onBackClick: () -> Unit = {},
    onSaveClick: () -> Unit = {},
    onCancelClick: () -> Unit = {}
) {
    val viewModel: QuestionereViewModel = hiltViewModel()
    val state by viewModel.uiState.collectAsState()
    val answers = viewModel.answers

    LaunchedEffect(state.isSaveSuccess) {
        if (state.isSaveSuccess) {
            viewModel.onSaveComplete()
            onSaveClick()
        }
    }

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

        if (state.isLoading) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
            ) {
                Spacer(modifier = Modifier.height(16.dp))

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

                // Section 1: Pertanyaan Gejala Fisik (DISAMAKAN)
                SectionCard(
                    title = "Pertanyaan Gejala Fisik",
                    questions = listOf(
                        "Apakah Anda sering merasa mudah lelah tanpa aktivitas berat?" to listOf("Ya", "Tidak"),
                        "Apakah luka pada tubuh Anda lama sembuhnya?" to listOf("Ya", "Tidak"),
                        "Apakah Anda sering mengalami penglihatan kabur?" to listOf("Ya", "Tidak"),
                        "Apakah Anda sering merasa kesemutan di tangan atau kaki?" to listOf("Ya", "Tidak"),
                        "Apakah Anda sering merasa haus berlebihan?" to listOf("Ya", "Tidak")
                    ),
                    answers = answers,
                    onAnswerChange = viewModel::onAnswerChange
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Section 2: Pertanyaan Riwayat Medis (DISAMAKAN)
                SectionCard(
                    title = "Pertanyaan Riwayat Medis",
                    questions = listOf(
                        "Apakah ada anggota keluarga yang memiliki riwayat diabetes?" to listOf("Ya", "Tidak"),
                        "Apakah Anda pernah didiagnosis memiliki tekanan darah tinggi?" to listOf("Ya", "Tidak"),
                        "Apakah Anda memiliki riwayat kolesterol tinggi atau obesitas?" to listOf("Ya", "Tidak"),
                        "Apakah Anda sedang mengonsumsi obat tertentu (misalnya steroid) secara rutin?" to listOf("Ya", "Tidak")
                    ),
                    answers = answers,
                    onAnswerChange = viewModel::onAnswerChange
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Section 3: Pertanyaan Gaya Hidup (DISAMAKAN)
                SectionCard(
                    title = "Pertanyaan Gaya Hidup",
                    questions = listOf(
                        "Seberapa sering Anda melakukan aktivitas fisik dalam seminggu?" to listOf("Jarang", "1x", "2-3x", "> 3x"),
                        "Seberapa sering Anda mengonsumsi makanan atau minuman tinggi gula?" to listOf("Jarang", "Kadang", "Sering", "Setiap Hari"),
                        "Apakah Anda memiliki kebiasaan merokok atau minum alkohol?" to listOf("Ya", "Tidak"),
                        "Berapa jam tidur rata-rata Anda setiap malam?" to listOf("< 5 jam", "6-7 Jam", "> 8 jam")
                    ),
                    answers = answers,
                    onAnswerChange = viewModel::onAnswerChange
                )

                Spacer(modifier = Modifier.height(24.dp))

                // Tombol Simpan dan Batal
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Button(
                        onClick = { viewModel.saveChanges() },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF00B4A3)
                        ),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text("Simpan", fontSize = 16.sp, fontWeight = FontWeight.SemiBold, color = Color.White)
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
                            brush = SolidColor(Color(0xFFE0E0E0))
                        )
                    ) {
                        Text("Batal", fontSize = 16.sp, fontWeight = FontWeight.SemiBold)
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
            }
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