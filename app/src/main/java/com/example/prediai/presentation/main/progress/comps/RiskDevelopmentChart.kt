package com.example.prediai.presentation.main.progress.comps

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.prediai.presentation.main.progress.ChartFilter
import kotlin.math.ceil
import kotlin.math.floor

@Composable
fun RiskDevelopmentChart(
    selectedFilter: ChartFilter,
    onFilterChanged: (ChartFilter) -> Unit,
    data: List<Pair<String, Float>>
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "Perkembangan Risiko",
                fontWeight = FontWeight.SemiBold,
                fontSize = 18.sp
            )
            Spacer(modifier = Modifier.height(16.dp))

            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                FilterButton("1 Minggu", selectedFilter == ChartFilter.ONE_WEEK) { onFilterChanged(ChartFilter.ONE_WEEK) }
                FilterButton("1 Bulan", selectedFilter == ChartFilter.ONE_MONTH) { onFilterChanged(ChartFilter.ONE_MONTH) }
                FilterButton("6 Bulan", selectedFilter == ChartFilter.SIX_MONTHS) { onFilterChanged(ChartFilter.SIX_MONTHS) }
                FilterButton("Semua", selectedFilter == ChartFilter.ALL_TIME) { onFilterChanged(ChartFilter.ALL_TIME) }
            }
            Spacer(modifier = Modifier.height(24.dp))
            LineChart(data = data)
        }
    }
}

@Composable
private fun FilterButton(text: String, isSelected: Boolean, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        shape = RoundedCornerShape(50),
        colors = ButtonDefaults.buttonColors(
            containerColor = if (isSelected) Color(0xFF00B4A3) else Color(0xFFF3F4F6),
            contentColor = if (isSelected) Color.White else Color.Gray
        ),
        contentPadding = PaddingValues(horizontal = 14.dp)
    ) {
        Text(text = text, fontSize = 12.sp, fontWeight = FontWeight.Medium)
    }
}

@Composable
private fun LineChart(data: List<Pair<String, Float>>) {
    val chartColor = Color(0xFF00B4A3)

    if (data.isEmpty()) {
        Box(modifier = Modifier
            .fillMaxWidth()
            .height(150.dp), contentAlignment = androidx.compose.ui.Alignment.Center) {
            Text("Tidak ada data untuk ditampilkan", color = Color.Gray)
        }
        return
    }

    val minValue = floor((data.minOfOrNull { it.second } ?: 0f) / 10f) * 10f
    val maxValue = ceil((data.maxOfOrNull { it.second } ?: 100f) / 10f) * 10f
    val valueRange = (maxValue - minValue).coerceAtLeast(1f)
    val numHorizontalLines = 5

    Canvas(
        modifier = Modifier
            .fillMaxWidth()
            .height(150.dp)
    ) {
        val horizontalPadding = 16.dp.toPx()
        val yAxisLabelOffset = 30.dp.toPx()
        val chartHeight = size.height - 30.dp.toPx()
        val chartWidth = size.width - yAxisLabelOffset - horizontalPadding

        val textPaint = Paint().asFrameworkPaint().apply {
            isAntiAlias = true
            textSize = 10.sp.toPx()
            color = android.graphics.Color.GRAY
            textAlign = android.graphics.Paint.Align.RIGHT
        }

        // Gambar garis dan label Y-axis secara dinamis
        for (i in 0 until numHorizontalLines) {
            val y = chartHeight * (1 - (i.toFloat() / (numHorizontalLines - 1)))
            drawLine(
                color = Color.LightGray.copy(alpha = 0.3f),
                start = Offset(yAxisLabelOffset, y),
                end = Offset(size.width - horizontalPadding, y)
            )
            val label = (minValue + (valueRange * (i.toFloat() / (numHorizontalLines - 1)))).toInt()
            drawIntoCanvas {
                it.nativeCanvas.drawText(
                    "$label%",
                    yAxisLabelOffset - 8.dp.toPx(),
                    y + 4.dp.toPx(),
                    textPaint
                )
            }
        }

        // Gambar titik dan garis data
        val points = data.mapIndexed { index, pair ->
            val x = yAxisLabelOffset + (index.toFloat() / (data.size - 1).coerceAtLeast(1)) * chartWidth
            val y = chartHeight * (1 - ((pair.second - minValue) / valueRange))
            Offset(x, y)
        }

        val path = Path().apply {
            points.forEachIndexed { index, offset ->
                if (index == 0) moveTo(offset.x, offset.y) else lineTo(offset.x, offset.y)
            }
        }
        drawPath(path, color = chartColor, style = Stroke(width = 2.dp.toPx()))

        points.forEachIndexed { index, offset ->
            drawCircle(color = chartColor, radius = 4.dp.toPx(), center = offset)
            drawIntoCanvas {
                it.nativeCanvas.drawText(
                    data[index].first,
                    offset.x,
                    size.height,
                    textPaint.apply { textAlign = android.graphics.Paint.Align.CENTER }
                )
            }
        }
    }
}