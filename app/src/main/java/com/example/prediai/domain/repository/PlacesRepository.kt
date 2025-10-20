package com.example.prediai.domain.repository

import com.example.prediai.domain.model.NearbyPlace
import com.google.android.gms.maps.model.LatLng

interface PlacesRepository {
    suspend fun findNearbyHospitals(location: LatLng): Result<List<NearbyPlace>>
}