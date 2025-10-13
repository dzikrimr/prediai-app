package com.example.prediai.presentation.scan.tabs

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.prediai.R
import com.example.prediai.presentation.scan.comps.DetailItemWithCircle

@Composable
fun DetailsScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF5F5F5))
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        Spacer(modifier = Modifier.height(8.dp))

        // Risk Factors Identified
        Surface(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            color = Color.White,
            shadowElevation = 2.dp
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(bottom = 12.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Warning,
                        contentDescription = "Warning",
                        tint = Color(0xFFFFC107),
                        modifier = Modifier
                            .size(20.dp)
                            .padding(end = 8.dp)
                    )
                    Text(
                        text = "Risk Factors Identified",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color(0xFF212121)
                    )
                }

                DetailItemWithCircle(
                    text = "Nail discoloration patterns",
                    circleColor = Color(0xFFFACC15)
                )
                DetailItemWithCircle(
                    text = "Texture irregularities",
                    circleColor = Color(0xFFFACC15)
                )
                DetailItemWithCircle(
                    text = "Surface changes detected",
                    circleColor = Color(0xFFFACC15)
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Lifestyle Suggestions
        Surface(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            color = Color.White,
            shadowElevation = 2.dp
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(bottom = 12.dp)
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_heart),
                        contentDescription = "Heart",
                        tint = Color(0xFF00B4A3),
                        modifier = Modifier
                            .size(20.dp)
                            .padding(end = 8.dp)
                    )
                    Text(
                        text = "Lifestyle Suggestions",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color(0xFF212121)
                    )
                }

                DetailItemWithCircle(
                    text = "Maintain balanced nutrition with vitamins",
                    circleColor = Color(0xFF00B4A3)
                )
                DetailItemWithCircle(
                    text = "Regular exercise and stress management",
                    circleColor = Color(0xFF00B4A3)
                )
                DetailItemWithCircle(
                    text = "Avoid harmful habits and chemicals",
                    circleColor = Color(0xFF00B4A3)
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Next Steps
        Surface(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            color = Color.White,
            shadowElevation = 2.dp
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(bottom = 12.dp)
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_steps),
                        contentDescription = "Steps",
                        tint = Color(0xFF7E57C2),
                        modifier = Modifier
                            .size(20.dp)
                            .padding(end = 8.dp)
                    )
                    Text(
                        text = "Next Steps",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color(0xFF212121)
                    )
                }

                Text(
                    text = "Monitor changes over the next 2-3 weeks. Schedule a routine check-up with your healthcare provider to discuss these findings and get professional guidance.",
                    fontSize = 14.sp,
                    color = Color(0xFF424242),
                    lineHeight = 20.sp
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Kesimpulan Card
        Surface(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            color = Color(0xFFE3F2FD),
            shadowElevation = 1.dp
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_conclusion),
                        contentDescription = "Conclusion",
                        tint = Color(0xFF1976D2),
                        modifier = Modifier
                            .size(24.dp)
                            .padding(end = 8.dp)
                    )
                    Text(
                        text = "Kesimpulan",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color(0xFF1976D2)
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Berdasarkan analisa citra kuku dan lidah, tidak ditemukan indikasi kuat diabetes. Namun, tetap disarankan untuk rutin untuk pencegahan.",
                    fontSize = 14.sp,
                    color = Color(0xFF424242),
                    lineHeight = 20.sp
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))
    }
}