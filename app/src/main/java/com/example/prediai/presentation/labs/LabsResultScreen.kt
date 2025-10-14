package com.example.prediai.presentation.labs

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.prediai.presentation.common.TopBar
import com.example.prediai.presentation.labs.comps.AIExplanationCard
import com.example.prediai.presentation.labs.comps.FileInfoCard

@Composable
fun LabResultScreen(
    fileName: String = "CamScanner-dokter-01.jpg",
    uploadDate: String = "Apr 15, 2025",
    onBackClick: () -> Unit = {},
    onConsultClick: () -> Unit = {},
    onAskAIClick: () -> Unit = {}
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF5F5F5))
    ) {
        TopBar(
            title = "Lab Analysis",
            onBackClick = onBackClick
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            FileInfoCard(
                fileName = fileName,
                uploadDate = uploadDate
            )

            Spacer(modifier = Modifier.height(16.dp))

            AIExplanationCard(
                onConsultClick = onConsultClick
            )

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = onAskAIClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .height(48.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFA78BFA) // Ganti backgroundColor -> containerColor
                ),
                shape = RoundedCornerShape(8.dp),
                elevation = ButtonDefaults.buttonElevation( // Ganti elevation() -> buttonElevation()
                    defaultElevation = 0.dp
                )
            ) {
                Text(
                    text = "Tanya ke AI",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.White
                )
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Preview
@Composable
private fun LabsResultScreenPreview() {
    LabResultScreen()
}