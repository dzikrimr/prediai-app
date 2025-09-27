package com.example.prediai.domain.usecase

import com.example.prediai.domain.repository.LoginRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LoginUseCase @Inject constructor(
    private val repository: LoginRepository
) {
    fun execute(username: String, password: String): Boolean {
        return repository.login(username, password)
    }
}
