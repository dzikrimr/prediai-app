package com.example.prediai.data.repository

import com.example.prediai.cache.UserData
import com.example.prediai.data.datastore.UserPreferencesManager
import com.example.prediai.domain.model.UserProfile
import com.example.prediai.domain.repository.UserRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val prefsManager: UserPreferencesManager,
    private val database: FirebaseDatabase,
    private val auth: FirebaseAuth
) : UserRepository {

    override suspend fun getUserProfileFromFirebase(uid: String): UserProfile? {
        return try {
            val snapshot = database.getReference("users").child(uid).get().await()
            snapshot.getValue(UserProfile::class.java)
        } catch (e: Exception) {
            null
        }
    }

    override suspend fun saveUserProfileToCache(uid: String, profile: UserProfile) {
        prefsManager.saveUserProfile(uid, profile)
        UserData.set(uid, profile)
    }

    override fun getCachedUserProfile(): Flow<UserProfile> {
        return prefsManager.userProfileFlow
    }

    override fun getCachedUserUid(): Flow<String?> {
        return prefsManager.userUidFlow
    }

    override suspend fun setOnboardingCompleted(isCompleted: Boolean) {
        prefsManager.setOnboardingCompleted(isCompleted)
    }

    override fun getOnboardingState(): Flow<Boolean> {
        return prefsManager.isOnboardingCompleted
    }

    override suspend fun logout() {
        auth.signOut()
        prefsManager.clear()
        UserData.clear()
    }

    override suspend fun saveUserProfileToFirebase(uid: String, profile: UserProfile): Result<Unit> {
        return try {
            database.getReference("users").child(uid).setValue(profile).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override fun getCurrentUser(): FirebaseUser? {
        return auth.currentUser
    }
}