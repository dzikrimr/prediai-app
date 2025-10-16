package com.example.prediai.presentation.profile.comps

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun LogoutConfirmationDialog(
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    // Warna yang digunakan
    val confirmColor = Color(0xFF2DD4BF)
    val dismissColor = Color(0xFFF87171)

    Card(
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 40.dp, vertical = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Teks Pertanyaan
            Text(
                text = "Yakin ingin keluar dari akun?",
                fontSize = 18.sp,
                fontWeight = FontWeight.Normal,
                color = Color(0xFF6B7280)
            )

            Spacer(modifier = Modifier.height(20.dp))

            Row(
                horizontalArrangement = Arrangement.spacedBy(16.dp) // Jarak antar tombol
            ) {
                // Tombol Batal (Dismiss)
                Button(
                    onClick = onDismiss,
                    modifier = Modifier
                        .width(100.dp) // Lebar persegi panjang
                        .height(48.dp), // Tinggi persegi panjang
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = dismissColor
                    ),
                    contentPadding = PaddingValues(0.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Batal",
                        tint = Color.White,
                        modifier = Modifier.size(28.dp)
                    )
                }

                // Tombol Konfirmasi
                Button(
                    onClick = onConfirm,
                    modifier = Modifier
                        .width(100.dp) // Lebar persegi panjang
                        .height(48.dp), // Tinggi persegi panjang
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = confirmColor
                    ),
                    contentPadding = PaddingValues(0.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = "Konfirmasi Keluar",
                        tint = Color.White,
                        modifier = Modifier.size(28.dp)
                    )
                }
            }
        }
    }
}

@Preview
@Composable
private fun LogoutConfirmationDialogPreview() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .wrapContentSize(Alignment.Center)
    ) {
        LogoutConfirmationDialog(
            onConfirm = {},
            onDismiss = {}
        )
    }
}