package com.example.prediai.presentation.onboarding.comps

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun OnboardingButtons(
    onBackClick: () -> Unit,
    onNextClick: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Back button
        Box(
            modifier = Modifier
                .size(48.dp)
                .clip(CircleShape)
                .background(Color(0xFFE5E7EB))
                .clickable { onBackClick() },
            contentAlignment = Alignment.Center
        ) {
            Text("<", fontSize = 20.sp, color = Color.Black)
        }

        // Next button
        Button(
            onClick = onNextClick,
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF00B4A3)),
            modifier = Modifier.height(48.dp)
        ) {
            Text("Lanjut >", color = Color.White)
        }
    }
}
