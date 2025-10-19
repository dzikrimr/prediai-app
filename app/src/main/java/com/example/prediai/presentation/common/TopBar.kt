package com.example.prediai.presentation.common

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
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

@Composable
fun TopBar(
    title: String,
    subtitle: String? = null,
    onBackClick: (() -> Unit)? = null,
    backgroundColor: Color = Color.White,
    contentColor: Color = Color(0xFF2D3142)
) {
    // 1. Latar belakang sekarang diatur oleh Box luar.
    // Box ini tidak memiliki padding vertikal sama sekali.
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(backgroundColor)
    ) {
        // 2. Konten (Row) ditempatkan di dalam Box.
        Row(
            // 3. Row inilah yang diberi padding untuk status bar.
            // Ini akan mendorong tombol dan teks ke bawah,
            // tetapi background di Box luar akan tetap terlihat di belakang status bar.
            modifier = Modifier
                .fillMaxWidth()
                .windowInsetsPadding(WindowInsets.statusBars)
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (onBackClick != null) {
                IconButton(onClick = onBackClick) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_back),
                        contentDescription = "Back",
                        tint = contentColor
                    )
                }
            } else {
                Spacer(modifier = Modifier.width(48.dp))
            }

            Column(
                modifier = Modifier.weight(1f),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = title,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = contentColor
                )
                if (subtitle != null) {
                    Text(
                        text = subtitle,
                        fontSize = 12.sp,
                        color = contentColor.copy(alpha = 0.6f)
                    )
                }
            }

            Spacer(modifier = Modifier.width(48.dp))
        }
    }
}


@Preview
@Composable
private fun TopBarPreview() {
    TopBar(
        title = "Home",
        subtitle = "Current Location",
        onBackClick = {}
    )
}