package com.example.prediai.presentation.profile.edit

import android.app.DatePickerDialog
import android.content.Context
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.background
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.prediai.R
import com.example.prediai.presentation.common.TopBar
import kotlinx.coroutines.launch
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter

// Definisikan warna aksen kustom (00B4A3)
val CustomAccentColor = Color(0xFF00B4A3)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditProfileScreen(navController: NavController, viewModel: EditProfileViewModel = hiltViewModel()) {
    val uiState by viewModel.uiState.collectAsState()
    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current

    // Launcher for picking image from gallery
    val pickImageLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            coroutineScope.launch {
                viewModel.uploadProfileImage(uri, context)
            }
        }
    }

    // Date Picker Dialog
    val datePickerState = rememberDatePickerState(
        initialSelectedDateMillis = if (uiState.profile.birthDate.isNotBlank()) {
            try {
                val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
                val localDate = java.time.LocalDate.parse(uiState.profile.birthDate, formatter)
                localDate.atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli()
            } catch (e: Exception) {
                Instant.now().toEpochMilli()
            }
        } else {
            Instant.now().toEpochMilli()
        }
    )

    if (uiState.showDatePicker) {
        // --- BUNGKUS DENGAN THEME KUSTOM UNTUK MENGUBAH WARNA AKSEN DATEPICKER ---
        MaterialTheme(
            colorScheme = MaterialTheme.colorScheme.copy(
                primary = CustomAccentColor,      // Warna latar belakang header dan pemilih
                onPrimary = Color.White,          // Warna teks/icon pada primary color (mis. pada header)
                primaryContainer = CustomAccentColor.copy(alpha = 0.15f), // Warna latar belakang DateInput
                onPrimaryContainer = Color.Black // Warna teks pada DateInput
            )
        ) {
            androidx.compose.material3.DatePickerDialog(
                onDismissRequest = { viewModel.toggleDatePicker(false) },
                confirmButton = {
                    TextButton(
                        onClick = {
                            val selectedDateMillis = datePickerState.selectedDateMillis
                            if (selectedDateMillis != null) {
                                val selectedDate = Instant.ofEpochMilli(selectedDateMillis)
                                    .atZone(ZoneId.systemDefault())
                                    .toLocalDate()
                                    .format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))

                                viewModel.updateBirthDate(selectedDate)
                            }
                            viewModel.toggleDatePicker(false)
                        }
                    ) {
                        Text("Pilih") // Menggunakan warna Primary dari kustom Theme
                    }
                },
                dismissButton = {
                    TextButton(onClick = { viewModel.toggleDatePicker(false) }) {
                        Text("Batal") // Menggunakan warna Primary dari kustom Theme
                    }
                }
            ) {
                DatePicker(state = datePickerState)
            }
        }
        // -----------------------------------------------------------------------
    }

    // City Dropdown States
    var cityExpanded by remember { mutableStateOf(false) }
    val cities = listOf("Jakarta", "Surabaya", "Bandung", "Medan", "Semarang", "Makassar", "Palembang", "Yogyakarta")
    var selectedCityDisplay by remember { mutableStateOf(uiState.profile.city.ifBlank { "" }) }

    // Sync local state with ViewModel for city
    LaunchedEffect(uiState.profile.city) {
        selectedCityDisplay = uiState.profile.city.ifBlank { "" }
    }

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
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Box(
                            modifier = Modifier.size(70.dp),
                            contentAlignment = Alignment.Center // Tambahkan ini agar loading/icon berada di tengah
                        ) {
                            if (uiState.isLoading) {
                                // 1. Loading State (jika sedang mengunggah atau menyimpan)
                                CircularProgressIndicator(
                                    modifier = Modifier.size(60.dp),
                                    color = CustomAccentColor,
                                    strokeWidth = 3.dp
                                )
                            } else {
                                // 2. Tampilkan Gambar atau Placeholder
                                val imageModifier = Modifier
                                    .size(60.dp)
                                    .clip(CircleShape)

                                if (!uiState.profile.profileImageUrl.isNullOrEmpty()) {
                                    AsyncImage(
                                        model = uiState.profile.profileImageUrl,
                                        contentDescription = "Profile",
                                        modifier = imageModifier,
                                        contentScale = ContentScale.Crop
                                    )
                                } else {
                                    // Placeholder default
                                    Box(
                                        modifier = imageModifier
                                            .background(Color(0xFFE0E0E0)),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Person,
                                            contentDescription = "Profile Placeholder",
                                            tint = Color.White,
                                            modifier = Modifier.size(50.dp)
                                        )
                                    }
                                }

                                // Camera Button (Tetap di sini, di atas gambar/placeholder/loading)
                                Box(
                                    modifier = Modifier
                                        .size(32.dp)
                                        .align(Alignment.BottomEnd)
                                        .offset(x = (-4).dp, y = (-4).dp)
                                        .clip(CircleShape)
                                        .background(CustomAccentColor)
                                        .clickable(
                                            interactionSource = remember { MutableInteractionSource() },
                                            indication = LocalIndication.current,
                                            onClick = { pickImageLauncher.launch("image/*") }
                                        ),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.CameraAlt,
                                        contentDescription = "Change Photo",
                                        tint = Color.White,
                                        modifier = Modifier.size(18.dp)
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        Text(
                            text = "Ketuk untuk mengubah foto",
                            fontSize = 14.sp,
                            color = Color(0xFF757575)
                        )
                    }
                }
            }

            // Nama Lengkap
            item {
                InputField(
                    label = "Nama Lengkap",
                    value = uiState.profile.name,
                    onValueChange = { viewModel.updateName(it) },
                    placeholder = "Sarah Wijaya",
                    icon = R.drawable.ic_profile,
                    iconTint = Color(0xFFA78BFA)
                )
            }

            // Tanggal Lahir (Menggunakan Box Clickable)
            item {
                Column(modifier = Modifier.fillMaxWidth()) {
                    // --- 1. Label dan Icon Kustom ---
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(bottom = 8.dp)
                    ) {
                        val iconTint = Color(0xFFFF9800)
                        Box(
                            modifier = Modifier
                                .size(24.dp)
                                .clip(CircleShape)
                                .background(iconTint.copy(alpha = 0.15f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_calendar),
                                contentDescription = "Tanggal Lahir",
                                tint = iconTint,
                                modifier = Modifier.size(14.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Tanggal Lahir",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Medium,
                            color = Color(0xFF212121)
                        )
                    }

                    // --- 2. Box yang Dapat Diklik yang Membungkus TextField ---
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable(
                                interactionSource = remember { MutableInteractionSource() },
                                indication = LocalIndication.current,
                                onClick = { viewModel.toggleDatePicker(true) }
                            )
                    ) {
                        OutlinedTextField(
                            value = uiState.profile.birthDate,
                            onValueChange = { },
                            readOnly = true,
                            enabled = false, // KUNCI agar klik melewati TextField ke Box
                            placeholder = {
                                Text(
                                    text = "1990-05-15",
                                    color = Color.Black,
                                    fontSize = 14.sp
                                )
                            },
                            modifier = Modifier.fillMaxWidth(),
                            colors = OutlinedTextFieldDefaults.colors(
                                // Warna Enabled/Focused
                                focusedContainerColor = Color.White,
                                unfocusedContainerColor = Color.White,
                                focusedBorderColor = CustomAccentColor, // Warna aksen
                                unfocusedBorderColor = Color(0xFFE0E0E0),
                                focusedTextColor = Color.Black,
                                unfocusedTextColor = Color.Black,

                                // Warna Disabled/Click-Through (Meniru tampilan aktif)
                                disabledContainerColor = Color.White,
                                disabledBorderColor = Color(0xFFE0E0E0),
                                disabledTextColor = Color.Black,
                                disabledPlaceholderColor = Color.Black,
                            ),
                            shape = RoundedCornerShape(12.dp),
                            singleLine = true,
                            trailingIcon = {
                                Icon(
                                    imageVector = Icons.Default.CalendarToday,
                                    contentDescription = "Calendar",
                                    tint = Color(0xFF9E9E9E),
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                        )
                    }
                }
            }

            // Tinggi Badan
            item {
                InputField(
                    label = "Tinggi Badan",
                    value = uiState.profile.height,
                    onValueChange = { viewModel.updateHeight(it) },
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
                    value = uiState.profile.weight,
                    onValueChange = { viewModel.updateWeight(it) },
                    placeholder = "60",
                    icon = R.drawable.ic_weight,
                    iconTint = Color(0xFF4CAF50),
                    suffix = "kg",
                    keyboardType = KeyboardType.Number
                )
            }

            // Asal Kota
            item {
                Column(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(bottom = 8.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(24.dp)
                                .clip(CircleShape)
                                .background(Color(0xFFA78BFA).copy(alpha = 0.15f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_location),
                                contentDescription = "Asal Kota",
                                tint = Color(0xFFA78BFA),
                                modifier = Modifier.size(14.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Asal Kota",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Medium,
                            color = Color(0xFF212121)
                        )
                    }

                    ExposedDropdownMenuBox(
                        expanded = cityExpanded,
                        onExpandedChange = { cityExpanded = !cityExpanded },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        OutlinedTextField(
                            value = selectedCityDisplay,
                            onValueChange = {},
                            readOnly = true,
                            placeholder = {
                                Text(
                                    text = "Pilih kota",
                                    color = Color.Black,
                                    fontSize = 14.sp
                                )
                            },
                            trailingIcon = {
                                Icon(
                                    imageVector = Icons.Default.KeyboardArrowDown,
                                    contentDescription = "Dropdown",
                                    tint = Color(0xFF9E9E9E)
                                )
                            },
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = CustomAccentColor, // Warna aksen
                                unfocusedBorderColor = Color(0xFFE0E0E0),
                                focusedContainerColor = Color.White,
                                unfocusedContainerColor = Color.White,
                                disabledContainerColor = Color.White
                            ),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier
                                .menuAnchor()
                                .fillMaxWidth()
                        )

                        ExposedDropdownMenu(
                            expanded = cityExpanded,
                            onDismissRequest = { cityExpanded = false },
                            containerColor = Color.White
                        ) {
                            cities.forEach { city ->
                                DropdownMenuItem(
                                    text = { Text(city) },
                                    onClick = {
                                        selectedCityDisplay = city
                                        cityExpanded = false
                                        viewModel.updateCity(city)
                                    },
                                    colors = MenuDefaults.itemColors(
                                        textColor = Color.Black
                                    )
                                )
                            }
                        }
                    }
                }
            }

            item {
                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = {
                        coroutineScope.launch {
                            viewModel.saveProfile()
                            navController.popBackStack()
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = CustomAccentColor // Warna aksen
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

                OutlinedButton(
                    onClick = { navController.popBackStack() },
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

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clickable(
                    enabled = onClick != null,
                    interactionSource = remember { MutableInteractionSource() },
                    indication = LocalIndication.current
                ) { onClick?.invoke() }
        ) {
            OutlinedTextField(
                value = value,
                onValueChange = onValueChange,
                placeholder = {
                    Text(
                        text = placeholder,
                        color = Color.Black,
                        fontSize = 14.sp
                    )
                },
                modifier = Modifier.fillMaxWidth(),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White,
                    disabledContainerColor = Color.White,
                    focusedBorderColor = CustomAccentColor, // <-- Menggunakan CustomAccentColor
                    unfocusedBorderColor = Color(0xFFE0E0E0),
                    focusedTextColor = Color.Black,
                    unfocusedTextColor = Color.Black,
                    unfocusedPlaceholderColor = Color.Black,
                    focusedPlaceholderColor = Color.Black
                ),
                shape = RoundedCornerShape(12.dp),
                singleLine = true,
                readOnly = readOnly || onClick != null,
                enabled = true,
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
}

@Preview(showBackground = true)
@Composable
private fun EditProfileScreenPreview() {
    val navController = NavController(LocalContext.current)
    EditProfileScreen(navController = navController)
}