package com.example.prediai.domain.usecase

import com.example.prediai.domain.model.UserProfile
import com.example.prediai.domain.repository.AuthRepository
import com.example.prediai.domain.repository.UserRepository
import javax.inject.Inject

class SaveUserProfileUseCase @Inject constructor(
    private val authRepository: AuthRepository,
    private val userRepository: UserRepository
) {
    suspend operator fun invoke(userProfile: UserProfile): Result<Unit> {
        val userId = authRepository.getCurrentUser()?.uid
        return if (userId != null) {
            // PANGGIL FUNGSI YANG BENAR DARI REPOSITORY
            userRepository.saveUserProfileToFirebase(userId, userProfile)
        } else {
            Result.failure(Exception("Pengguna tidak ditemukan"))
        }
    }
}