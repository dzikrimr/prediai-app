package com.example.prediai.data.remote.places

import com.google.gson.annotations.SerializedName
import retrofit2.http.GET
import retrofit2.http.Query

data class SerpApiResponse(
    @SerializedName("local_results")
    val localResults: List<LocalResult>?,
    val error: String?
)

data class LocalResult(
    val title: String?,
    val address: String?,
    val link: String?,
    val rating: Double?,
    val reviews: Int?,
    val type: String?,
    val gps_coordinates: GpsCoordinates?
)

interface PlacesSerpApiService {
    @GET("search-local")
    suspend fun searchLocalPlaces(
        @Query("query") query: String,
    ): SerpApiResponse
}
