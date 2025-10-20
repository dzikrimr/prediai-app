package com.example.prediai.data.remote.places

import com.google.gson.annotations.SerializedName

data class GpsCoordinates(
    // Kunci 'latitude' dan 'longitude' sesuai dengan JSON
    val latitude: Double?,
    val longitude: Double?
)
