package com.example.prediai.presentation.main.schedule.comps

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.prediai.R
import com.example.prediai.presentation.main.schedule.ScheduleItem
import com.example.prediai.presentation.main.schedule.ScheduleStatus
import com.example.prediai.presentation.main.schedule.ScheduleType

@Composable
fun ScheduleItemCard(item: ScheduleItem) {
    val (statusText, statusBgColor, statusTextColor) = when (item.status) {
        ScheduleStatus.MENDATANG -> Triple("Mendatang", Color(0xFFE0F2F1), Color(0xFF00B4A3))
        ScheduleStatus.SELESAI -> Triple("Selesai", Color(0xFFE8F5E9), Color(0xFF4CAF50))
        ScheduleStatus.TERLEWAT -> Triple("Terlewat", Color(0xFFFFEBEE), Color(0xFFE53935))
    }
    val borderColor = when (item.status) {
        ScheduleStatus.MENDATANG -> Color(0xFF00B4A3)
        ScheduleStatus.SELESAI -> Color(0xFF4CAF50)
        ScheduleStatus.TERLEWAT -> Color(0xFFE53935)
    }
    val iconId = when (item.type) {
        ScheduleType.CEK_GULA -> R.drawable.ic_blood_drop
        ScheduleType.KONSULTASI -> R.drawable.ic_consultation
        ScheduleType.OLAHRAGA -> R.drawable.ic_exercise
        ScheduleType.MINUM_OBAT -> R.drawable.ic_pills
    }

    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .width(4.dp)
                .height(80.dp) // Match height of card content
                .background(borderColor, shape = RoundedCornerShape(2.dp))
        )
        Spacer(Modifier.width(12.dp))
        Column(modifier = Modifier.weight(1f)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(painterResource(id = iconId), contentDescription = null, tint = Color.Gray)
                Spacer(Modifier.width(8.dp))
                Text(item.type.displayName, fontWeight = FontWeight.Bold)
            }
            Text(item.description, fontSize = 14.sp, color = Color.Gray)
            Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(top = 4.dp)) {
                Icon(painterResource(id = R.drawable.ic_time), contentDescription = "Time", tint = Color.Gray, modifier = Modifier.size(16.dp))
                Spacer(Modifier.width(4.dp))
                Text(item.time, fontSize = 12.sp, color = Color.Gray)
                Spacer(Modifier.width(16.dp))
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(50))
                        .background(statusBgColor)
                        .padding(horizontal = 10.dp, vertical = 4.dp)
                ) {
                    Text(statusText, color = statusTextColor, fontSize = 12.sp, fontWeight = FontWeight.Medium)
                }
            }
        }
    }
}