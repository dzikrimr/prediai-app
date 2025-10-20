package com.example.prediai.data.repository

import android.Manifest
import android.content.Context
import android.location.Location
import android.util.Log
import androidx.annotation.RequiresPermission
import com.example.prediai.domain.model.NearbyPlace
import com.example.prediai.domain.repository.PlacesRepository
import com.google.android.gms.maps.model.LatLng
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.net.FindCurrentPlaceRequest
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PlacesRepositoryImpl @Inject constructor(
    @ApplicationContext private val context: Context
) : PlacesRepository {

    private val placesClient = Places.createClient(context)

    /**
     * Cari rumah sakit, klinik, atau dokter di sekitar lokasi pengguna
     */
    @RequiresPermission(allOf = [Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION])
    override suspend fun findNearbyHospitals(location: LatLng): Result<List<NearbyPlace>> {
        Log.d("PlacesAPI", "Mencari tempat di sekitar: Lat=${location.latitude}, Lng=${location.longitude}")

        // Field yang ingin diambil dari setiap Place
        val placeFields = listOf(
            Place.Field.ID,
            Place.Field.NAME,
            Place.Field.ADDRESS,
            Place.Field.LAT_LNG,
            Place.Field.TYPES
        )

        val request = FindCurrentPlaceRequest.newInstance(placeFields)

        return try {
            // Ambil data tempat dari Places API
            val response = placesClient.findCurrentPlace(request).await()
            Log.d("PlacesAPI", "Dapat ${response.placeLikelihoods.size} kemungkinan tempat")

            val nearbyHospitals = response.placeLikelihoods
                .map { it.place }
                .filter { place ->
                    val types = place.types ?: emptyList()
                    types.any { type ->
                        type == Place.Type.HOSPITAL ||
                                type == Place.Type.DOCTOR ||
                                type == Place.Type.HEALTH ||
                                type == Place.Type.PHARMACY ||
                                type == Place.Type.DENTIST
                    }
                }
                .map { place ->
                    val distanceResults = FloatArray(1)
                    place.latLng?.let { latLng ->
                        Location.distanceBetween(
                            location.latitude, location.longitude,
                            latLng.latitude, latLng.longitude,
                            distanceResults
                        )
                    }

                    NearbyPlace(
                        id = place.id ?: "",
                        name = place.name ?: "Nama tidak tersedia",
                        address = place.address ?: "Alamat tidak tersedia",
                        distanceInMeters = place.latLng?.let { distanceResults[0].toDouble() }
                    )
                }
                .sortedBy { it.distanceInMeters }

            Log.d("PlacesAPI", "Ditemukan ${nearbyHospitals.size} tempat relevan")
            Result.success(nearbyHospitals)

        } catch (e: Exception) {
            Log.e("PlacesAPI", "Gagal mencari tempat", e)
            Result.failure(e)
        }
    }
}
