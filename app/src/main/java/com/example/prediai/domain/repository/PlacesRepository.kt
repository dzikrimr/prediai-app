package com.example.prediai.domain.repository

import com.example.prediai.domain.model.NearbyPlace
import com.google.android.gms.maps.model.LatLng // Tetap pertahankan jika ada logic lain

interface PlacesRepository {
    suspend fun findNearbyHospitalsByCity(city: String): Result<List<NearbyPlace>>

}