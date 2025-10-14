package com.example.prediai.presentation.guide

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
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
import com.example.prediai.presentation.guide.comps.ScreeningOptionCard

@Composable
fun GuideScreen(
    onBackClick: () -> Unit = {},
    onContinueClick: () -> Unit = {},
    onChangeQuestionnaireClick: () -> Unit = {}
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF5F9FA))
    ) {
        TopBar(
            title = "Health Screening",
            subtitle = "PrediAI Analysis",
            onBackClick = onBackClick
        )

        Column(
            modifier = Modifier
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(24.dp))

            // Illustration
            Image(
                painter = painterResource(id = R.drawable.see_guide),
                contentDescription = "Health Screening Illustration",
                modifier = Modifier
                    .size(250.dp)
                    .padding(horizontal = 32.dp)
            )

            // Screening Options
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                ScreeningOptionCard(
                    icon = R.drawable.hand_ic,
                    title = "Posisi Tangan",
                    description = "Letakkan tangan dengan kuku menghadap kamera, pastikan pencahayaan cukup",
                    backgroundColor = Color(0xFFFFF4E6),
                    iconTint = Color(0xFFFFA726)
                )

                ScreeningOptionCard(
                    icon = R.drawable.tongue_ic,
                    title = "Posisi Lidah",
                    description = "Julurkan lidah dengan jelas, pastikan terlihat dalam frame yang sama",
                    backgroundColor = Color(0xFFFFE4F0),
                    iconTint = Color(0xFFEC407A)
                )

                ScreeningOptionCard(
                    icon = R.drawable.phone_ic,
                    title = "Ambil Foto",
                    description = "Tekan tombol scan untuk mengambil foto dan memulai analisis",
                    backgroundColor = Color(0xFFEDE7F6),
                    iconTint = Color(0xFF9C27B0)
                )
            }

            Spacer(modifier = Modifier.weight(1f))

            // Bottom Buttons
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Button(
                    onClick = onContinueClick,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF00BFA5)
                    ),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text(
                        text = "Lanjutkan ke Screening",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color.White
                    )
                }

                TextButton(
                    onClick = onChangeQuestionnaireClick,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                ) {
                    Text(
                        text = "Ubah Kuisionermu",
                        fontSize = 14.sp,
                        color = Color(0xFF2D3142)
                    )
                }
            }
        }
    }
}

@Preview
@Composable
private fun GuideScreenPreview()
{
    GuideScreen()
}