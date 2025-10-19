package com.example.prediai.domain.repository

import com.google.firebase.auth.FirebaseUser

interface AuthRepository {
    suspend fun login(email: String, password: String): Result<Unit>
    suspend fun register(email: String, password: String): Result<Unit>
    suspend fun signInWithGoogle(idToken: String): Result<Unit>
    suspend fun sendPasswordResetEmail(email: String): Result<Unit>
    fun getCurrentUser(): FirebaseUser?
}
