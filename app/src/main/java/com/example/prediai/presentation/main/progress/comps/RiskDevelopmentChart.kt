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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.prediai.presentation.main.progress.ChartFilter
import com.example.prediai.presentation.theme.PrediAITheme

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
                FilterButton("1 Bulan", selectedFilter == ChartFilter.ONE_MONTH) { onFilterChanged(ChartFilter.ONE_MONTH) }
                FilterButton("3 Bulan", selectedFilter == ChartFilter.THREE_MONTHS) { onFilterChanged(ChartFilter.THREE_MONTHS) }
                FilterButton("Semua Data", selectedFilter == ChartFilter.ALL_TIME) { onFilterChanged(ChartFilter.ALL_TIME) }
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
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
    ) {
        Text(
            text = text,
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium
        )
    }
}

@Composable
private fun LineChart(data: List<Pair<String, Float>>) {
    val maxValue = 10f
    val minValue = 2f
    val chartColor = Color(0xFF6C63FF)

    Canvas(
        modifier = Modifier
            .fillMaxWidth()
            .height(150.dp)
    ) {
        val horizontalPadding = 16.dp.toPx()
        val yAxisLabelOffset = 24.dp.toPx()

        val chartHeight = size.height - 30.dp.toPx()
        val chartWidth = size.width - yAxisLabelOffset - horizontalPadding

        val textPaint = Paint().asFrameworkPaint().apply {
            isAntiAlias = true
            textSize = 10.sp.toPx()
            color = android.graphics.Color.GRAY
            textAlign = android.graphics.Paint.Align.RIGHT
        }

        for (i in 0..4) {
            val y = chartHeight * (1 - (i.toFloat() / 4f))
            drawLine(
                color = Color.LightGray.copy(alpha = 0.3f),
                start = Offset(yAxisLabelOffset, y),
                end = Offset(size.width - horizontalPadding, y)
            )
            val label = (minValue + (maxValue - minValue) * (i.toFloat() / 4f)).toInt()
            drawIntoCanvas {
                it.nativeCanvas.drawText(
                    label.toString(),
                    // DIUBAH: Menggeser teks label Y ke kanan agar tidak mepet
                    yAxisLabelOffset - 8.dp.toPx(),
                    y + 4.dp.toPx(),
                    textPaint
                )
            }
        }

        if (data.isEmpty()) return@Canvas

        val points = data.mapIndexed { index, pair ->
            val x = yAxisLabelOffset + (index.toFloat() / (data.size - 1).coerceAtLeast(1)) * chartWidth
            val y = chartHeight * (1 - ((pair.second - minValue) / (maxValue - minValue)))
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


@Preview(showBackground = true)
@Composable
fun RiskDevelopmentChartPreview() {
    PrediAITheme {
        RiskDevelopmentChart(
            selectedFilter = ChartFilter.ONE_MONTH,
            onFilterChanged = {},
            data = listOf(
                "Apr '24" to 3.5f, "May '24" to 4.2f, "Jun '24" to 5.1f,
                "Jul '24" to 6.3f, "Aug '24" to 7.0f, "Sep '24" to 7.8f, "Oct '24" to 8.5f
            )
        )
    }
}