package com.example.prediai.domain.model

data class NearbyPlace(
    val id: String,
    val name: String,
    val address: String,
    val distanceInMeters: Double?
)