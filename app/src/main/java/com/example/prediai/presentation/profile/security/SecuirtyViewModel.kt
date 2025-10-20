package com.example.prediai.presentation.profile.security

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.prediai.domain.repository.AuthRepository
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
    val isCameraPermissionEnabled: Boolean = false,
    val isLocationEnabled: Boolean = false     // 🔑 Perubahan: Mengganti Notifikasi menjadi Lokasi
)

@HiltViewModel
class SecurityViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(SecurityUiState())
    val uiState = _uiState.asStateFlow()
    // ... (updateEmail dan sendPasswordReset tidak berubah) ...

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

    fun updateCameraPermissionState(isEnabled: Boolean) {
        _uiState.update { it.copy(isCameraPermissionEnabled = isEnabled) }
    }

    // 🔑 Perubahan: Fungsi pembaruan status lokasi
    fun updateLocationState(isEnabled: Boolean) {
        _uiState.update { it.copy(isLocationEnabled = isEnabled) }
    }

    // 🔑 Perubahan: Mengganti Notifikasi menjadi Lokasi di fungsi loading awal
    fun loadInitialPermissions(isCameraEnabled: Boolean, isLocationEnabled: Boolean) {
        _uiState.update {
            it.copy(
                isCameraPermissionEnabled = isCameraEnabled,
                isLocationEnabled = isLocationEnabled, // Diperbarui
                emailInput = authRepository.getCurrentUser()?.email ?: ""
            )
        }
    }
}