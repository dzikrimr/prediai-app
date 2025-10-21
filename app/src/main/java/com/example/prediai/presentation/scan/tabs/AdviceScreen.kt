package com.example.prediai.presentation.scan.tabs

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.prediai.R
import com.example.prediai.domain.model.NearbyPlace
import com.example.prediai.presentation.scan.ScanResultViewModel
import com.example.prediai.presentation.scan.comps.HealthcareItem
import com.example.prediai.presentation.scan.comps.MapsConfirmationDialog

fun openGoogleMaps(context: Context, name: String, address: String) {
    val searchUri = Uri.parse("geo:0,0?q=$name, $address")
    val mapIntent = Intent(Intent.ACTION_VIEW, searchUri).apply {
        setPackage("com.google.android.apps.maps")
    }

    if (mapIntent.resolveActivity(context.packageManager) != null) {
        context.startActivity(mapIntent)
    } else {
        // URI fallback sedikit diperbaiki untuk stabilitas
        val webUri = Uri.parse("https://maps.google.com/?q=$name, $address")
        val webIntent = Intent(Intent.ACTION_VIEW, webUri)
        context.startActivity(webIntent)
    }
}

@Composable
fun AdviceScreen(
    viewModel: ScanResultViewModel,
    onNavigateToDoctor: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current
    var showMapsDialog by remember { mutableStateOf(false) }
    var selectedPlaceForMaps by remember { mutableStateOf<NearbyPlace?>(null) }

    // 🔑 1. DEKLARASI SCROLL STATE
    val scrollState = rememberScrollState()

    LaunchedEffect(uiState.nearbyPlaces) {
        Log.d("AdviceScreen", "nearbyPlaces changed: ${uiState.nearbyPlaces.size} - ${uiState.nearbyPlaces}")
    }

    val locationPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        if (permissions[android.Manifest.permission.ACCESS_FINE_LOCATION] == true ||
            permissions[android.Manifest.permission.ACCESS_COARSE_LOCATION] == true) {
            viewModel.findNearbyHealthcare()
        } else {
            viewModel.onLocationPermissionDenied()
        }
    }

    LaunchedEffect(Unit) {
        locationPermissionLauncher.launch(arrayOf(
            android.Manifest.permission.ACCESS_FINE_LOCATION,
            android.Manifest.permission.ACCESS_COARSE_LOCATION
        ))
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF5F5F5))
            .padding(horizontal = 16.dp)
            .verticalScroll(scrollState)
    ) {
        Spacer(modifier = Modifier.height(8.dp))

        Surface(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            color = Color.White,
            shadowElevation = 2.dp
        ) {
            Column(
                modifier = Modifier.padding(20.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.Top
                ) {
                    Box(
                        modifier = Modifier
                            .size(48.dp)
                            .background(
                                color = Color(0xFFFFEBEE),
                                shape = RoundedCornerShape(12.dp)
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_doctor),
                            contentDescription = "Doctor",
                            tint = Color(0xFFE53935),
                            modifier = Modifier.size(28.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "Konsultasi Profesional Direkomendasikan",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF212121),
                            lineHeight = 24.sp
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = "Kami merekomendasikan Anda untuk konsultasi dengan profesional kesehatan agar evaluasi yang tepat dan perawatan yang dipersonalisasi.",
                            fontSize = 14.sp,
                            color = Color(0xFF757575),
                            lineHeight = 20.sp
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    // GANTI aksi menjadi panggilan callback navigasi
                    onClick = onNavigateToDoctor,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF00B4A3)
                    )
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_consult),
                        contentDescription = "Consult",
                        tint = Color.White,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Konsultasi ke dokter langsung",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Fasilitas Kesehatan Terdekat",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF212121)
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        Log.d("AdviceScreen", "Nearby places: ${uiState.nearbyPlaces.size} - ${uiState.nearbyPlaces}")
        when {
            uiState.isLocationLoading -> {
                Box(
                    modifier = Modifier.fillMaxWidth().height(100.dp),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }
            uiState.locationError != null -> {
                Text(
                    text = uiState.locationError!!,
                    color = Color.Red,
                    modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)
                )
            }
            else -> {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    uiState.nearbyPlaces.forEach { place ->
                        HealthcareItem(
                            name = place.name,
                            specialty = place.address,
                            onClick = {
                                selectedPlaceForMaps = place
                                showMapsDialog = true
                            }
                        )
                    }
                }
            }
        }
    }
    if (showMapsDialog && selectedPlaceForMaps != null) {
        val place = selectedPlaceForMaps!!
        MapsConfirmationDialog(
            onConfirm = {
                openGoogleMaps(context, place.name, place.address)
                showMapsDialog = false
                selectedPlaceForMaps = null
            },
            onDismiss = {
                showMapsDialog = false
                selectedPlaceForMaps = null
            }
        )
    }
}