package com.example.prediai.domain.repository

import com.example.prediai.domain.model.UserProfile
import kotlinx.coroutines.flow.Flow

interface UserRepository {
    suspend fun getUserProfileFromFirebase(uid: String): UserProfile?
    suspend fun saveUserProfileToCache(uid: String, profile: UserProfile)
    fun getCachedUserProfile(): Flow<UserProfile>
    fun getCachedUserUid(): Flow<String?>
    suspend fun setOnboardingCompleted(isCompleted: Boolean)
    fun getOnboardingState(): Flow<Boolean>
    suspend fun logout()
    suspend fun saveUserProfileToFirebase(uid: String, profile: UserProfile): Result<Unit>
}