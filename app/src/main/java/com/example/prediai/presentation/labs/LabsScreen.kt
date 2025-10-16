package com.example.prediai.presentation.labs

import android.content.Context
import android.net.Uri
import android.provider.OpenableColumns
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.prediai.R
import com.example.prediai.domain.model.AnalysisItem
import com.example.prediai.domain.model.AnalysisType
import com.example.prediai.presentation.common.TopBar
import com.example.prediai.presentation.labs.comps.AnalysisItemCard
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

// Helper function untuk mendapatkan nama file dari Uri
fun getFileName(uri: Uri, context: Context): String {
    var result: String? = null
    if (uri.scheme == "content") {
        val cursor = context.contentResolver.query(uri, null, null, null, null)
        try {
            if (cursor != null && cursor.moveToFirst()) {
                val columnIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
                if (columnIndex != -1) {
                    result = cursor.getString(columnIndex)
                }
            }
        } finally {
            cursor?.close()
        }
    }
    if (result == null) {
        result = uri.path
        val cut = result?.lastIndexOf('/')
        if (cut != -1) {
            result = result?.substring(cut!! + 1)
        }
    }
    return result ?: "Unknown file"
}

@Composable
fun LabsScreen(
    onBackClick: () -> Unit = {},
    onNavigateToResult: (fileName: String, uploadDate: String) -> Unit = { _, _ -> }
) {
    var selectedFileUri by remember { mutableStateOf<Uri?>(null) }
    var selectedFileName by remember { mutableStateOf<String?>(null) }
    val context = LocalContext.current

    // Definisikan tipe file yang diizinkan
    val mimeTypes = arrayOf(
        "application/pdf",
        "application/msword",
        "application/vnd.openxmlformats-officedocument.wordprocessingml.document", // .docx
        "text/plain",
        "image/jpeg",
        "image/png"
    )

    // Launcher untuk membuka file picker
    val filePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.OpenDocument(),
        onResult = { uri ->
            if (uri != null) {
                selectedFileUri = uri
                selectedFileName = getFileName(uri, context)
            }
        }
    )

    // Sample data untuk recent analysis
    val recentAnalysisList = remember {
        listOf(
            AnalysisItem("CamScanner-dokter-01.jpg", "April 15, 2025", AnalysisType.IMAGE, false),
            AnalysisItem("CamScanner-dokter-01.jpg", "April 12, 2025", AnalysisType.IMAGE, true),
            AnalysisItem("CamScanner-dokter-01.jpg", "April 10, 2025", AnalysisType.PDF, true)
        )
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
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                Spacer(modifier = Modifier.height(8.dp))

                // Upload Card with Gradient
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(150.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .background(
                            brush = Brush.verticalGradient(
                                colors = listOf(
                                    Color(0xCC00B4A3), // 80% opacity
                                    Color(0x33A78BFA)  // 20% opacity
                                )
                            )
                        )
                ) {
                    // Background image
                    Image(
                        painter = painterResource(id = R.drawable.bg_lab),
                        contentDescription = null,
                        modifier = Modifier
                            .align(Alignment.CenterEnd)
                            .size(180.dp)
                            .offset(x = 40.dp),
                        alpha = 0.8f
                    )

                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(24.dp)
                    ) {
                        Text(
                            text = "Upload Your Lab Results",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = "Smart Lab Analysis",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Color.White
                        )

                        Spacer(modifier = Modifier.height(4.dp))

                        Text(
                            text = "Upload your lab results and get AI powered insights to better understand your health data",
                            fontSize = 12.sp,
                            color = Color.White.copy(alpha = 0.9f),
                            lineHeight = 18.sp
                        )
                    }
                }
            }

            item {
                // Upload Preview Box
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(120.dp),
                    contentAlignment = Alignment.Center
                ) {
                    // Dashed border using Canvas
                    Canvas(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(120.dp)
                    ) {
                        val cornerRadius = 12.dp.toPx()
                        drawRoundRect(
                            color = Color(0xFFF5F5F5),
                            cornerRadius = androidx.compose.ui.geometry.CornerRadius(cornerRadius)
                        )
                        drawRoundRect(
                            color = Color(0xFF00B4A3),
                            cornerRadius = androidx.compose.ui.geometry.CornerRadius(cornerRadius),
                            style = Stroke(
                                width = 2.dp.toPx(),
                                pathEffect = PathEffect.dashPathEffect(floatArrayOf(30f, 30f), 0f)
                            )
                        )
                    }

                    // Content
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.padding(16.dp)
                    ) {
                        if (selectedFileName != null) {
                            Text(
                                text = selectedFileName!!,
                                fontSize = 14.sp,
                                color = Color.Gray,
                                fontWeight = FontWeight.Medium
                            )
                        } else {
                            Text(
                                text = "Preview akan ditampilkan disini",
                                fontSize = 14.sp,
                                color = Color.Gray,
                                textAlign = TextAlign.Center
                            )
                            Text(
                                text = "Maks. 1 File",
                                fontSize = 12.sp,
                                color = Color.Gray
                            )
                        }
                    }
                }
            }

            item {
                // Upload File Button
                Button(
                    onClick = {
                        // Jalankan file picker saat tombol diklik
                        filePickerLauncher.launch(mimeTypes)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(65.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF00B4A3)
                    ),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_upload),
                                contentDescription = null,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Upload File",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                        }

                        Spacer(modifier = Modifier.height(4.dp))

                        // File types with rounded background
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .background(Color.White.copy(alpha = 0.2f))
                                .padding(horizontal = 8.dp, vertical = 2.dp)
                        ) {
                            Text(
                                text = "PDF, DOC, TXT, JPG, PNG",
                                fontSize = 10.sp,
                                color = Color.White
                            )
                        }
                    }
                }
            }

            item {
                Button(
                    onClick = {
                        selectedFileName?.let { fileName ->
                            val currentDate = LocalDate.now().format(
                                DateTimeFormatter.ofPattern("MMM dd, yyyy", Locale.getDefault())
                            )
                            onNavigateToResult(fileName, currentDate)
                        }
                    },
                    modifier = Modifier.fillMaxWidth().height(50.dp),
                    enabled = selectedFileUri != null,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFFA78BFA),
                        disabledContainerColor = Color(0xFFD9D9D9),
                        contentColor = Color.White,
                        disabledContentColor = Color.Gray
                    ),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_analysis),
                        contentDescription = null,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Mulai Analisa",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }

            item {
                Spacer(modifier = Modifier.height(8.dp))

                // Recent Analysis Header
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Recent Analysis",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )

                    TextButton(onClick = { /* View all */ }) {
                        Text(
                            text = "View All",
                            fontSize = 14.sp,
                            color = Color(0xFF00B4A3)
                        )
                    }
                }
            }

            items(recentAnalysisList) { item ->
                AnalysisItemCard(item)
            }

            item {
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}