package com.example.prediai.presentation.quistionere

import androidx.compose.runtime.mutableStateMapOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.prediai.domain.model.UserProfile
import com.example.prediai.domain.repository.AuthRepository
import com.example.prediai.domain.repository.UserRepository
import com.example.prediai.domain.usecase.SaveUserProfileUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

// State untuk UI
data class QuestionereUiState(
    val isLoading: Boolean = true,
    val isSaveSuccess: Boolean = false,
    val errorMessage: String? = null,
    val userProfile: UserProfile? = null // Simpan profil asli
)

@HiltViewModel
class QuestionereViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val userRepository: UserRepository,
    private val saveUserProfileUseCase: SaveUserProfileUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(QuestionereUiState())
    val uiState = _uiState.asStateFlow()

    // State untuk jawaban yang bisa diedit di UI
    val answers = mutableStateMapOf<String, String>()

    init {
        loadUserProfile()
    }

    fun onAnswerChange(question: String, answer: String) {
        answers[question] = answer
    }

    private fun loadUserProfile() {
        viewModelScope.launch {
            val uid = authRepository.getCurrentUser()?.uid
            if (uid != null) {
                val profile = userRepository.getUserProfileFromFirebase(uid)
                if (profile != null) {
                    // Gabungkan jawaban dari profil ke dalam satu map untuk UI
                    val initialAnswers = mutableMapOf<String, String>()

                    // Konversi Map<String, Boolean> ke Map<String, String>
                    profile.symptomsAndHistory.forEach { (question, answer) ->
                        initialAnswers[question] = if (answer) "Ya" else "Tidak"
                    }
                    // Tambahkan jawaban dari lifestyle
                    initialAnswers.putAll(profile.lifestyle)

                    answers.putAll(initialAnswers)
                    _uiState.update { it.copy(isLoading = false, userProfile = profile) }
                } else {
                    _uiState.update { it.copy(isLoading = false, errorMessage = "Profil tidak ditemukan.") }
                }
            } else {
                _uiState.update { it.copy(isLoading = false, errorMessage = "Pengguna tidak login.") }
            }
        }
    }

    fun saveChanges() {
        viewModelScope.launch {
            val currentProfile = _uiState.value.userProfile
            if (currentProfile != null) {
                // Buat salinan baru dari profil dengan jawaban yang diperbarui
                val updatedSymptoms = currentProfile.symptomsAndHistory.toMutableMap()
                val updatedLifestyle = currentProfile.lifestyle.toMutableMap()

                answers.forEach { (question, answer) ->
                    if (updatedSymptoms.containsKey(question)) {
                        updatedSymptoms[question] = (answer == "Ya")
                    } else if (updatedLifestyle.containsKey(question)) {
                        updatedLifestyle[question] = answer
                    }
                }

                val updatedProfile = currentProfile.copy(
                    symptomsAndHistory = updatedSymptoms,
                    lifestyle = updatedLifestyle
                )

                val result = saveUserProfileUseCase(updatedProfile)
                result.onSuccess {
                    _uiState.update { it.copy(isSaveSuccess = true) }
                }.onFailure { exception ->
                    _uiState.update { it.copy(errorMessage = exception.message) }
                }
            }
        }
    }

    // Fungsi untuk me-reset event setelah ditangani UI
    fun onSaveComplete() {
        _uiState.update { it.copy(isSaveSuccess = false) }
    }
}