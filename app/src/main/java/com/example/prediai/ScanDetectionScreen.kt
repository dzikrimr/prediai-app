package com.example.prediai

import android.Manifest
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.io.File
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScanDetectionScreen(navController: NavController) {
    var selectedTab by remember { mutableStateOf("Kuku") }
    var hasCameraPermission by remember { mutableStateOf(false) }
    var isCameraActive by remember { mutableStateOf(false) }
    var isBackCamera by remember { mutableStateOf(true) }
    var cameraProvider by remember { mutableStateOf<ProcessCameraProvider?>(null) }
    var previewView by remember { mutableStateOf<PreviewView?>(null) }
    var imageCapture by remember { mutableStateOf<ImageCapture?>(null) }

    // Track captured images
    var kukuImagePath by remember { mutableStateOf<String?>(null) }
    var lidahImagePath by remember { mutableStateOf<String?>(null) }
    var showCaptureSuccess by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val scope = rememberCoroutineScope()

    // Camera executor for image capture
    val cameraExecutor: ExecutorService = remember { Executors.newSingleThreadExecutor() }

    // Launcher for camera permission
    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        hasCameraPermission = isGranted
        if (isGranted) {
            isCameraActive = true
        } else {
            errorMessage = "Izin kamera diperlukan untuk melakukan scan."
        }
    }

    // Function to bind camera
    fun bindCamera() {
        previewView?.let { pv ->
            val cameraProviderFuture = ProcessCameraProvider.getInstance(context)
            cameraProviderFuture.addListener({
                try {
                    val provider = cameraProviderFuture.get()
                    cameraProvider = provider

                    val preview = Preview.Builder().build().also { previewBuilder ->
                        previewBuilder.setSurfaceProvider(pv.surfaceProvider)
                    }

                    // Initialize ImageCapture
                    imageCapture = ImageCapture.Builder().build()

                    val cameraSelector = if (isBackCamera) {
                        CameraSelector.DEFAULT_BACK_CAMERA
                    } else {
                        CameraSelector.DEFAULT_FRONT_CAMERA
                    }

                    provider.unbindAll()
                    provider.bindToLifecycle(
                        lifecycleOwner,
                        cameraSelector,
                        preview,
                        imageCapture
                    )
                } catch (e: Exception) {
                    errorMessage = "Gagal menginisialisasi kamera: ${e.message}"
                    isCameraActive = false
                }
            }, ContextCompat.getMainExecutor(context))
        }
    }

    // Function to capture photo
    fun capturePhoto() {
        if (imageCapture == null || showCaptureSuccess) return

        val photoFile = File(
            context.cacheDir,
            "scan_${selectedTab.lowercase()}_${System.currentTimeMillis()}.jpg"
        )

        val outputOptions = ImageCapture.OutputFileOptions.Builder(photoFile).build()

        imageCapture?.takePicture(
            outputOptions,
            cameraExecutor,
            object : ImageCapture.OnImageSavedCallback {
                override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
                    showCaptureSuccess = true

                    if (selectedTab == "Kuku") {
                        kukuImagePath = photoFile.absolutePath
                    } else if (selectedTab == "Lidah") {
                        lidahImagePath = photoFile.absolutePath
                    }

                    scope.launch {
                        delay(1000)
                        showCaptureSuccess = false
                        if (kukuImagePath != null && lidahImagePath != null) {
                            cameraProvider?.unbindAll()
                            navController.navigate("questionnaire")
                        } else {
                            if (selectedTab == "Kuku" && lidahImagePath == null) {
                                selectedTab = "Lidah"
                            } else if (selectedTab == "Lidah" && kukuImagePath == null) {
                                selectedTab = "Kuku"
                            }
                        }
                    }
                }

                override fun onError(exception: ImageCaptureException) {
                    scope.launch {
                        errorMessage = "Gagal mengambil foto: ${exception.message}"
                        showCaptureSuccess = false
                    }
                }
            }
        )
    }

    // LaunchedEffect to rebind camera when isBackCamera or isCameraActive changes
    LaunchedEffect(isBackCamera, isCameraActive, hasCameraPermission) {
        if (hasCameraPermission && isCameraActive) {
            bindCamera()
        }
    }

    // Request camera permission if not granted
    LaunchedEffect(Unit) {
        when (ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA)) {
            PackageManager.PERMISSION_GRANTED -> {
                hasCameraPermission = true
                isCameraActive = true
            }
            else -> {
                permissionLauncher.launch(Manifest.permission.CAMERA)
            }
        }
    }

    // Clean up camera and executor on dispose
    DisposableEffect(Unit) {
        onDispose {
            cameraProvider?.unbindAll()
            cameraExecutor.shutdown()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF8F9FA))
    ) {
        // Top App Bar
        TopAppBar(
            title = {
                Text(
                    text = "Scan Deteksi",
                    fontWeight = FontWeight.Medium,
                    color = Color.Black
                )
            },
            navigationIcon = {
                IconButton(onClick = { navController.navigateUp() }) {
                    Icon(
                        Icons.Default.ArrowBack,
                        contentDescription = "Back",
                        tint = Color.Black
                    )
                }
            },
            actions = {
                IconButton(onClick = { /* Handle help */ }) {
                    Icon(
                        Icons.Default.Help,
                        contentDescription = "Help",
                        tint = Color.Gray
                    )
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = Color.White
            )
        )

        // Error Message
        errorMessage?.let { error ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.errorContainer
                )
            ) {
                Row(
                    modifier = Modifier.padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Error,
                        contentDescription = "Error",
                        tint = MaterialTheme.colorScheme.onErrorContainer,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = error,
                        color = MaterialTheme.colorScheme.onErrorContainer,
                        fontSize = 12.sp
                    )
                    Spacer(modifier = Modifier.weight(1f))
                    IconButton(
                        onClick = { errorMessage = null },
                        modifier = Modifier.size(20.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Close",
                            tint = MaterialTheme.colorScheme.onErrorContainer,
                            modifier = Modifier.size(14.dp)
                        )
                    }
                }
            }
        }

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Feature Card
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFE8F5F3))
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(48.dp)
                                .background(Color(0xFF00BFA5), CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                Icons.Default.PhotoCamera,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(24.dp)
                            )
                        }

                        Spacer(modifier = Modifier.width(12.dp))

                        Column {
                            Text(
                                text = "Scan Kuku & Lidah",
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 16.sp
                            )
                            Text(
                                text = "Deteksi dini potensi diabetes",
                                fontSize = 14.sp,
                                color = Color.Gray
                            )
                        }
                    }

                    Text(
                        text = "Ambil foto kuku dan lidah Anda dalam satu frame untuk analisis komprehensif menggunakan AI.",
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                        fontSize = 14.sp,
                        color = Color.Gray
                    )

                    Spacer(modifier = Modifier.height(8.dp))
                }
            }

            // Scan Guide Section
            item {
                Text(
                    text = "Panduan Scan",
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 18.sp,
                    color = Color.Black
                )
            }

            // Guide Steps
            item {
                Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                    GuideStep(
                        number = "1",
                        title = "Posisi Tangan",
                        description = "Letakkan tangan dengan kuku menghadap kamera, pastikan pencahayaan cukup"
                    )

                    GuideStep(
                        number = "2",
                        title = "Posisi Lidah",
                        description = "Julurkan lidah dengan jelas, pastikan terlihat dalam frame yang sama"
                    )

                    GuideStep(
                        number = "3",
                        title = "Ambil Foto",
                        description = "Tekan tombol scan untuk mengambil foto dan memulai analisis"
                    )
                }
            }

            // Photo Section in Card (Tab Selection + Camera Section)
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {
                        // Tab Selection with indicators
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .border(
                                    2.dp,
                                    Color(0xFF00BFA5),
                                    RoundedCornerShape(8.dp)
                                )
                                .clip(RoundedCornerShape(8.dp)),
                            horizontalArrangement = Arrangement.SpaceEvenly
                        ) {
                            TabButton(
                                text = "Kuku",
                                isSelected = selectedTab == "Kuku",
                                isCaptured = kukuImagePath != null,
                                onClick = { if (!showCaptureSuccess) selectedTab = "Kuku" },
                                modifier = Modifier.weight(1f)
                            )
                            TabButton(
                                text = "Lidah",
                                isSelected = selectedTab == "Lidah",
                                isCaptured = lidahImagePath != null,
                                onClick = { if (!showCaptureSuccess) selectedTab = "Lidah" },
                                modifier = Modifier.weight(1f)
                            )
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        // Camera Section
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            // Camera Preview with Overlay
                            Box(
                                modifier = Modifier
                                    .size(240.dp)
                                    .clip(RoundedCornerShape(12.dp)),
                                contentAlignment = Alignment.Center
                            ) {
                                if (hasCameraPermission && isCameraActive) {
                                    AndroidView(
                                        factory = { context ->
                                            PreviewView(context).also { pv ->
                                                previewView = pv
                                            }
                                        },
                                        modifier = Modifier.fillMaxSize()
                                    )

                                    // Overlay Guide
                                    Canvas(
                                        modifier = Modifier.fillMaxSize()
                                    ) {
                                        drawScanGuide(selectedTab)
                                    }

                                    // Success overlay
                                    if (showCaptureSuccess) {
                                        Box(
                                            modifier = Modifier
                                                .fillMaxSize()
                                                .background(Color.Black.copy(alpha = 0.6f)),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Column(
                                                horizontalAlignment = Alignment.CenterHorizontally
                                            ) {
                                                Icon(
                                                    Icons.Default.CheckCircle,
                                                    contentDescription = null,
                                                    tint = Color(0xFF4CAF50),
                                                    modifier = Modifier.size(48.dp)
                                                )
                                                Spacer(modifier = Modifier.height(8.dp))
                                                Text(
                                                    text = "Foto ${selectedTab} Berhasil!",
                                                    color = Color.White,
                                                    fontSize = 16.sp,
                                                    fontWeight = FontWeight.SemiBold
                                                )
                                            }
                                        }
                                    }
                                } else {
                                    Box(
                                        modifier = Modifier
                                            .fillMaxSize()
                                            .background(Color.Gray.copy(alpha = 0.3f), RoundedCornerShape(12.dp)),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Icon(
                                            Icons.Default.PhotoCamera,
                                            contentDescription = null,
                                            tint = Color.Gray,
                                            modifier = Modifier.size(32.dp)
                                        )
                                    }
                                }
                            }

                            Spacer(modifier = Modifier.height(12.dp))

                            Text(
                                text = if (hasCameraPermission && isCameraActive)
                                    "Posisikan ${selectedTab.lowercase()} dalam area panduan (${if(isBackCamera) "Kamera Belakang" else "Kamera Depan"})"
                                else "Kamera akan aktif saat tombol scan ditekan",
                                fontSize = 14.sp,
                                color = Color.Gray,
                                textAlign = TextAlign.Center
                            )

                            Spacer(modifier = Modifier.height(24.dp))

                            // Action Buttons
                            Row(
                                horizontalArrangement = Arrangement.spacedBy(16.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                IconButton(
                                    onClick = { /* Handle gallery */ },
                                    modifier = Modifier
                                        .size(56.dp)
                                        .background(Color.Gray.copy(alpha = 0.2f), CircleShape)
                                ) {
                                    Icon(
                                        Icons.Default.Image,
                                        contentDescription = "Gallery",
                                        tint = Color.Gray
                                    )
                                }

                                // Main Camera Button
                                IconButton(
                                    onClick = {
                                        if (hasCameraPermission && isCameraActive && !showCaptureSuccess) {
                                            capturePhoto()
                                        } else if (!hasCameraPermission) {
                                            permissionLauncher.launch(Manifest.permission.CAMERA)
                                        } else if (!isCameraActive) {
                                            isCameraActive = true
                                        }
                                    },
                                    modifier = Modifier
                                        .size(80.dp)
                                        .background(
                                            if (showCaptureSuccess) Color.Gray else Color(0xFF00BFA5),
                                            CircleShape
                                        ),
                                    enabled = !showCaptureSuccess
                                ) {
                                    Icon(
                                        if (showCaptureSuccess) Icons.Default.Check else Icons.Default.PhotoCamera,
                                        contentDescription = "Take Photo",
                                        tint = Color.White,
                                        modifier = Modifier.size(32.dp)
                                    )
                                }

                                // Camera Flip Button
                                IconButton(
                                    onClick = {
                                        if (!showCaptureSuccess) {
                                            isBackCamera = !isBackCamera
                                        }
                                    },
                                    modifier = Modifier
                                        .size(56.dp)
                                        .background(Color.Gray.copy(alpha = 0.2f), CircleShape),
                                    enabled = !showCaptureSuccess
                                ) {
                                    Icon(
                                        Icons.Default.FlipCameraAndroid,
                                        contentDescription = "Flip Camera",
                                        tint = if (showCaptureSuccess) Color.Gray.copy(alpha = 0.5f) else Color.Gray
                                    )
                                }
                            }
                        }
                    }
                }
            }

            // Tips Section
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFFFF8E1))
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                Icons.Default.Lightbulb,
                                contentDescription = null,
                                tint = Color(0xFFFF8F00),
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Tips untuk Hasil Terbaik",
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 16.sp,
                                color = Color(0xFFFF8F00)
                            )
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        val tips = listOf(
                            "Pastikan pencahayaan yang cukup",
                            "Hindari bayangan pada kuku dan lidah",
                            "Jaga kamera tetap stabil",
                            "Pastikan kuku dan lidah terlihat jelas",
                            "Ikuti panduan garis hijau pada kamera"
                        )

                        tips.forEach { tip ->
                            Row(
                                modifier = Modifier.padding(vertical = 2.dp),
                                verticalAlignment = Alignment.Top
                            ) {
                                Text(
                                    text = "• ",
                                    color = Color(0xFFFF8F00),
                                    fontSize = 14.sp
                                )
                                Text(
                                    text = tip,
                                    fontSize = 14.sp,
                                    color = Color(0xFF5D4037)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun TabButton(
    text: String,
    isSelected: Boolean,
    isCaptured: Boolean = false,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Button(
        onClick = onClick,
        modifier = modifier.fillMaxWidth(),
        colors = ButtonDefaults.buttonColors(
            containerColor = if (isSelected) Color(0xFF00BFA5) else Color.Transparent,
            contentColor = if (isSelected) Color.White else Color(0xFF00BFA5)
        ),
        elevation = ButtonDefaults.buttonElevation(0.dp),
        shape = RoundedCornerShape(6.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            if (isCaptured) {
                Icon(
                    Icons.Default.CheckCircle,
                    contentDescription = null,
                    modifier = Modifier.size(16.dp),
                    tint = if (isSelected) Color.White else Color(0xFF4CAF50)
                )
                Spacer(modifier = Modifier.width(4.dp))
            }
            Text(
                text = text,
                fontWeight = FontWeight.Medium,
                fontSize = 14.sp
            )
        }
    }
}

fun DrawScope.drawScanGuide(selectedTab: String) {
    val guideColor = Color(0xFF00B4A3)
    val strokeWidth = 3.dp.toPx()
    val pathEffect = PathEffect.dashPathEffect(floatArrayOf(15f, 10f), 0f)

    val centerX = size.width / 2
    val centerY = size.height / 2

    if (selectedTab == "Kuku") {
        // Draw nail guide - rectangular with rounded corners
        val guideWidth = size.width * 0.6f
        val guideHeight = size.height * 0.4f
        val left = centerX - guideWidth / 2
        val top = centerY - guideHeight / 2
        val right = centerX + guideWidth / 2
        val bottom = centerY + guideHeight / 2
        val cornerRadius = 20f

        val path = Path().apply {
            moveTo(left + cornerRadius, top)
            lineTo(right - cornerRadius, top)
            quadraticBezierTo(right, top, right, top + cornerRadius)
            lineTo(right, bottom - cornerRadius)
            quadraticBezierTo(right, bottom, right - cornerRadius, bottom)
            lineTo(left + cornerRadius, bottom)
            quadraticBezierTo(left, bottom, left, bottom - cornerRadius)
            lineTo(left, top + cornerRadius)
            quadraticBezierTo(left, top, left + cornerRadius, top)
            close()
        }

        drawPath(
            path = path,
            color = guideColor,
            style = Stroke(width = strokeWidth, pathEffect = pathEffect)
        )

        // Add corner indicators
        val cornerSize = 15f
        val corners = listOf(
            Pair(left, top),
            Pair(right, top),
            Pair(left, bottom),
            Pair(right, bottom)
        )

        corners.forEach { (x, y) ->
            drawLine(
                color = guideColor,
                start = Offset(x - cornerSize, y),
                end = Offset(x + cornerSize, y),
                strokeWidth = strokeWidth
            )
            drawLine(
                color = guideColor,
                start = Offset(x, y - cornerSize),
                end = Offset(x, y + cornerSize),
                strokeWidth = strokeWidth
            )
        }

    } else {
        // Draw tongue guide - tongue-like shape
        val guideWidth = size.width * 0.5f
        val guideHeight = size.height * 0.6f

        val path = Path().apply {
            moveTo(centerX - guideWidth / 4, centerY - guideHeight / 2)
            quadraticBezierTo(
                centerX, centerY - guideHeight / 2 - 5,
                centerX + guideWidth / 4, centerY - guideHeight / 2
            )
            quadraticBezierTo(
                centerX + guideWidth / 2, centerY - guideHeight / 4,
                centerX + guideWidth / 2, centerY
            )
            quadraticBezierTo(
                centerX + guideWidth / 2, centerY + guideHeight / 4,
                centerX + guideWidth / 4, centerY + guideHeight / 3
            )
            quadraticBezierTo(
                centerX + guideWidth / 8, centerY + guideHeight / 2,
                centerX, centerY + guideHeight / 2 + 8
            )
            quadraticBezierTo(
                centerX - guideWidth / 8, centerY + guideHeight / 2,
                centerX - guideWidth / 4, centerY + guideHeight / 3
            )
            quadraticBezierTo(
                centerX - guideWidth / 2, centerY + guideHeight / 4,
                centerX - guideWidth / 2, centerY
            )
            quadraticBezierTo(
                centerX - guideWidth / 2, centerY - guideHeight / 4,
                centerX - guideWidth / 4, centerY - guideHeight / 2
            )
            close()
        }

        drawPath(
            path = path,
            color = guideColor,
            style = Stroke(width = strokeWidth, pathEffect = pathEffect)
        )

        drawLine(
            color = guideColor.copy(alpha = 0.5f),
            start = Offset(centerX, centerY - guideHeight / 2),
            end = Offset(centerX, centerY + guideHeight / 2 + 8),
            strokeWidth = strokeWidth / 2,
            pathEffect = pathEffect
        )
    }
}

@Composable
fun GuideStep(
    number: String,
    title: String,
    description: String
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.Top
    ) {
        Box(
            modifier = Modifier
                .size(32.dp)
                .background(Color(0xFF00BFA5), CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = number,
                color = Color.White,
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp
            )
        }

        Spacer(modifier = Modifier.width(16.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                fontWeight = FontWeight.SemiBold,
                fontSize = 16.sp
            )
            Text(
                text = description,
                fontSize = 14.sp,
                color = Color.Gray,
                modifier = Modifier.padding(top = 4.dp)
            )
        }
    }
}