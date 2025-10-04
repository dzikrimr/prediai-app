@file:OptIn(ExperimentalMaterial3Api::class)

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material.icons.outlined.BarChart
import androidx.compose.material.icons.outlined.List
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController

data class ScanResult(
    val date: String,
    val time: String,
    val riskLevel: String,
    val percentage: Int,
    val color: Color,
    val icon: @Composable () -> Unit
)

@Composable
fun ProgressTrackingScreen(navController: NavController) {
    var selectedTab by remember { mutableStateOf(0) }
    var selectedChart by remember { mutableStateOf(0) } // 0: Line Chart, 1: Bar Chart

    val scanResults = listOf(
        ScanResult(
            "15 Jan 2024",
            "09:30",
            "Risiko Rendah",
            25,
            Color(0xFF4CAF50)
        ) {
            Icon(Icons.Filled.Check, null, tint = Color.White)
        },
        ScanResult(
            "14 Jan 2024",
            "14:20",
            "Risiko Sedang",
            45,
            Color(0xFFFF9800)
        ) {
            Icon(Icons.Filled.Warning, null, tint = Color.White)
        },
        ScanResult(
            "13 Jan 2024",
            "11:15",
            "Risiko Rendah",
            20,
            Color(0xFF4CAF50)
        ) {
            Icon(Icons.Filled.Check, null, tint = Color.White)
        }
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF5F5F5))
    ) {
        // Top Bar
        TopAppBar(
            title = { Text("Progress Tracking") },
            navigationIcon = {
                IconButton(onClick = { navController.popBackStack() }) { // Fungsi back
                    Icon(Icons.Filled.ArrowBack, contentDescription = null)
                }
            },
            actions = {
                IconButton(onClick = {}) {
                    Icon(Icons.Filled.MoreVert, contentDescription = null)
                }
            }
        )

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Tab Bar
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    TabButton(
                        text = "1 Bulan",
                        isSelected = selectedTab == 0,
                        onClick = { selectedTab = 0 }
                    )
                    TabButton(
                        text = "3 Bulan",
                        isSelected = selectedTab == 1,
                        onClick = { selectedTab = 1 }
                    )
                    TabButton(
                        text = "Semua Data",
                        isSelected = selectedTab == 2,
                        onClick = { selectedTab = 2 }
                    )
                }
            }

            // Stats Cards
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    StatsCard(
                        modifier = Modifier.weight(1f),
                        title = "Total",
                        value = "24",
                        subtitle = "Total Scan",
                        backgroundColor = Color(0xFF00BCD4),
                        icon = Icons.Outlined.BarChart
                    )
                    StatsCard(
                        modifier = Modifier.weight(1f),
                        title = "Avg",
                        value = "32%",
                        subtitle = "Rata-rata\nRisiko",
                        backgroundColor = Color(0xFFFF9800),
                        icon = Icons.Filled.Warning
                    )
                    StatsCard(
                        modifier = Modifier.weight(1f),
                        title = "Days",
                        value = "7",
                        subtitle = "Streak\nTerlama",
                        backgroundColor = Color(0xFF4CAF50),
                        icon = Icons.Filled.DateRange
                    )
                }
            }

            // Risk Development Section with Chart Toggle
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Perkembangan Risiko",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.Black
                            )
                            Row {
                                IconButton(
                                    onClick = { selectedChart = 0 }
                                ) {
                                    Icon(
                                        Icons.Outlined.BarChart,
                                        contentDescription = null,
                                        tint = if (selectedChart == 0) Color(0xFF00BCD4) else Color.Gray
                                    )
                                }
                                IconButton(
                                    onClick = { selectedChart = 1 }
                                ) {
                                    Icon(
                                        Icons.Outlined.List,
                                        contentDescription = null,
                                        tint = if (selectedChart == 1) Color(0xFF00BCD4) else Color.Gray
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        // Dynamic Chart
                        if (selectedChart == 0) {
                            RiskLineChart(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(200.dp),
                                data = scanResults.map { it.percentage }
                            )
                        } else {
                            RiskBarChart(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(200.dp),
                                data = scanResults
                            )
                        }
                    }
                }
            }

            // Risk Level Legend
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(
                            text = "Level Risiko",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.Black
                        )

                        RiskLevelItem("Rendah (0-30%)", Color(0xFF4CAF50))
                        RiskLevelItem("Sedang (31-70%)", Color(0xFFFF9800))
                        RiskLevelItem("Tinggi (71-100%)", Color(0xFFF44336))
                    }
                }
            }

            // Recent Scans
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Text(
                            text = "Scan Terbaru",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.Black,
                            modifier = Modifier.padding(bottom = 16.dp)
                        )

                        scanResults.forEachIndexed { index, result ->
                            ScanResultItem(result = result)

                            // Add divider except for last item
                            if (index < scanResults.size - 1) {
                                HorizontalDivider(
                                    modifier = Modifier.padding(vertical = 12.dp),
                                    thickness = 1.dp,
                                    color = Color.Gray.copy(alpha = 0.2f)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun TabButton(
    text: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(
            containerColor = if (isSelected) Color(0xFF00BCD4) else Color.White,
            contentColor = if (isSelected) Color.White else Color.Gray
        ),
        shape = RoundedCornerShape(20.dp),
        elevation = ButtonDefaults.buttonElevation(
            defaultElevation = if (isSelected) 4.dp else 1.dp
        )
    ) {
        Text(text, fontSize = 14.sp)
    }
}

@Composable
fun StatsCard(
    modifier: Modifier = Modifier,
    title: String,
    value: String,
    subtitle: String,
    backgroundColor: Color,
    icon: androidx.compose.ui.graphics.vector.ImageVector
) {
    Card(
        modifier = modifier.height(100.dp), // Fixed height untuk semua card
        colors = CardDefaults.cardColors(containerColor = backgroundColor),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    icon,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(16.dp)
                )
                Text(
                    text = title,
                    color = Color.White,
                    fontSize = 12.sp
                )
            }

            Text(
                text = value,
                color = Color.White,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            )

            Text(
                text = subtitle,
                color = Color.White.copy(alpha = 0.9f),
                fontSize = 10.sp,
                lineHeight = 12.sp
            )
        }
    }
}

@Composable
fun RiskLineChart(
    modifier: Modifier = Modifier,
    data: List<Int>
) {
    Canvas(modifier = modifier) {
        if (data.isEmpty()) return@Canvas

        val width = size.width
        val height = size.height
        val maxValue = data.maxOrNull() ?: 100
        val minValue = data.minOrNull() ?: 0
        val range = maxValue - minValue

        val path = Path()
        val points = data.mapIndexed { index, value ->
            val x = (index.toFloat() / (data.size - 1)) * width
            val y = height - ((value - minValue).toFloat() / range) * height
            Offset(x, y)
        }

        // Draw grid lines
        for (i in 0..4) {
            val y = height * i / 4
            drawLine(
                color = Color.Gray.copy(alpha = 0.2f),
                start = Offset(0f, y),
                end = Offset(width, y),
                strokeWidth = 1.dp.toPx()
            )
        }

        // Draw line
        for (i in 0 until points.size - 1) {
            drawLine(
                color = Color(0xFF00BCD4),
                start = points[i],
                end = points[i + 1],
                strokeWidth = 3.dp.toPx()
            )
        }

        // Draw points
        points.forEach { point ->
            drawCircle(
                color = Color(0xFF00BCD4),
                radius = 6.dp.toPx(),
                center = point
            )
            drawCircle(
                color = Color.White,
                radius = 3.dp.toPx(),
                center = point
            )
        }
    }
}

@Composable
fun RiskBarChart(
    modifier: Modifier = Modifier,
    data: List<ScanResult>
) {
    Canvas(modifier = modifier) {
        if (data.isEmpty()) return@Canvas

        val width = size.width
        val height = size.height
        val barWidth = width / data.size * 0.6f
        val spacing = width / data.size * 0.4f

        data.forEachIndexed { index, item ->
            val barHeight = (item.percentage / 100f) * height
            val x = index * (barWidth + spacing) + spacing / 2
            val y = height - barHeight

            drawRect(
                color = item.color,
                topLeft = Offset(x, y),
                size = androidx.compose.ui.geometry.Size(barWidth, barHeight)
            )
        }
    }
}

@Composable
fun RiskLevelItem(
    text: String,
    color: Color
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Box(
            modifier = Modifier
                .size(12.dp)
                .background(color, CircleShape)
        )
        Text(
            text = text,
            fontSize = 14.sp,
            color = Color.Black
        )
    }
}

@Composable
fun ScanResultItem(
    result: ScanResult
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .background(result.color, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                result.icon()
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = result.date,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color.Black
                )
                Text(
                    text = result.riskLevel,
                    fontSize = 12.sp,
                    color = Color.Gray
                )
                Text(
                    text = result.time,
                    fontSize = 11.sp,
                    color = Color.Gray
                )
            }

            Text(
                text = "${result.percentage}%",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = result.color,
                textAlign = TextAlign.End
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ProgressTrackingPreview() {
    MaterialTheme {
        // Simulasi NavController untuk preview
        val navController = rememberNavController()
        ProgressTrackingScreen(navController = navController)
    }
}