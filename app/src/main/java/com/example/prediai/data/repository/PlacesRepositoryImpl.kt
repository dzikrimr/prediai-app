package com.example.prediai.data.repository

import android.util.Log
import com.example.prediai.data.remote.places.PlacesSerpApiService
import com.example.prediai.data.remote.scan.RetrofitClient
import com.example.prediai.domain.model.NearbyPlace
import com.example.prediai.domain.repository.PlacesRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PlacesRepositoryImpl @Inject constructor() : PlacesRepository {

    private val placesSerpApiService: PlacesSerpApiService = RetrofitClient.placesInstance

    override suspend fun findNearbyHospitalsByCity(city: String): Result<List<NearbyPlace>> {
        val apiQueryString = "rumah sakit klinik di $city"
        Log.d("SerpApi", "Mencari '$apiQueryString' di backend Railway...")

        return try {
            val response = placesSerpApiService.searchLocalPlaces(query = apiQueryString)

            if (response.error != null) {
                return Result.failure(Exception("API Error: ${response.error}"))
            }

            val nearbyPlaces = response.localResults?.mapNotNull { result ->
                if (result.title != null && result.address != null) {
                    NearbyPlace(
                        id = result.link ?: result.title,
                        name = result.title,
                        address = result.address,
                        distanceInMeters = null
                    )
                } else null
            } ?: emptyList()

            Log.d("SerpApi", "Ditemukan ${nearbyPlaces.size} tempat.")
            Result.success(nearbyPlaces)

        } catch (e: Exception) {
            Log.e("SerpApi", "Gagal menghubungi SerpApi backend", e)
            Result.failure(e)
        }
    }
}
