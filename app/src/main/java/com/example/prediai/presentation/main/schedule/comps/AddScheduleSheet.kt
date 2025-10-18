package com.example.prediai.presentation.main.schedule.comps

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.NoteAlt
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.prediai.presentation.theme.PrediAITheme
import java.util.Locale
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
// --- IMPORTS BARU ---
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.IntSize
import com.example.prediai.R // Pastikan import R ini benar
import androidx.compose.ui.res.painterResource
// Ganti dengan path ke file Theme/Type.kt kamu
// import com.example.prediai.presentation.theme.PoppinsFontFamily

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddScheduleSheet(
    onDismiss: () -> Unit,
    onSaveClick: (type: String, time: String, notes: String) -> Unit
) {
    // --- State untuk Form ---
    var scheduleType by remember { mutableStateOf("") }
    var notes by remember { mutableStateOf("") }
    var typeExpanded by remember { mutableStateOf(false) }

    // --- BARU: Ganti List menjadi Map untuk menyimpan Ikon ---
    // (Pastikan R.drawable... ini ada di app-mu, sesuai ScheduleItemCard)
    val scheduleTypesWithIcons = mapOf(
        "Cek Gula Darah" to R.drawable.ic_blood_drop,
        "Konsultasi Dokter" to R.drawable.ic_consultation,
        "Olahraga" to R.drawable.ic_exercise,
        "Minum Obat" to R.drawable.ic_pills,
        "Skrining AI" to R.drawable.ic_camera,
        "Jadwal Makan" to R.drawable.ic_food_avoid,
        "Cek Tensi Darah" to R.drawable.ic_blood_pressure
    )

    // --- State untuk Time Picker ---
    var time by remember { mutableStateOf("--:--") }
    var showTimePicker by remember { mutableStateOf(false) }
    val timePickerState = rememberTimePickerState(
        initialHour = 12,
        initialMinute = 0,
        is24Hour = true
    )

    // --- Warna Kustom ---
    val mainBackgroundColor = Color(0xFFFFFFFF)
    val fieldBackgroundColor = Color(0xFFF9FAFB)
    val fieldBorderColor = Color(0xFFE5E7EB)
    val batalButtonColor = Color(0xFFF3F4F6)
    val simpanButtonColor = Color(0xFF00B4A3)
    val iconColorYellow = Color(0xFFE6C64E)
    val iconColorTeal = Color(0xFF32B5A7)
    val iconColorBlue = Color(0xFF5A8DEE)

    // --- Warna TextField (Tidak Berubah) ---
    val textFieldColors = OutlinedTextFieldDefaults.colors(
        focusedContainerColor = fieldBackgroundColor,
        unfocusedContainerColor = fieldBackgroundColor,
        disabledContainerColor = fieldBackgroundColor,
        focusedBorderColor = simpanButtonColor,
        unfocusedBorderColor = fieldBorderColor,
        disabledBorderColor = fieldBorderColor
    )

    // --- Time Picker Dialog (Tidak Berubah) ---
    if (showTimePicker) {
        BasicAlertDialog(
            onDismissRequest = { showTimePicker = false },
            modifier = Modifier.fillMaxWidth()
        ) {
            Surface(
                shape = RoundedCornerShape(28.dp),
                tonalElevation = 6.dp,
                color = MaterialTheme.colorScheme.surface,
                contentColor = MaterialTheme.colorScheme.onSurface
            ) {
                // ... (Konten Time Picker tidak berubah) ...
                Column(
                    modifier = Modifier.padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Pilih Waktu",
                        style = MaterialTheme.typography.titleLarge,
                        modifier = Modifier.padding(bottom = 20.dp)
                    )
                    TimePicker(state = timePickerState)
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 12.dp),
                        horizontalArrangement = Arrangement.End
                    ) {
                        TextButton(onClick = { showTimePicker = false }) {
                            Text("Batal")
                        }
                        TextButton(
                            onClick = {
                                showTimePicker = false
                                time = String.format(
                                    Locale.getDefault(),
                                    "%02d:%02d",
                                    timePickerState.hour,
                                    timePickerState.minute
                                )
                            }
                        ) {
                            Text("Pilih")
                        }
                    }
                }
            }
        }
    }

    val timeInteractionSource = remember { MutableInteractionSource() }
    val isTimePressed: Boolean by timeInteractionSource.collectIsPressedAsState()

    LaunchedEffect(isTimePressed) {
        if (isTimePressed) {
            showTimePicker = true
        }
    }

    // --- BARU: State untuk menyimpan ukuran TextField ---
    var textFieldSize by remember { mutableStateOf(IntSize.Zero) }
    val density = LocalDensity.current


    // --- Konten Utama Bottom Sheet ---
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(mainBackgroundColor)
            .padding(horizontal = 24.dp, vertical = 16.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        // --- Header (Tidak Berubah) ---
        Box(
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = "Tambahkan Jadwal",
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp,
                modifier = Modifier.align(Alignment.Center)
            )
            IconButton(
                onClick = onDismiss,
                modifier = Modifier.align(Alignment.CenterEnd)
            ) {
                Icon(Icons.Default.Close, contentDescription = "Tutup")
            }
        }

        // --- Jenis Jadwal ---
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            // (Label Row - Tidak Berubah) ...
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(32.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(iconColorYellow.copy(alpha = 0.1f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        Icons.Default.CalendarToday,
                        contentDescription = null,
                        tint = iconColorYellow,
                        modifier = Modifier.size(18.dp)
                    )
                }
                Text("Jenis Jadwal", fontWeight = FontWeight.Medium, fontSize = 16.sp)
            }
            // Dropdown Field
            ExposedDropdownMenuBox(expanded = typeExpanded, onExpandedChange = { typeExpanded = it }) {
                OutlinedTextField(
                    value = scheduleType,
                    onValueChange = {},
                    readOnly = true,
                    placeholder = { Text("Pilih jenis jadwal") },
                    trailingIcon = { Icon(Icons.Default.ArrowDropDown, contentDescription = null) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .menuAnchor()
                        // --- BARU: Simpan ukuran field ini ---
                        .onSizeChanged { textFieldSize = it },
                    shape = RoundedCornerShape(12.dp),
                    colors = textFieldColors
                )
                ExposedDropdownMenu(
                    expanded = typeExpanded,
                    onDismissRequest = { typeExpanded = false },
                    modifier = Modifier
                        // --- BARU: Paksa lebar menu = lebar field ---
                        .width(with(density) { textFieldSize.width.toDp() })
                        .background(mainBackgroundColor)
                        .clip(RoundedCornerShape(12.dp)) // Beri sudut melengkung
                ) {
                    // --- BARU: Kustomisasi tampilan item ---
                    scheduleTypesWithIcons.forEach { (type, iconRes) ->
                        DropdownMenuItem(
                            text = {
                                Text(
                                    text = type,
                                    // --- GANTI 'PoppinsFontFamily' dengan Font Family-mu ---
                                    // fontFamily = PoppinsFontFamily
                                )
                            },
                            onClick = {
                                scheduleType = type
                                typeExpanded = false
                            },
                            // --- BARU: Tambahkan ikon di depan ---
                            leadingIcon = {
                                Icon(
                                    painter = painterResource(id = iconRes),
                                    contentDescription = type,
                                    tint = Color.Gray, // Ganti warnanya jika perlu
                                    modifier = Modifier.size(24.dp)
                                )
                            },
                            // --- BARU: Beri padding agar tidak mepet ---
                            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
                        )
                    }
                }
            }
        }

        // --- Waktu (Tidak Berubah) ---
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            // (Label Row) ...
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(32.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(iconColorTeal.copy(alpha = 0.1f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        Icons.Default.Schedule,
                        contentDescription = null,
                        tint = iconColorTeal,
                        modifier = Modifier.size(18.dp)
                    )
                }
                Text("Waktu", fontWeight = FontWeight.Medium, fontSize = 16.sp)
            }
            OutlinedTextField(
                value = time,
                onValueChange = {},
                readOnly = true,
                trailingIcon = { Icon(Icons.Default.Schedule, contentDescription = "Pilih Waktu") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = textFieldColors,
                interactionSource = timeInteractionSource
            )
        }

        // --- Catatan Opsional (Tidak Berubah) ---
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            // (Label Row) ...
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(32.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(iconColorBlue.copy(alpha = 0.1f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        Icons.Default.NoteAlt,
                        contentDescription = null,
                        tint = iconColorBlue,
                        modifier = Modifier.size(18.dp)
                    )
                }
                Text("Catatan Opsional", fontWeight = FontWeight.Medium, fontSize = 16.sp)
            }
            OutlinedTextField(
                value = notes,
                onValueChange = { notes = it },
                placeholder = { Text("Tambahkan catatan tambahan...") },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(100.dp),
                shape = RoundedCornerShape(12.dp),
                colors = textFieldColors
            )
        }

        val isSaveEnabled = scheduleType.isNotBlank() && time != "--:--"

        // --- Tombol (Tidak Berubah) ---
        Row(
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.padding(top = 8.dp)
        ) {
            Button(
                onClick = onDismiss,
                modifier = Modifier
                    .weight(1f)
                    .height(50.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = batalButtonColor,
                    contentColor = Color.Gray
                )
            ) {
                Text("Batal")
            }
            Button(
                onClick = { onSaveClick(scheduleType, time, notes) },
                enabled = isSaveEnabled,
                modifier = Modifier
                    .weight(1f)
                    .height(50.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = simpanButtonColor,
                    contentColor = Color.White,
                    disabledContainerColor = batalButtonColor,
                    disabledContentColor = Color.Gray
                )
            ) {
                Text("Simpan")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))
    }
}


@Preview(showBackground = true)
@Composable
fun AddScheduleSheetPreview() {
    PrediAITheme {
        AddScheduleSheet(
            onDismiss = {},
            onSaveClick = { _, _, _ -> }
        )
    }
}