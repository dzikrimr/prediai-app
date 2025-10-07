// User.kt
package com.example.prediai.domain.model

data class User(
    val uid: String = "",
    val name: String = "",
    val email: String = "",
    val birthDate: String = "",
    val height: String = "",
    val weight: String = "",
    val city: String = "",
    val questionnaireStep1: Map<Int, Boolean> = emptyMap(),
    val questionnaireStep2: Map<Int, Boolean> = emptyMap(),
    val questionnaireStep3: Map<Int, String> = emptyMap()
)