package com.example.prediai.presentation.auth.comps

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.prediai.R

/**
 * Komponen formulir login dengan input email, password, dan tombol submit.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginForm(
    email: String,
    password: String,
    onEmailChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onLoginClick: () -> Unit,
    isLoading: Boolean,
    errorMessage: String?,
    onDismissError: () -> Unit,
    onForgotPasswordClick: () -> Unit = {},
    onRegisterClick: () -> Unit = {},
    onGoogleSignInClick: () -> Unit = {}
) {
    var passwordVisible by remember { mutableStateOf(false) }

    val primaryColor = Color(0xFF00B4A3)

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp)
            .padding(vertical = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        // ✅ Logo
        androidx.compose.foundation.Image(
            painter = painterResource(id = R.drawable.prediailogo),
            contentDescription = "PrediAI Logo",
            modifier = Modifier
                .size(110.dp)
        )
        // ✅ Teks PrediAI
        Text(
            text = "PrediAI",
            fontWeight = FontWeight.Bold,
            fontSize = 24.sp,
            color = Color(0xFF2D3748)
        )
        // Form Container (semua komponen di dalam Card ini)
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(15.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Title
                Text(
                    text = "Masuk",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color(0xFF1F2937),
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )

                // Email Field
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(
                        text = "Email",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color(0xFF374151)
                    )

                    OutlinedTextField(
                        value = email,
                        onValueChange = onEmailChange,
                        placeholder = {
                            Text("Masukkan email Anda", color = Color(0xFFADAEBC), fontSize = 16.sp)
                        },
                        modifier = Modifier.fillMaxWidth(),
                        enabled = !isLoading,
                        leadingIcon = {
                            Icon(Icons.Default.Email, contentDescription = "Email", tint = Color(0xFFADAEBC))
                        },
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = primaryColor,
                            unfocusedBorderColor = Color(0xFFE2E8F0),
                            focusedTextColor = Color(0xFF2D3748),
                            unfocusedTextColor = Color(0xFF2D3748)
                        )
                    )
                }

                // Password Field
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(
                        text = "Password",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color(0xFF374151)
                    )

                    OutlinedTextField(
                        value = password,
                        onValueChange = onPasswordChange,
                        placeholder = {
                            Text("Masukkan password", color = Color(0xFFADAEBC), fontSize = 16.sp)
                        },
                        modifier = Modifier.fillMaxWidth(),
                        enabled = !isLoading,
                        leadingIcon = {
                            Icon(Icons.Default.Lock, contentDescription = "Password", tint = Color(0xFFADAEBC))
                        },
                        trailingIcon = {
                            IconButton(onClick = { passwordVisible = !passwordVisible }) {
                                Icon(
                                    imageVector = if (passwordVisible) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                                    contentDescription = if (passwordVisible) "Sembunyikan Password" else "Tampilkan Password",
                                    tint = Color(0xFFA0AEC0)
                                )
                            }
                        },
                        visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = primaryColor,
                            unfocusedBorderColor = Color(0xFFE2E8F0),
                            focusedTextColor = Color(0xFF2D3748),
                            unfocusedTextColor = Color(0xFF2D3748)
                        )
                    )
                }

                // Error Message
                errorMessage?.let {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFFFED7D7)),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(it, color = Color(0xFF9B2C2C), fontSize = 14.sp, modifier = Modifier.weight(1f))
                            TextButton(onClick = onDismissError) {
                                Text("✕", color = Color(0xFF9B2C2C))
                            }
                        }
                    }
                }

                // Login Button
                Button(
                    onClick = onLoginClick,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp),
                    enabled = !isLoading && email.isNotBlank() && password.isNotBlank(),
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
                        Text("Masuk", color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.SemiBold)
                    }
                }

                // Forgot Password Link
                TextButton(onClick = onForgotPasswordClick, modifier = Modifier.fillMaxWidth()) {
                    Text("Lupa Password?", color = primaryColor, fontSize = 14.sp, fontWeight = FontWeight.Medium)
                }

                // Divider with "atau"
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Divider(modifier = Modifier.weight(1f), color = Color(0xFFE2E8F0))
                    Text("atau", modifier = Modifier.padding(horizontal = 16.dp), color = Color(0xFF718096), fontSize = 14.sp)
                    Divider(modifier = Modifier.weight(1f), color = Color(0xFFE2E8F0))
                }

                // Google Sign In Button
                OutlinedButton(
                    onClick = onGoogleSignInClick,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.outlinedButtonColors(containerColor = Color.White),
                    border = ButtonDefaults.outlinedButtonBorder.copy(width = 1.dp)
                ) {
                    Row(
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_google), // Ganti dengan nama file ikon Anda
                            contentDescription = "Google Logo",
                            modifier = Modifier.size(20.dp), // Sesuaikan ukuran ikon jika perlu
                            tint = Color.Unspecified // Penting agar warna asli ikon ditampilkan
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Text("Masuk dengan Google", color = Color(0xFF2D3748), fontSize = 14.sp, fontWeight = FontWeight.Medium)
                    }
                }

                // Register Section
                Row(
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Belum punya akun? ",
                        color = Color(0xFF718096),
                        fontSize = 14.sp
                    )
                    TextButton(
                        onClick = onRegisterClick,
                        contentPadding = PaddingValues(0.dp)
                    ) {
                        Text(text = "Daftar",
                            color = primaryColor,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
            }
        }
    }
}
