package com.example.prediai.presentation.auth

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.example.prediai.R
import com.example.prediai.domain.model.UserProfile
import com.example.prediai.domain.repository.AuthRepository
import com.example.prediai.domain.repository.UserRepository
import com.example.prediai.domain.usecase.SaveUserProfileUseCase
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class AuthUiState(
    val email: String = "",
    val password: String = "",
    val confirmPassword: String = "",
    val isLoading: Boolean = false,
    val authErrorMessage: String? = null,
    val questionnaireErrorMessage: String? = null,
    val isQuestionnaireSuccess: Boolean = false,

    val name: String = "",
    val birthDate: String = "",
    val height: String = "",
    val weight: String = "",
    val city: String = "",

    val step2Answers: Map<String, Boolean> = emptyMap(),
    val step3Answers: Map<String, Boolean> = emptyMap(),
    val step4Answers: Map<String, String> = emptyMap()
)

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val userRepository: UserRepository,
    private val saveUserProfileUseCase: SaveUserProfileUseCase,
    private val app: Application
) : ViewModel() {

    val googleSignInClient: GoogleSignInClient by lazy {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(app.getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        GoogleSignIn.getClient(app, gso)
    }

    private val _uiState = MutableStateFlow(AuthUiState())
    val uiState = _uiState.asStateFlow()

    // --- Sisa kode Anda tetap sama ---

    fun onEmailChange(email: String) {
        _uiState.update { it.copy(email = email) }
    }

    fun onPasswordChange(password: String) {
        _uiState.update { it.copy(password = password) }
    }

    fun onConfirmPasswordChange(confirmPassword: String) {
        _uiState.update { it.copy(confirmPassword = confirmPassword) }
    }

    fun onNameChange(name: String) {
        _uiState.update { it.copy(name = name) }
    }

    fun onBirthDateChange(birthDate: String) {
        _uiState.update { it.copy(birthDate = birthDate) }
    }

    fun onHeightChange(height: String) {
        _uiState.update { it.copy(height = height) }
    }

    fun onWeightChange(weight: String) {
        _uiState.update { it.copy(weight = weight) }
    }

    fun onCityChange(city: String) {
        _uiState.update { it.copy(city = city) }
    }

    fun onStep2Answer(question: String, answer: Boolean) {
        val newAnswers = _uiState.value.step2Answers.toMutableMap()
        newAnswers[question] = answer
        _uiState.update { it.copy(step2Answers = newAnswers) }
    }

    fun onStep3Answer(question: String, answer: Boolean) {
        val newAnswers = _uiState.value.step3Answers.toMutableMap()
        newAnswers[question] = answer
        _uiState.update { it.copy(step3Answers = newAnswers) }
    }

    fun onStep4Answer(question: String, answer: String) {
        val newAnswers = _uiState.value.step4Answers.toMutableMap()
        newAnswers[question] = answer
        _uiState.update { it.copy(step4Answers = newAnswers) }
    }

    private fun loadUserAndCache(uid: String) {
        viewModelScope.launch {
            val userProfile = userRepository.getUserProfileFromFirebase(uid)
            if (userProfile != null) {
                userRepository.saveUserProfileToCache(uid, userProfile)
            }
        }
    }

    fun login(navController: NavController) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, authErrorMessage = null) }
            val result = authRepository.login(_uiState.value.email, _uiState.value.password)
            if (result.isSuccess) {
                val uid = authRepository.getCurrentUser()?.uid
                if (uid != null) {
                    loadUserAndCache(uid)
                }
                navController.navigate("home") {
                    popUpTo("login") { inclusive = true }
                }
            } else {
                _uiState.update { it.copy(authErrorMessage = result.exceptionOrNull()?.message) }
            }
            _uiState.update { it.copy(isLoading = false) }
        }
    }

    fun register(navController: NavController) {
        val state = _uiState.value
        if (state.password != state.confirmPassword) {
            _uiState.update { it.copy(authErrorMessage = "Password tidak cocok") }
            return
        }

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, authErrorMessage = null) }
            val result = authRepository.register(state.email, state.password)
            if (result.isSuccess) {
                navController.navigate("questionnaire") {
                    popUpTo("register") { inclusive = true }
                }
            } else {
                _uiState.update { it.copy(authErrorMessage = result.exceptionOrNull()?.message) }
            }
            _uiState.update { it.copy(isLoading = false) }
        }
    }

    fun signInWithGoogle(idToken: String, navController: NavController) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, authErrorMessage = null) }
            val result = authRepository.signInWithGoogle(idToken)
            if (result.isSuccess) {
                val user = authRepository.getCurrentUser()
                if (user?.uid != null) {
                    val userProfile = userRepository.getUserProfileFromFirebase(user.uid)
                    if (userProfile != null) {
                        userRepository.saveUserProfileToCache(user.uid, userProfile)
                        navController.navigate("home") { popUpTo("login") { inclusive = true } }
                    } else {
                        val newProfile = UserProfile(name = user.displayName ?: "", city = "") // Buat profil dasar
                        userRepository.saveUserProfileToCache(user.uid, newProfile)
                        navController.navigate("questionnaire") { popUpTo("login") { inclusive = true } }
                    }
                }
            } else {
                _uiState.update { it.copy(authErrorMessage = result.exceptionOrNull()?.message) }
            }
            _uiState.update { it.copy(isLoading = false) }
        }
    }

    fun submitQuestionnaire() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, questionnaireErrorMessage = null) }
            val currentState = _uiState.value
            val uid = authRepository.getCurrentUser()?.uid

            if (uid == null) {
                _uiState.update { it.copy(isLoading = false, questionnaireErrorMessage = "Pengguna tidak ditemukan.") }
                return@launch
            }

            val userProfile = UserProfile(
                name = currentState.name,
                birthDate = currentState.birthDate,
                height = currentState.height,
                weight = currentState.weight,
                city = currentState.city,
                symptomsAndHistory = currentState.step2Answers + currentState.step3Answers,
                lifestyle = currentState.step4Answers
            )

            val result = saveUserProfileUseCase(userProfile)

            result.onSuccess {
                userRepository.saveUserProfileToCache(uid, userProfile)
                _uiState.update { it.copy(isLoading = false, isQuestionnaireSuccess = true) }
            }.onFailure { exception ->
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        questionnaireErrorMessage = exception.message ?: "Gagal menyimpan data"
                    )
                }
            }
        }
    }

    fun clearError() {
        _uiState.update { it.copy(authErrorMessage = null) }
    }
}