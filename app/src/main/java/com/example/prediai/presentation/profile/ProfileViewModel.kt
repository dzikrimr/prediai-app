package com.example.prediai.presentation.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.prediai.domain.model.UserProfile
import com.example.prediai.domain.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class ProfileUiState(
    val profile: UserProfile = UserProfile(),
    val email: String = "",
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val showLogoutDialog: Boolean = false
)

sealed class NavigationEvent {
    data class NavigateTo(val route: String) : NavigationEvent()
}

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(ProfileUiState(isLoading = true))
    val uiState: StateFlow<ProfileUiState> = _uiState.asStateFlow()
    private val _navigationEvent = MutableSharedFlow<NavigationEvent>()
    val navigationEvent = _navigationEvent.asSharedFlow()

    init {
        loadUserProfile()
    }

    fun onLogoutClicked() {
        // Tampilkan dialog saat tombol logout diklik
        _uiState.update { it.copy(showLogoutDialog = true) }
    }

    fun onDismissLogoutDialog() {
        // Sembunyikan dialog jika pengguna membatalkan
        _uiState.update { it.copy(showLogoutDialog = false) }
    }

    fun onConfirmLogout() {
        viewModelScope.launch {
            _uiState.update { it.copy(showLogoutDialog = false) }
            userRepository.logout()

            // --- KIRIM EVENT YANG LEBIH LENGKAP ---
            _navigationEvent.emit(NavigationEvent.NavigateTo("login"))
            // --- AKHIR PERUBAHAN ---
        }
    }

    fun loadUserProfile() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            val firebaseUser = userRepository.getCurrentUser()
            val uid = firebaseUser?.uid ?: run {
                _uiState.update { it.copy(isLoading = false, errorMessage = "Pengguna tidak ditemukan") }
                return@launch
            }

            val userEmail = firebaseUser.email ?: "Email tidak tersedia"

            // MENGGUNAKAN .take(1) untuk hanya mendapatkan nilai cache awal (untuk kecepatan)
            userRepository.getCachedUserProfile()
                .take(1) // <-- Ambil nilai pertama dari cache, lalu hentikan koleksi
                .collect { cachedProfile ->

                    // 1. Tampilkan data cache segera
                    if (cachedProfile.name.isNotEmpty() || !cachedProfile.profileImageUrl.isNullOrEmpty()) {
                        _uiState.update { it.copy(
                            profile = cachedProfile,
                            email = userEmail,
                            isLoading = false
                        ) }
                    }

                    // 2. SELALU FETCH DARI FIREBASE (RTDB) untuk memastikan data terbaru (termasuk URL foto)
                    val firebaseProfile = userRepository.getUserProfileFromFirebase(uid)

                    if (firebaseProfile != null) {
                        // Perbarui UI state dan cache dengan data Realtime Database terbaru
                        userRepository.saveUserProfileToCache(uid, firebaseProfile)
                        _uiState.update { it.copy(
                            profile = firebaseProfile,
                            email = userEmail,
                            isLoading = false
                        ) }
                    } else if (_uiState.value.profile.name.isEmpty()) {
                        _uiState.update { it.copy(isLoading = false, errorMessage = "Gagal memuat profil") }
                    }
                }
        }
    }
}