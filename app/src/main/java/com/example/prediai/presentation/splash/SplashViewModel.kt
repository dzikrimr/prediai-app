package com.example.prediai.presentation.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.prediai.domain.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {

    fun getOnboardingState(): Flow<Boolean> = userRepository.getOnboardingState()
    fun getCachedUserUid(): Flow<String?> = userRepository.getCachedUserUid()

    fun refreshCache(uid: String) {
        viewModelScope.launch {
            val userProfile = userRepository.getUserProfileFromFirebase(uid)
            userProfile?.let {
                userRepository.saveUserProfileToCache(uid, it)
            }
        }
    }
}