package com.example.prediai.presentation.labs

import android.content.Context
import android.net.Uri
import android.provider.OpenableColumns
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.prediai.R
import com.example.prediai.presentation.common.TopBar
import com.example.prediai.presentation.labs.comps.AnalysisItemCard
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

@Composable
fun LabsScreen(
    onBackClick: () -> Unit,
    onNavigateToResult: (fileName: String, uploadDate: String) -> Unit,
    viewModel: LabsViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    val mimeTypes = arrayOf("application/pdf", "text/plain", "image/jpeg", "image/png")

    val filePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.OpenDocument(),
        onResult = { uri -> viewModel.onFileSelected(uri) }
    )

    LaunchedEffect(uiState.analysisComplete) {
        if (uiState.analysisComplete) {
            val currentDate = LocalDate.now().format(
                DateTimeFormatter.ofPattern("dd MMMM yyyy", Locale.forLanguageTag("id-ID"))
            )
            onNavigateToResult(uiState.selectedFileName ?: "File Tidak Dikenal", currentDate)
            viewModel.onNavigationComplete()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF9FAFB))
    ) {
        TopBar(
            title = "Labs",
            subtitle = "Smart Lab Analysis",
            onBackClick = onBackClick
        )

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            contentPadding = PaddingValues(bottom = 16.dp)
        ) {
            item {
                Spacer(modifier = Modifier.height(8.dp))
                // Card gradien
                Box(
                    modifier = Modifier.fillMaxWidth().height(150.dp).clip(RoundedCornerShape(16.dp))
                        .background(brush = Brush.verticalGradient(colors = listOf(Color(0xCC00B4A3), Color(0x33A78BFA))))
                ) {
                    Image(painter = painterResource(id = R.drawable.bg_lab), contentDescription = null, modifier = Modifier.align(Alignment.CenterEnd).size(180.dp).offset(x = 40.dp), alpha = 0.8f)
                    Column(modifier = Modifier.fillMaxWidth().padding(24.dp)) {
                        Text(text = "Unngah Hasil Lab Anda", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = Color.White)
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(text = "Analisis Lab Cerdas", fontSize = 14.sp, fontWeight = FontWeight.SemiBold, color = Color.White)
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(text = "Unggah hasil lab dan dapatkan wawasan berbasis AI untuk lebih memahami data kesehatan Anda", fontSize = 12.sp, color = Color.White.copy(alpha = 0.9f), lineHeight = 18.sp)
                    }
                }
            }

            item {
                // Box preview file
                Box(modifier = Modifier.fillMaxWidth().height(120.dp), contentAlignment = Alignment.Center) {
                    Canvas(modifier = Modifier.fillMaxWidth().height(120.dp)) {
                        val cornerRadius = 12.dp.toPx()
                        drawRoundRect(color = Color(0xFFF5F5F5), cornerRadius = androidx.compose.ui.geometry.CornerRadius(cornerRadius))
                        drawRoundRect(color = Color(0xFF00B4A3), cornerRadius = androidx.compose.ui.geometry.CornerRadius(cornerRadius), style = Stroke(width = 2.dp.toPx(), pathEffect = PathEffect.dashPathEffect(floatArrayOf(30f, 30f), 0f)))
                    }

                    if (uiState.selectedFileName != null) {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(8.dp),
                            contentAlignment = Alignment.TopEnd
                        ) {
                            IconButton(
                                onClick = { viewModel.clearFileSelection() },
                                modifier = Modifier
                                    .size(24.dp)
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(Color.Black.copy(alpha = 0.1f))
                            ) {
                                Icon(
                                    painter = painterResource(id = R.drawable.ic_close),
                                    contentDescription = "Hapus File",
                                    tint = Color.Black.copy(alpha = 0.6f),
                                    modifier = Modifier.size(16.dp)
                                )
                            }
                        }
                    }

                    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.padding(16.dp)) {
                        if (uiState.selectedFileName != null) {
                            Text(text = uiState.selectedFileName!!, fontSize = 14.sp, color = Color.Gray, fontWeight = FontWeight.Medium, textAlign = TextAlign.Center)
                        } else {
                            Text(text = "Preview akan ditampilkan disini", fontSize = 14.sp, color = Color.Gray, textAlign = TextAlign.Center)
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(text = "Maks. 1 File", fontSize = 12.sp, color = Color.Black, fontWeight = FontWeight.SemiBold) // <--- DIBUAT HITAM
                            }
                        }
                    }
                }
            }

            item {
                // Tombol Upload File
                Button(onClick = { filePickerLauncher.launch(mimeTypes) }, modifier = Modifier.fillMaxWidth().height(65.dp), colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF00B4A3)), shape = RoundedCornerShape(12.dp)) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(painter = painterResource(id = R.drawable.ic_upload), contentDescription = null, modifier = Modifier.size(20.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(text = "Unggah Berkas", fontSize = 16.sp, fontWeight = FontWeight.SemiBold)
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Box(modifier = Modifier.clip(RoundedCornerShape(8.dp)).background(Color.White.copy(alpha = 0.2f)).padding(horizontal = 8.dp, vertical = 2.dp)) {
                            Text(text = "PDF, TXT, JPG, PNG", fontSize = 10.sp, color = Color.White)
                        }
                    }
                }
            }

            item {
                // Tombol Mulai Analisa
                Button(
                    onClick = { viewModel.startAnalysis() },
                    modifier = Modifier.fillMaxWidth().height(50.dp),
                    enabled = uiState.selectedFileUri != null && !uiState.isLoading,
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFA78BFA), disabledContainerColor = Color(0xFFD9D9D9), contentColor = Color.White, disabledContentColor = Color.Gray),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    if (uiState.isLoading) {
                        CircularProgressIndicator(modifier = Modifier.size(24.dp), color = Color.White, strokeWidth = 2.dp)
                    } else {
                        Icon(painter = painterResource(id = R.drawable.ic_analysis), contentDescription = null, modifier = Modifier.size(20.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(text = "Mulai Analisa", fontSize = 16.sp, fontWeight = FontWeight.SemiBold)
                    }
                }
            }

            if (uiState.errorMessage != null) {
                item {
                    Text(text = uiState.errorMessage!!, color = MaterialTheme.colorScheme.error, modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Center)
                }
            }

            // --- BAGIAN RECENT ANALYSIS DITAMBAHKAN KEMBALI ---
            if (uiState.recentAnalyses.isNotEmpty()) {
                item {
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(text = "Analisis Terakhir", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color.Black)
                    }
                }

                // 2. Tampilkan daftar atau pesan "kosong" secara kondisional
                if (uiState.recentAnalyses.isEmpty()) {
                    item {
                        Box(
                            modifier = Modifier.fillMaxWidth().padding(vertical = 32.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "Belum ada riwayat analisis.",
                                color = Color.Gray,
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                } else {
                    items(uiState.recentAnalyses) { item ->
                        // 3. Panggil AnalysisItemCard dengan aksi klik
                        AnalysisItemCard(
                            item = item,
                            onClick = {
                                // Panggil fungsi ViewModel untuk memuat data riwayat
                                // dan memicu navigasi via LaunchedEffect
                                viewModel.viewHistoricalAnalysis(item.id)
                            }
                        )
                    }
                }
            }
        }
    }
}