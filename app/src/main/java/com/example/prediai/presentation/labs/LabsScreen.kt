package com.example.prediai.presentation.labs

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
import androidx.compose.material3.ExperimentalMaterial3Api
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.prediai.R
import com.example.prediai.domain.model.AnalysisItem
import com.example.prediai.domain.model.AnalysisType
import com.example.prediai.presentation.common.TopBar
import com.example.prediai.presentation.labs.comps.AnalysisItemCard

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LabsScreen(
    onBackClick: () -> Unit = {}
) {
    var selectedFile by remember { mutableStateOf<String?>(null) }
    var isAnalysisEnabled by remember { mutableStateOf(false) }

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
            .background(Color(0xFFF9FAFB)) // Background color applied to the main Column
    ) {
        // Top Bar
        TopBar(
            title = "Labs",
            subtitle = "Smart Lab Analysis",
            onBackClick = onBackClick // Use the passed onBackClick
        )

        // Content using LazyColumn for scrolling
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                // Removed background from here as it's in the Column
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
                        if (selectedFile != null) {
                            Text(
                                text = selectedFile ?: "",
                                fontSize = 14.sp,
                                color = Color.Gray
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
                        // Handle file upload
                        selectedFile = "document.pdf"
                        isAnalysisEnabled = true
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
                // Mulai Analisa Button
                Button(
                    onClick = { /* Handle analysis */ },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (isAnalysisEnabled) Color(0xFFA78BFA) else Color(0xFFD9D9D9),
                        contentColor = if (isAnalysisEnabled) Color.White else Color.Gray
                    ),
                    enabled = isAnalysisEnabled,
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

@Preview
@Composable
private fun LabScreenPreview() {
    LabsScreen()
}
