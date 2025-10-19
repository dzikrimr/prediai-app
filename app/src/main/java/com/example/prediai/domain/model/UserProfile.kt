package com.example.prediai.domain.model

data class UserProfile(
    val name: String = "",
    val birthDate: String = "",
    val height: String = "",
    val weight: String = "",
    val city: String = "",
    val symptomsAndHistory: Map<String, Boolean> = emptyMap(),
    val lifestyle: Map<String, String> = emptyMap(),
    val profileImageUrl: String? = null
)