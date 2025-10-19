package com.example.prediai.presentation.profile.security

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.prediai.domain.repository.AuthRepository // Asumsi: Anda memiliki AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class SecurityUiState(
    val emailInput: String = "",
    val message: String? = null,
    val isLoading: Boolean = false,
    val isSuccess: Boolean = false,
    val isCameraPermissionEnabled: Boolean = false, // Status izin nyata harus di-fetch dari sistem
    val isNotificationEnabled: Boolean = false     // Status izin nyata harus di-fetch dari sistem
)

@HiltViewModel
class SecurityViewModel @Inject constructor(
    private val authRepository: AuthRepository // Asumsi: AuthRepository memiliki fungsi resetPassword
) : ViewModel() {

    private val _uiState = MutableStateFlow(SecurityUiState())
    val uiState = _uiState.asStateFlow()

    fun updateEmail(email: String) {
        _uiState.update { it.copy(emailInput = email, message = null, isSuccess = false) }
    }

    fun sendPasswordReset() {
        val email = _uiState.value.emailInput
        if (email.isBlank()) {
            _uiState.update { it.copy(message = "Email tidak boleh kosong.") }
            return
        }

        _uiState.update { it.copy(isLoading = true, message = null, isSuccess = false) }

        viewModelScope.launch {
            val result = authRepository.sendPasswordResetEmail(email)
            result.onSuccess {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        isSuccess = true,
                        message = "Tautan reset kata sandi telah dikirim ke $email."
                    )
                }
            }.onFailure { error ->
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        isSuccess = false,
                        message = "Gagal: ${error.message ?: "Terjadi kesalahan."}"
                    )
                }
            }
        }
    }

    // Fungsi untuk mensimulasikan pembaruan status izin di UI,
    // tetapi aksi membuka pengaturan akan ditangani oleh Composable
    fun updateCameraPermissionState(isEnabled: Boolean) {
        _uiState.update { it.copy(isCameraPermissionEnabled = isEnabled) }
    }

    fun updateNotificationState(isEnabled: Boolean) {
        _uiState.update { it.copy(isNotificationEnabled = isEnabled) }
    }

    // Fungsi yang HARUS DIPANGGIL oleh Composable saat halaman dibuka
    // untuk mendapatkan status izin yang sebenarnya dari sistem Android
    fun loadInitialPermissions(isCameraEnabled: Boolean, isNotificationEnabled: Boolean) {
        _uiState.update {
            it.copy(
                isCameraPermissionEnabled = isCameraEnabled,
                isNotificationEnabled = isNotificationEnabled,
                // Prefill email with current user's email if available (optional)
                emailInput = authRepository.getCurrentUser()?.email ?: ""
            )
        }
    }
}