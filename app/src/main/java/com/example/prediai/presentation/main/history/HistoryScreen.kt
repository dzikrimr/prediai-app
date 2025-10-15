package com.example.prediai.presentation.main.history

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.prediai.presentation.theme.BackgroundColor
import com.example.prediai.presentation.theme.PrimaryColor
import com.example.prediai.presentation.theme.SecondaryColor
import com.example.prediai.presentation.theme.DangerColor
import com.example.prediai.presentation.theme.WarningColor

// Data class untuk hasil scan
data class ScanResult(
    val id: Int,
    val status: String,
    val date: String,
    val time: String,
    val description: String,
    val scanType: String,
    val duration: String,
    val percentage: String,
    val statusColor: Color,
    val icon: ImageVector
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HistoryScreen(navController: NavController) {
    var selectedFilter by remember { mutableStateOf("Semua") }

    val scanResults = listOf(
        ScanResult(
            id = 1,
            status = "Risiko Tinggi",
            date = "Hari ini",
            time = "11:52",
            description = "Hasil menunjukkan indikasi kuat diabetes. Segera konsultasi dengan dokter.",
            scanType = "Scan kuku & lidah",
            duration = "Baru saja",
            percentage = "85%",
            statusColor = DangerColor,
            icon = Icons.Filled.Warning
        ),
        ScanResult(
            id = 2,
            status = "Peringatan",
            date = "Kemarin",
            time = "09:15",
            description = "Perlu perhatian. Disarankan untuk mengatur pola makan dan olahraga.",
            scanType = "Scan kuku & lidah",
            duration = "1 hari yang lalu",
            percentage = "65%",
            statusColor = WarningColor,
            icon = Icons.Filled.Warning
        ),
        ScanResult(
            id = 3,
            status = "Normal",
            date = "2 hari lalu",
            time = "16:45",
            description = "Kondisi normal. Tetap jaga pola hidup sehat.",
            scanType = "Scan kuku & lidah",
            duration = "2 hari yang lalu",
            percentage = "15%",
            statusColor = SecondaryColor,
            icon = Icons.Filled.CheckCircle
        ),
        ScanResult(
            id = 4,
            status = "Normal",
            date = "3 hari lalu",
            time = "11:20",
            description = "Hasil bagus! Tidak ada indikasi diabetes.",
            scanType = "Scan kuku & lidah",
            duration = "3 hari yang lalu",
            percentage = "20%",
            statusColor = SecondaryColor,
            icon = Icons.Filled.CheckCircle
        )
    )

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundColor),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            TopBarWithFilters(selectedFilter = selectedFilter) { filter ->
                selectedFilter = filter
            }
        }

        item {
            MonthlySummarySection()
        }

        item {
            HistoryHeaderSection()
        }

        items(scanResults.filter { result ->
            when (selectedFilter) {
                "Normal" -> result.status == "Normal"
                "Peringatan" -> result.status == "Peringatan"
                "Tinggi" -> result.status == "Risiko Tinggi"
                else -> true
            }
        }) { scanResult ->
            ScanResultCard(scanResult = scanResult, navController = navController)
        }

        item {
            LoadMoreButton()
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBarWithFilters(selectedFilter: String, onFilterSelected: (String) -> Unit) {
    val filters = listOf("Semua", "Normal", "Peringatan", "Tinggi")

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = PrimaryColor),
        shape = RoundedCornerShape(bottomStart = 16.dp, bottomEnd = 16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            // TopBar Section
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Riwayat Scan",
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp
                )

                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    IconButton(onClick = { /* Handle filter */ }) {
                        Icon(
                            imageVector = Icons.Filled.FilterList,
                            contentDescription = "Filter",
                            tint = Color.White
                        )
                    }
                    IconButton(onClick = { /* Handle search */ }) {
                        Icon(
                            imageVector = Icons.Filled.Search,
                            contentDescription = "Search",
                            tint = Color.White
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Filter Section
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                contentPadding = PaddingValues(vertical = 8.dp)
            ) {
                items(filters) { filter ->
                    FilterChip(
                        selected = selectedFilter == filter,
                        onClick = { onFilterSelected(filter) },
                        label = {
                            Text(
                                text = filter,
                                fontSize = 14.sp,
                                color = Color.White,
                                fontWeight = if (selectedFilter == filter) FontWeight.Medium else FontWeight.Normal
                            )
                        },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = Color.White.copy(alpha = 0.3f),
                            containerColor = Color.White.copy(alpha = 0.2f),
                            selectedLabelColor = Color.White,
                            labelColor = Color.White
                        ),
                        border = FilterChipDefaults.filterChipBorder(
                            borderColor = Color.Transparent,
                            selectedBorderColor = Color.Transparent,
                            enabled = true,
                            selected = selectedFilter == filter
                        ),
                        modifier = Modifier
                            .height(46.dp)
                            .clip(RoundedCornerShape(100.dp))
                    )
                }
            }
        }
    }
}

@Composable
fun MonthlySummarySection() {
    Column(modifier = Modifier.padding(horizontal = 16.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Ringkasan Bulan Ini",
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                color = Color.Black
            )
            Text(
                text = "Agustus 2025", // Diperbarui ke bulan saat ini
                color = Color.Gray,
                fontSize = 14.sp
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            SummaryItem(
                icon = Icons.Filled.CheckCircle,
                count = "12",
                label = "Normal",
                color = SecondaryColor
            )
            SummaryItem(
                icon = Icons.Filled.Warning,
                count = "3",
                label = "Peringatan",
                color = WarningColor
            )
            SummaryItem(
                icon = Icons.Filled.Error,
                count = "1",
                label = "Tinggi",
                color = DangerColor
            )
        }
    }
}

@Composable
fun SummaryItem(icon: ImageVector, count: String, label: String, color: Color) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(48.dp)
                .background(color.copy(alpha = 0.1f), CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = label,
                tint = color,
                modifier = Modifier.size(24.dp)
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = count,
            fontWeight = FontWeight.Bold,
            fontSize = 24.sp,
            color = Color.Black
        )
        Text(
            text = label,
            fontSize = 12.sp,
            color = Color.Gray
        )
    }
}

@Composable
fun HistoryHeaderSection() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "Riwayat Scan",
            fontWeight = FontWeight.Bold,
            fontSize = 18.sp,
            color = Color.Black
        )
        Text(
            text = "16 total scan",
            color = Color.Gray,
            fontSize = 14.sp
        )
    }
}

@Composable
fun ScanResultCard(scanResult: ScanResult, navController: NavController) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .clickable {
                navController.navigate("historyDetail/${scanResult.id}")
            },
        colors = CardDefaults.cardColors(containerColor = Color.White),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .background(scanResult.statusColor.copy(alpha = 0.1f), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = scanResult.icon,
                    contentDescription = scanResult.status,
                    tint = scanResult.statusColor,
                    modifier = Modifier.size(24.dp)
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Top
                ) {
                    Text(
                        text = scanResult.status,
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                        color = Color.Black
                    )
                    Box(
                        modifier = Modifier
                            .background(scanResult.statusColor, RoundedCornerShape(12.dp))
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = scanResult.percentage,
                            color = Color.White,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }

                Text(
                    text = "${scanResult.date}, ${scanResult.time}",
                    color = Color.Gray,
                    fontSize = 12.sp
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = scanResult.scanType,
                    color = Color.Gray,
                    fontSize = 12.sp
                )

                Text(
                    text = scanResult.duration,
                    color = Color.Gray,
                    fontSize = 12.sp
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = scanResult.description,
                    color = Color.Gray,
                    fontSize = 13.sp,
                    lineHeight = 18.sp
                )

                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Lihat Detail",
                        color = PrimaryColor,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Icon(
                        imageVector = Icons.Filled.ArrowForward,
                        contentDescription = "Detail",
                        tint = PrimaryColor,
                        modifier = Modifier.size(16.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun LoadMoreButton() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        TextButton(
            onClick = { /* Load more results */ }
        ) {
            Text(
                text = "Muat Lebih Banyak",
                color = PrimaryColor,
                fontWeight = FontWeight.Medium,
                fontSize = 16.sp
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun HistoryScreenPreview() {
    MaterialTheme {
        HistoryScreen(navController = rememberNavController())
    }
}