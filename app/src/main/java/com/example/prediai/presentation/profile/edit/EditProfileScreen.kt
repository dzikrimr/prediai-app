package com.example.prediai.presentation.profile.edit

import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.prediai.R
import com.example.prediai.presentation.common.TopBar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditProfileScreen(navController: NavController) {
    var fullName by remember { mutableStateOf("Sarah Wijaya") }
    var birthDate by remember { mutableStateOf("1990-05-15") }
    var height by remember { mutableStateOf("165") }
    var weight by remember { mutableStateOf("60") }
    var city by remember { mutableStateOf("") }
    var showDatePicker by remember { mutableStateOf(false) }
    var showCityDropdown by remember { mutableStateOf(false) }

    val tealColor = Color(0xFF00BFA5)
    val purpleIcon = Color(0xFFA78BFA)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF9FAFB))
    ) {
        // Top Bar
        TopBar(
            title = "Edit Profil",
            subtitle = null,
            onBackClick = { navController.popBackStack() }
        )

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item { Spacer(modifier = Modifier.height(6.dp)) }

            // Profile Photo Section
            item {
                // --- MODIFICATION START ---
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 24.dp), // Added padding for spacing inside the card
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Box(
                            modifier = Modifier.size(70.dp) // Changed from 120.dp
                        ) {
                            // Profile Image
                            Box(
                                modifier = Modifier
                                    .size(60.dp) // Changed from 120.dp
                                    .clip(CircleShape)
                                    .background(Color(0xFFE0E0E0)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Person,
                                    contentDescription = "Profile",
                                    tint = Color.White,
                                    modifier = Modifier.size(50.dp) // Changed from 60.dp
                                )
                            }

                            // Camera Button
                            Box(
                                modifier = Modifier
                                    .size(32.dp) // Changed from 36.dp
                                    .align(Alignment.BottomEnd)
                                    .offset(x = (-4).dp, y = (-4).dp) // Adjusted offset slightly
                                    .clip(CircleShape)
                                    .background(tealColor)
                                    .clickable(
                                        interactionSource = remember { MutableInteractionSource() },
                                        indication = LocalIndication.current,
                                        onClick = { /* Handle photo change */ }
                                    ),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.CameraAlt,
                                    contentDescription = "Change Photo",
                                    tint = Color.White,
                                    modifier = Modifier.size(18.dp) // Changed from 20.dp
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp)) // Increased spacer a bit

                        Text(
                            text = "Ketuk untuk mengubah foto",
                            fontSize = 14.sp,
                            color = Color(0xFF757575)
                        )
                    }
                }
                // --- MODIFICATION END ---
            }


            // Nama Lengkap
            item {
                InputField(
                    label = "Nama Lengkap",
                    value = fullName,
                    onValueChange = { fullName = it },
                    placeholder = "Sarah Wijaya",
                    icon = R.drawable.ic_profile,
                    iconTint = purpleIcon
                )
            }

            // Tanggal Lahir
            item {
                InputField(
                    label = "Tanggal Lahir",
                    value = birthDate,
                    onValueChange = { birthDate = it },
                    placeholder = "1990-05-15",
                    icon = R.drawable.ic_calendar,
                    iconTint = Color(0xFFFF9800),
                    trailingIcon = {
                        Icon(
                            imageVector = Icons.Default.CalendarToday,
                            contentDescription = "Calendar",
                            tint = Color(0xFF9E9E9E),
                            modifier = Modifier.size(20.dp)
                        )
                    },
                    readOnly = true,
                    onClick = { showDatePicker = true }
                )
            }

            // Tinggi Badan
            item {
                InputField(
                    label = "Tinggi Badan",
                    value = height,
                    onValueChange = { height = it },
                    placeholder = "165",
                    icon = R.drawable.ic_ruler,
                    iconTint = Color(0xFF2196F3),
                    suffix = "cm",
                    keyboardType = KeyboardType.Number
                )
            }

            // Berat Badan
            item {
                InputField(
                    label = "Berat Badan",
                    value = weight,
                    onValueChange = { weight = it },
                    placeholder = "60",
                    icon = R.drawable.ic_weight,
                    iconTint = Color(0xFF4CAF50),
                    suffix = "kg",
                    keyboardType = KeyboardType.Number
                )
            }

            // Asal Kota
            item {
                InputField(
                    label = "Asal Kota",
                    value = city,
                    onValueChange = { city = it },
                    placeholder = "Pilih kota",
                    icon = R.drawable.ic_location,
                    iconTint = purpleIcon,
                    trailingIcon = {
                        Icon(
                            imageVector = Icons.Default.KeyboardArrowDown,
                            contentDescription = "Dropdown",
                            tint = Color(0xFF9E9E9E)
                        )
                    },
                    readOnly = true,
                    onClick = { showCityDropdown = true }
                )
            }

            // Buttons
            item {
                Spacer(modifier = Modifier.height(16.dp))

                // Simpan Button
                Button(
                    onClick = { /* Handle save */ },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = tealColor
                    ),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = "Save",
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Simpan",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Batal Button
                OutlinedButton(
                    onClick = { /* Handle cancel */ },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = Color(0xFF757575)
                    ),
                    border = androidx.compose.foundation.BorderStroke(
                        width = 1.dp,
                        color = Color(0xFFE0E0E0)
                    ),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Cancel",
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Batal",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }

            item { Spacer(modifier = Modifier.height(24.dp)) }
        }
    }
}

@Composable
fun InputField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    icon: Int,
    iconTint: Color,
    suffix: String? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
    keyboardType: KeyboardType = KeyboardType.Text,
    readOnly: Boolean = false,
    onClick: (() -> Unit)? = null
) {
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        // Label with Icon
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(bottom = 8.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(24.dp)
                    .clip(CircleShape)
                    .background(iconTint.copy(alpha = 0.15f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(id = icon),
                    contentDescription = label,
                    tint = iconTint,
                    modifier = Modifier.size(14.dp)
                )
            }
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = label,
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                color = Color(0xFF212121)
            )
        }

        // Input Field
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            placeholder = {
                Text(
                    text = placeholder,
                    color = Color(0xFFBDBDBD),
                    fontSize = 14.sp
                )
            },
            modifier = Modifier
                .fillMaxWidth()
                .clickable(
                    enabled = onClick != null,
                    interactionSource = remember { MutableInteractionSource() },
                    indication = LocalIndication.current
                ) { onClick?.invoke() },
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = Color.White,
                unfocusedContainerColor = Color.White,
                disabledContainerColor = Color.White,
                focusedBorderColor = Color(0xFFE0E0E0),
                unfocusedBorderColor = Color(0xFFE0E0E0),
            ),
            shape = RoundedCornerShape(12.dp),
            singleLine = true,
            readOnly = readOnly,
            enabled = onClick == null,
            keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
            trailingIcon = {
                if (suffix != null) {
                    Text(
                        text = suffix,
                        color = Color(0xFF9E9E9E),
                        fontSize = 14.sp,
                        modifier = Modifier.padding(end = 8.dp)
                    )
                } else {
                    trailingIcon?.invoke()
                }
            }
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun EditProfileScreenPreview() {
    EditProfileScreen(navController = NavController(LocalContext.current))
}