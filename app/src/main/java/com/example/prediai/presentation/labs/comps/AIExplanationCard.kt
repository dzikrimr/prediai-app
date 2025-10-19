package com.example.prediai.presentation.labs.comps

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.prediai.R
import com.example.prediai.domain.model.Finding

@Composable
fun AIExplanationCard(
    summary: String,
    findings: List<Finding>,
    nextSteps: String,
    onConsultClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
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
                    text = "Analisis AI & Saran Tindak Lanjut",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF2D3142)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Ringkasan AI",
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = summary,
                fontSize = 14.sp,
                lineHeight = 22.sp,
                color = Color(0xFF5D5D5D)
            )
            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Temuan Kunci",
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp
            )
            Spacer(modifier = Modifier.height(8.dp))
            Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                findings.forEach { finding ->
                    KeyFindingItem(finding = finding)
                }
            }
            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Langkah Selanjutnya",
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = nextSteps,
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
                    containerColor = Color(0xFF00BFA5)
                ),
                shape = RoundedCornerShape(10.dp)
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

@Composable
fun KeyFindingItem(finding: Finding) {
    val statusColor = when (finding.status.lowercase()) {
        "tinggi" -> Color(0xFFEF4444)
        "rendah" -> Color(0xFF3B82F6)
        else -> Color.DarkGray
    }

    Column {
        Text(
            text = finding.parameter,
            fontWeight = FontWeight.SemiBold,
            color = Color.Black
        )
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Nilai Anda: ${finding.value}",
                fontSize = 13.sp,
                color = Color.DarkGray
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = finding.status,
                fontSize = 12.sp,
                color = Color.White,
                modifier = Modifier
                    .background(statusColor, RoundedCornerShape(4.dp))
                    .padding(horizontal = 6.dp, vertical = 2.dp)
            )
        }
        Text(
            text = "Rentang Normal: ${finding.normal_range}",
            fontSize = 13.sp,
            color = Color.Gray
        )
        if (finding.explanation.isNotBlank()) {
            Text(
                text = finding.explanation,
                fontSize = 13.sp,
                color = Color.Gray,
                lineHeight = 18.sp
            )
        }
    }
}