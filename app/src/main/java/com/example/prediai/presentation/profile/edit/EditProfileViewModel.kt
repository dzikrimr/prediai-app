// File: EditProfileViewModel.kt
package com.example.prediai.presentation.profile.edit

import android.content.Context
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.prediai.domain.model.UserProfile
import com.example.prediai.domain.repository.HistoryRepository
import com.example.prediai.domain.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine
import java.io.File
import java.io.FileOutputStream
import javax.inject.Inject
import kotlin.coroutines.resume

data class EditProfileUiState(
    val profile: UserProfile = UserProfile(),
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val showDatePicker: Boolean = false,
    val showCityDropdown: Boolean = false
)

@HiltViewModel
class EditProfileViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val historyRepository: HistoryRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(EditProfileUiState(isLoading = true))
    val uiState = _uiState.asStateFlow()

    init {
        loadProfile()
    }

    private fun loadProfile() {
        viewModelScope.launch {
            val uid = userRepository.getCurrentUser()?.uid ?: run {
                _uiState.update { it.copy(isLoading = false, errorMessage = "Pengguna tidak ditemukan") }
                return@launch
            }

            // MENGGUNAKAN .take(1).collect UNTUK MENGAMBIL HANYA NILAI PERTAMA
            userRepository.getCachedUserProfile()
                .take(1) // <-- Operator ini akan menghentikan koleksi setelah item pertama
                .collect { cachedProfile ->

                    // 1. Tampilkan data cache segera (jika ada)
                    if (cachedProfile.name.isNotEmpty() || !cachedProfile.profileImageUrl.isNullOrEmpty()) {
                        _uiState.update { it.copy(profile = cachedProfile, isLoading = false) }
                    }

                    // 2. SELALU FETCH DARI REALTIME DATABASE UNTUK MENGAMBIL DATA TERBARU
                    val firebaseProfile = userRepository.getUserProfileFromFirebase(uid)

                    if (firebaseProfile != null) {
                        // Update UI state dengan data RTDB terbaru (termasuk URL)
                        _uiState.update { it.copy(profile = firebaseProfile, isLoading = false) }
                        userRepository.saveUserProfileToCache(uid, firebaseProfile)
                    } else if (_uiState.value.profile.name.isEmpty()) {
                        // Atur loading false jika tidak ada data sama sekali
                        _uiState.update { it.copy(isLoading = false, errorMessage = "Gagal memuat profil") }
                    }

                    // Tidak perlu return@launch di sini; .take(1) akan membatalkan koleksi.
                }
        }
    }
    fun updateName(name: String) {
        _uiState.update { it.copy(profile = it.profile.copy(name = name)) }
    }

    fun updateBirthDate(birthDate: String) {
        _uiState.update { it.copy(profile = it.profile.copy(birthDate = birthDate)) }
    }

    fun updateHeight(height: String) {
        _uiState.update { it.copy(profile = it.profile.copy(height = height)) }
    }

    fun updateWeight(weight: String) {
        _uiState.update { it.copy(profile = it.profile.copy(weight = weight)) }
    }

    fun updateCity(city: String) {
        _uiState.update { it.copy(profile = it.profile.copy(city = city)) }
    }

    fun toggleDatePicker(show: Boolean) {
        _uiState.update { it.copy(showDatePicker = show) }
    }

    fun toggleCityDropdown(show: Boolean) {
        _uiState.update { it.copy(showCityDropdown = show) }
    }

    suspend fun uploadProfileImage(uri: Uri, context: Context) {
        _uiState.update { it.copy(isLoading = true) }
        try {
            // Convert URI to ByteArray
            val inputStream = context.contentResolver.openInputStream(uri)
            val byteArray = inputStream?.readBytes() ?: throw Exception("Gagal membaca gambar")

            // Upload using HistoryRepository's uploadImage (Cloudinary)
            val result = historyRepository.uploadImage(byteArray)
            result.onSuccess { url ->
                // Menyimpan URL Cloudinary ke state lokal
                _uiState.update { it.copy(profile = it.profile.copy(profileImageUrl = url), isLoading = false) }
            }.onFailure { error ->
                _uiState.update { it.copy(isLoading = false, errorMessage = error.message) }
            }
        } catch (e: Exception) {
            _uiState.update { it.copy(isLoading = false, errorMessage = e.message) }
        }
    }

    suspend fun saveProfile() {
        _uiState.update { it.copy(isLoading = true) }
        val uid = userRepository.getCurrentUser()?.uid ?: run {
            _uiState.update { it.copy(isLoading = false, errorMessage = "Pengguna tidak ditemukan") }
            return
        }
        // saveUserProfileToFirebase -> Menyimpan ke RTDB (sesuai UserRepositoryImpl Anda)
        val result = userRepository.saveUserProfileToFirebase(uid, _uiState.value.profile)
        result.onSuccess {
            userRepository.saveUserProfileToCache(uid, _uiState.value.profile)
            _uiState.update { it.copy(isLoading = false) }
        }.onFailure { error ->
            _uiState.update { it.copy(isLoading = false, errorMessage = error.message) }
        }
    }
}