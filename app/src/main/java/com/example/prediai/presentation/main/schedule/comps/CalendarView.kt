package com.example.prediai.presentation.main.schedule.comps

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.time.LocalDate

@Composable
fun CalendarView(
    selectedDate: LocalDate,
    datesWithEvents: Set<Int>,
    currentMonth: String,
    onDateClick: (LocalDate) -> Unit,
    onPrevMonthClick: () -> Unit,
    onNextMonthClick: () -> Unit
) {
    val daysOfWeek = listOf("S", "M", "T", "W", "T", "F", "S")
    val dates = (1..31).toList() // Simplified list of dates

    Column(modifier = Modifier.padding(horizontal = 16.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            IconButton(onClick = onPrevMonthClick) {
                Icon(Icons.AutoMirrored.Filled.KeyboardArrowLeft, contentDescription = "Previous Month")
            }
            Text(currentMonth, fontWeight = FontWeight.Bold, fontSize = 18.sp)
            IconButton(onClick = onNextMonthClick) {
                Icon(Icons.AutoMirrored.Filled.KeyboardArrowRight, contentDescription = "Next Month")
            }
        }
        Spacer(Modifier.height(16.dp))

        Row(modifier = Modifier.fillMaxWidth()) {
            daysOfWeek.forEach { day ->
                Text(
                    text = day,
                    modifier = Modifier.weight(1f),
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.Gray
                )
            }
        }
        Spacer(Modifier.height(8.dp))

        (0..4).forEach { week ->
            Row(modifier = Modifier.fillMaxWidth()) {
                (1..7).forEach { dayOfMonthInWeek ->
                    val day = (week * 7) + dayOfMonthInWeek - 3
                    if (day in dates) {
                        DateCell(
                            day = day,
                            isSelected = selectedDate.dayOfMonth == day,
                            hasEvent = day in datesWithEvents,
                            onClick = { onDateClick(selectedDate.withDayOfMonth(day)) }
                        )
                    } else {
                        Spacer(modifier = Modifier.weight(1f))
                    }
                }
            }
        }
    }
}

@Composable
fun RowScope.DateCell(
    day: Int,
    isSelected: Boolean,
    hasEvent: Boolean,
    onClick: () -> Unit
) {
    // BARU: Buat interactionSource
    val interactionSource = remember { MutableInteractionSource() }

    Box(
        // DIUBAH: Gunakan overload clickable yang lebih lengkap
        modifier = Modifier
            .weight(1f)
            .aspectRatio(1f)
            .clickable(
                onClick = onClick
            ),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .clip(CircleShape)
                    .background(if (isSelected) Color(0xFF00B4A3) else Color.Transparent),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = day.toString(),
                    color = if (isSelected) Color.White else Color.Black
                )
            }
            if (hasEvent && !isSelected) {
                Box(
                    modifier = Modifier
                        .padding(top = 2.dp)
                        .size(4.dp)
                        .clip(CircleShape)
                        .background(Color.Gray)
                )
            }
        }
    }
}