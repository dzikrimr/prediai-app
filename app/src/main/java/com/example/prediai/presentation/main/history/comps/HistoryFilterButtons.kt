package com.example.prediai.presentation.main.history.comps

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.prediai.presentation.main.history.HistoryStatus
import com.example.prediai.presentation.theme.PrediAITheme

@Composable
fun HistoryFilterButtons(
    selectedFilter: HistoryStatus,
    onFilterClick: (HistoryStatus) -> Unit
) {
    // DIUBAH: Modifier diubah untuk memungkinkan scroll horizontal
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .horizontalScroll(rememberScrollState()), // Tambahkan ini
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        FilterChip(
            text = "Semua",
            isSelected = selectedFilter == HistoryStatus.SEMUA,
            onClick = { onFilterClick(HistoryStatus.SEMUA) }
        )
        FilterChip(
            text = "Normal",
            isSelected = selectedFilter == HistoryStatus.NORMAL,
            onClick = { onFilterClick(HistoryStatus.NORMAL) }
        )
        FilterChip(
            text = "Peringatan",
            isSelected = selectedFilter == HistoryStatus.PERINGATAN,
            onClick = { onFilterClick(HistoryStatus.PERINGATAN) }
        )
        FilterChip(
            text = "Tinggi",
            isSelected = selectedFilter == HistoryStatus.TINGGI,
            onClick = { onFilterClick(HistoryStatus.TINGGI) }
        )
    }
}

@Composable
private fun FilterChip(text: String, isSelected: Boolean, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        shape = RoundedCornerShape(50),
        colors = ButtonDefaults.buttonColors(
            containerColor = if (isSelected) Color(0xFF00B4A3) else Color(0xFFF3F4F6),
            contentColor = if (isSelected) Color.White else Color.Gray
        ),
        contentPadding = PaddingValues(horizontal = 20.dp, vertical = 10.dp)
    ) {
        Text(text, fontSize = 14.sp, fontWeight = FontWeight.Medium)
    }
}

@Preview
@Composable
fun HistoryFilterButtonsPreview() {
    PrediAITheme {
        HistoryFilterButtons(selectedFilter = HistoryStatus.SEMUA, onFilterClick = {})
    }
}