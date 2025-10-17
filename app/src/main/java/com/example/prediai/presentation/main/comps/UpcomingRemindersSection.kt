package com.example.prediai.presentation.main.comps

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.CalendarToday
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.prediai.R
import com.example.prediai.presentation.main.Reminder
import com.example.prediai.presentation.theme.PrediAITheme

private val primaryButtonColor = Color(0xFF157BBC)
private val cardBorderColor = Color(0xFF93C5FD)

@Composable
fun UpcomingRemindersSection(
    reminders: List<Reminder>,
    onAddScheduleClick: () -> Unit,
    onSeeAllClick: () -> Unit // DIUBAH: Tambahkan parameter ini
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Pengingat Mendatang",
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    color = primaryButtonColor
                )
            }
            Spacer(modifier = Modifier.height(16.dp))

            if (reminders.isEmpty()) {
                EmptyReminders(onAddScheduleClick = onAddScheduleClick)
            } else {
                Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        contentPadding = PaddingValues(horizontal = 4.dp)
                    ) {
                        items(reminders) { reminder ->
                            ReminderItem(reminder = reminder)
                        }
                    }
                    Button(
                        onClick = onAddScheduleClick,
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = primaryButtonColor),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Tambahkan Jadwal", fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}

@Composable
private fun ReminderItem(reminder: Reminder) {
    Column(
        modifier = Modifier
            .width(140.dp)
            .border(
                width = 1.dp,
                color = cardBorderColor,
                shape = RoundedCornerShape(16.dp)
            )
            .padding(16.dp)
    ) {
        Icon(
            imageVector = Icons.Outlined.CalendarToday,
            contentDescription = null,
            tint = primaryButtonColor,
            modifier = Modifier.size(24.dp)
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = reminder.time,
            fontWeight = FontWeight.Medium,
            fontSize = 16.sp,
            color = primaryButtonColor
        )
        Text(
            text = reminder.title,
            fontSize = 10.sp,
            color = primaryButtonColor
        )
    }
}

@Composable
private fun EmptyReminders(onAddScheduleClick: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Icon(
            painter = painterResource(id = R.drawable.ic_calendar_empty),
            contentDescription = null,
            modifier = Modifier.size(64.dp),
            tint = Color.LightGray
        )
        Text(
            text = "Anda belum memiliki jadwal",
            color = primaryButtonColor,
            textAlign = TextAlign.Center
        )
        Button(
            onClick = onAddScheduleClick,
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(containerColor = primaryButtonColor),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Tambahkan Jadwal", fontWeight = FontWeight.Bold)
        }
    }
}

@Preview(showBackground = true, name = "Reminders Filled")
@Composable
fun UpcomingRemindersSectionFilledPreview() {
    PrediAITheme {
        UpcomingRemindersSection(
            reminders = listOf(
                Reminder("Take a Medicine", "Today at 2:00 PM"),
                Reminder("Take a Exercise", "Today at 2:00 PM"),
            ),
            onAddScheduleClick = {},
            onSeeAllClick = {} // Tambahkan untuk preview
        )
    }
}

@Preview(showBackground = true, name = "Reminders Empty")
@Composable
fun UpcomingRemindersSectionEmptyPreview() {
    PrediAITheme {
        UpcomingRemindersSection(
            reminders = emptyList(),
            onAddScheduleClick = {},
            onSeeAllClick = {} // Tambahkan untuk preview
        )
    }
}