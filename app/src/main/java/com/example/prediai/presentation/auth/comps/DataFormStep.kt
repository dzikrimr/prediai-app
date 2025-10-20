package com.example.prediai.presentation.auth.comps

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.*
import androidx.compose.material3.MenuDefaults
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DataFormStep(
    name: String,
    birthDate: String,
    height: String,
    weight: String,
    city: String,
    cityOptions: List<String>,
    onNameChange: (String) -> Unit,
    onBirthDateChange: (String) -> Unit,
    onHeightChange: (String) -> Unit,
    onWeightChange: (String) -> Unit,
    onCityChange: (String) -> Unit,
    onNextClick: () -> Unit,
    isLoading: Boolean
) {
    var expanded by remember { mutableStateOf(false) }
    val primaryColor = Color(0xFF00B4A3)

    // 1. State untuk menampilkan/menyembunyikan DatePickerDialog
    var showDatePicker by remember { mutableStateOf(false) }
    val datePickerState = rememberDatePickerState()

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Bantu kami Kenali Kesehatan Gula Darah Anda",
            fontSize = 15.sp,
            fontWeight = FontWeight.SemiBold,
            color = Color(0xFF2D3748)
        )

        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Text("Nama", fontSize = 14.sp, fontWeight = FontWeight.Medium, color = Color(0xFF4A5568))
            OutlinedTextField(
                value = name,
                onValueChange = onNameChange,
                placeholder = { Text("Masukkan nama Anda", color = Color(0xFFA0AEC0), fontSize = 14.sp) },
                modifier = Modifier.fillMaxWidth(),
                enabled = !isLoading,
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = primaryColor,
                    unfocusedBorderColor = Color(0xFFE2E8F0),
                    focusedTextColor = Color(0xFF2D3748),
                    unfocusedTextColor = Color(0xFF2D3748)
                )
            )
        }

        // 2. Modifikasi TextField Tanggal Lahir
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Text("Tanggal Lahir", fontSize = 14.sp, fontWeight = FontWeight.Medium, color = Color(0xFF4A5568))
            OutlinedTextField(
                value = birthDate,
                onValueChange = {}, // Dikosongkan karena tidak bisa diketik
                readOnly = true,    // Dibuat read-only
                placeholder = { Text("DD/MM/YYYY", color = Color(0xFFA0AEC0), fontSize = 14.sp) },
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { showDatePicker = true }, // Klik untuk membuka kalender
                enabled = !isLoading,
                shape = RoundedCornerShape(12.dp),
                trailingIcon = { // Tambahkan ikon kalender untuk UX
                    Icon(
                        imageVector = Icons.Default.DateRange,
                        contentDescription = "Pilih Tanggal",
                        modifier = Modifier.clickable { showDatePicker = true }
                    )
                },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = primaryColor,
                    unfocusedBorderColor = Color(0xFFE2E8F0),
                    focusedTextColor = Color(0xFF2D3748),
                    unfocusedTextColor = Color(0xFF2D3748)
                )
            )
        }

        // ... (Kode untuk Tinggi, Berat, Kota tidak berubah) ...
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Text("Tinggi Badan (cm)", fontSize = 14.sp, fontWeight = FontWeight.Medium, color = Color(0xFF4A5568))
            OutlinedTextField(
                value = height,
                onValueChange = onHeightChange,
                placeholder = { Text("Masukkan tinggi badan", color = Color(0xFFA0AEC0), fontSize = 14.sp) },
                modifier = Modifier.fillMaxWidth(),
                enabled = !isLoading,
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = primaryColor,
                    unfocusedBorderColor = Color(0xFFE2E8F0),
                    focusedTextColor = Color(0xFF2D3748),
                    unfocusedTextColor = Color(0xFF2D3748)
                )
            )
        }

        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Text("Berat Badan (kg)", fontSize = 14.sp, fontWeight = FontWeight.Medium, color = Color(0xFF4A5568))
            OutlinedTextField(
                value = weight,
                onValueChange = onWeightChange,
                placeholder = { Text("Masukkan berat badan", color = Color(0xFFA0AEC0), fontSize = 14.sp) },
                modifier = Modifier.fillMaxWidth(),
                enabled = !isLoading,
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = primaryColor,
                    unfocusedBorderColor = Color(0xFFE2E8F0),
                    focusedTextColor = Color(0xFF2D3748),
                    unfocusedTextColor = Color(0xFF2D3748)
                )
            )
        }

        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Text("Asal Kota", fontSize = 14.sp, fontWeight = FontWeight.Medium, color = Color(0xFF4A5568))
            ExposedDropdownMenuBox(
                expanded = expanded,
                onExpandedChange = { expanded = !expanded }
            ) {
                OutlinedTextField(
                    value = city,
                    onValueChange = {},
                    readOnly = true,
                    modifier = Modifier
                        .menuAnchor()
                        .fillMaxWidth(),
                    placeholder = { Text("Pilih kota", color = Color(0xFFA0AEC0), fontSize = 14.sp) },
                    trailingIcon = { Icon(Icons.Default.ArrowDropDown, contentDescription = null) },
                    enabled = !isLoading,
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = primaryColor,
                        unfocusedBorderColor = Color(0xFFE2E8F0),
                        focusedTextColor = Color(0xFF2D3748),
                        unfocusedTextColor = Color(0xFF2D3748),
                        // 🔑 Pastikan warna latar belakang inputnya putih
                        // Ini penting karena menu akan diletakkan di bawah input.
                        // Jika input tidak terlihat, mungkin ada masalah dengan warna container.
                        focusedContainerColor = Color.White,
                        unfocusedContainerColor = Color.White
                    )
                )

                // 🔑 ExposedDropdownMenu (Ini mewarisi warna surface/container secara default)
                // Kita bisa membungkusnya dengan Card untuk memastikan warna putih & elevation
                ExposedDropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false },
                    // ExposedDropdownMenu di M3 secara default menggunakan warna background dari MaterialTheme.surface
                    // atau DropdownMenuDefault.tonalElevation. Untuk memaksanya putih:
                    modifier = Modifier
                        .background(Color.White)
                        .clip(RoundedCornerShape(12.dp))
                ) {
                    cityOptions.forEach { option ->
                        DropdownMenuItem(
                            text = { Text(option) },
                            onClick = {
                                onCityChange(option)
                                expanded = false
                            },
                        )
                    }
                }
            }
        }

        Button(
            onClick = onNextClick,
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp),
            enabled = !isLoading,
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = primaryColor,
                disabledContainerColor = Color(0xFFE2E8F0)
            )
        ) {
            if (isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.size(20.dp),
                    color = Color.White,
                    strokeWidth = 2.dp
                )
            } else {
                Text("Selanjutnya", color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.SemiBold)
            }
        }
    }

    // 3. Tampilkan DatePickerDialog jika showDatePicker == true
    if (showDatePicker) {
        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                TextButton(
                    onClick = {
                        showDatePicker = false
                        val selectedDate = datePickerState.selectedDateMillis
                        if (selectedDate != null) {
                            onBirthDateChange(convertMillisToDate(selectedDate))
                        }
                    }
                ) {
                    Text("Pilih")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDatePicker = false }) {
                    Text("Batal")
                }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }
}

// Fungsi bantuan untuk mengubah timestamp (milidetik) menjadi string tanggal
private fun convertMillisToDate(millis: Long): String {
    val formatter = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
    return formatter.format(Date(millis))
}