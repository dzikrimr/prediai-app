package com.example.prediai.presentation.auth.comps

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

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

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Title
        Text(
            text = "Bantu kami Kenali Kesehatan Gula Darah Anda",
            fontSize = 15.sp,
            fontWeight = FontWeight.SemiBold,
            color = Color(0xFF2D3748)
        )

        // Nama
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

        // Tanggal Lahir
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Text("Tanggal Lahir", fontSize = 14.sp, fontWeight = FontWeight.Medium, color = Color(0xFF4A5568))
            OutlinedTextField(
                value = birthDate,
                onValueChange = onBirthDateChange,
                placeholder = { Text("DD/MM/YYYY", color = Color(0xFFA0AEC0), fontSize = 14.sp) },
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

        // Tinggi Badan
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

        // Berat Badan
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

        // Asal Kota
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
                        unfocusedTextColor = Color(0xFF2D3748)
                    )
                )
                ExposedDropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    cityOptions.forEach { option ->
                        DropdownMenuItem(
                            text = { Text(option) },
                            onClick = {
                                onCityChange(option)
                                expanded = false
                            }
                        )
                    }
                }
            }
        }

        // Tombol Next
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
}
