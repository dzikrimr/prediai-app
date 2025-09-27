package com.example.prediai.domain.repository

import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LoginRepository @Inject constructor() {

    // Fungsi login dummy
    fun login(username: String, password: String): Boolean {
        // Untuk sementara, return true kalau username & password tidak kosong
        return username.isNotEmpty() && password.isNotEmpty()
    }
}
