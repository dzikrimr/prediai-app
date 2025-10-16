package com.example.prediai.presentation.main.schedule.comps

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.prediai.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddScheduleSheet() {
    // State for the form would be hoisted to the ViewModel in a real app
    var scheduleType by remember { mutableStateOf("") }
    var time by remember { mutableStateOf("--:-- --") }
    var notes by remember { mutableStateOf("") }
    var typeExpanded by remember { mutableStateOf(false) }
    val scheduleTypes = listOf("Cek Gula Darah", "Konsultasi Dokter", "Olahraga", "Minum Obat")

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        // Jenis Jadwal
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Icon(painterResource(id = R.drawable.ic_calendar_empty), contentDescription = null, tint = Color.Gray)
                Text("Jenis Jadwal", color = Color.Gray)
            }
            ExposedDropdownMenuBox(expanded = typeExpanded, onExpandedChange = { typeExpanded = it }) {
                OutlinedTextField(
                    value = scheduleType,
                    onValueChange = {},
                    readOnly = true,
                    placeholder = { Text("Pilih jenis jadwal") },
                    trailingIcon = { Icon(Icons.Default.ArrowDropDown, contentDescription = null) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .menuAnchor(),
                    shape = RoundedCornerShape(12.dp)
                )
                ExposedDropdownMenu(expanded = typeExpanded, onDismissRequest = { typeExpanded = false }) {
                    scheduleTypes.forEach { type ->
                        DropdownMenuItem(text = { Text(type) }, onClick = {
                            scheduleType = type
                            typeExpanded = false
                        })
                    }
                }
            }
        }

        // Waktu
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Icon(painterResource(id = R.drawable.ic_time), contentDescription = null, tint = Color.Gray)
                Text("Waktu", color = Color.Gray)
            }
            OutlinedTextField(
                value = time,
                onValueChange = {},
                readOnly = true,
                trailingIcon = { Icon(Icons.Default.Schedule, contentDescription = null) },
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { /* TODO: Show Time Picker */ },
                shape = RoundedCornerShape(12.dp)
            )
        }

        // Catatan
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Icon(painterResource(id = R.drawable.ic_notes), contentDescription = null, tint = Color.Gray)
                Text("Catatan Opsional", color = Color.Gray)
            }
            OutlinedTextField(
                value = notes,
                onValueChange = { notes = it },
                placeholder = { Text("Tambahkan catatan tambahan...") },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(100.dp),
                shape = RoundedCornerShape(12.dp)
            )
        }

        // Tombol
        Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            Button(
                onClick = { /* TODO: Handle cancel */ },
                modifier = Modifier.weight(1f),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFF3F4F6))
            ) {
                Text("Batal", color = Color.Gray)
            }
            Button(
                onClick = { /* TODO: Handle save */ },
                modifier = Modifier.weight(1f),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF00B4A3))
            ) {
                Text("Simpan")
            }
        }
    }
}