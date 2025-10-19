package com.example.prediai.presentation.main.schedule.comps

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kizitonwose.calendar.compose.HorizontalCalendar
import com.kizitonwose.calendar.compose.rememberCalendarState
import com.kizitonwose.calendar.core.CalendarDay
import com.kizitonwose.calendar.core.CalendarMonth
import com.kizitonwose.calendar.core.DayPosition
import com.kizitonwose.calendar.core.daysOfWeek
import kotlinx.coroutines.launch
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.TextStyle // Import ini sudah benar
import java.util.Locale

@Composable
fun CalendarView(
    modifier: Modifier = Modifier,
    selectedDate: LocalDate,
    datesWithEvents: Set<LocalDate>,
    onDateClick: (LocalDate) -> Unit,
    onMonthChanged: (YearMonth) -> Unit
) {
    val currentMonth = remember { YearMonth.now() }
    val startMonth = remember { currentMonth.minusMonths(12) }
    val endMonth = remember { currentMonth.plusMonths(12) }
    val daysOfWeek = remember { daysOfWeek() }
    val coroutineScope = rememberCoroutineScope()

    val state = rememberCalendarState(
        startMonth = startMonth,
        endMonth = endMonth,
        firstVisibleMonth = currentMonth,
        firstDayOfWeek = daysOfWeek.first()
    )

    LaunchedEffect(state.firstVisibleMonth) {
        onMonthChanged(state.firstVisibleMonth.yearMonth)
    }

    Column(modifier = modifier.padding(horizontal = 16.dp)) {
        HorizontalCalendar(
            state = state,
            monthHeader = { month ->
                CalendarMonthHeader(
                    month = month,
                    daysOfWeek = daysOfWeek, // Teruskan daysOfWeek
                    onPrevClick = {
                        coroutineScope.launch {
                            state.animateScrollToMonth(month.yearMonth.minusMonths(1))
                        }
                    },
                    onNextClick = {
                        coroutineScope.launch {
                            state.animateScrollToMonth(month.yearMonth.plusMonths(1))
                        }
                    }
                )
            },
            dayContent = { day ->
                DateCell(
                    day = day,
                    isSelected = selectedDate == day.date,
                    hasEvent = day.date in datesWithEvents,
                    onClick = { onDateClick(day.date) }
                )
            }
        )
    }
}

@Composable
private fun CalendarMonthHeader(
    month: CalendarMonth,
    daysOfWeek: List<DayOfWeek>, // Terima daysOfWeek
    onPrevClick: () -> Unit,
    onNextClick: () -> Unit
) {
    val monthTitle = remember(month) {
        val formatter = java.time.format.DateTimeFormatter.ofPattern("MMMM yyyy", Locale.getDefault())
        month.yearMonth.format(formatter)
    }

    Column {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            IconButton(onClick = onPrevClick) {
                Icon(Icons.AutoMirrored.Filled.KeyboardArrowLeft, contentDescription = "Bulan Sebelumnya")
            }
            Text(monthTitle, fontWeight = FontWeight.Bold, fontSize = 18.sp)
            IconButton(onClick = onNextClick) {
                Icon(Icons.AutoMirrored.Filled.KeyboardArrowRight, contentDescription = "Bulan Berikutnya")
            }
        }
        Spacer(Modifier.height(16.dp))

        Row(modifier = Modifier.fillMaxWidth()) {
            daysOfWeek.forEach { dayOfWeek ->
                Text(
                    // --- PERBAIKAN DI SINI ---
                    // SHORT_NARROW akan memberi "S", "S", "R", dll.
                    text = dayOfWeek.getDisplayName(TextStyle.SHORT, Locale.getDefault()),
                    modifier = Modifier.weight(1f),
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.Gray
                )
            }
        }
        Spacer(Modifier.height(8.dp))
    }
}

@Composable
private fun DateCell(
    day: CalendarDay,
    isSelected: Boolean,
    hasEvent: Boolean,
    onClick: (LocalDate) -> Unit
) {
    // Perbaikan crash 'rememberRipple' sudah ada di sini
    if (day.position == DayPosition.MonthDate) {
        Box(
            modifier = Modifier
                .aspectRatio(1f)
                .clip(CircleShape)
                .clickable(
                    onClick = { onClick(day.date) }
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
                        text = day.date.dayOfMonth.toString(),
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
                } else {
                    Spacer(modifier = Modifier.height(6.dp))
                }
            }
        }
    } else {
        Spacer(modifier = Modifier.aspectRatio(1f))
    }
}