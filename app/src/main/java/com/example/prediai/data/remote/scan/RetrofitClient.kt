package com.example.prediai.data.remote.scan

import com.example.prediai.data.remote.places.PlacesSerpApiService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import kotlin.jvm.java

object RetrofitClient {
    // URL untuk deteksi YOLO
    private const val YOLO_BASE_URL = "https://yolo-tonguenail-service-production.up.railway.app/"
    private const val ANALYSIS_BASE_URL = "https://nailtonguediabetdetection-production.up.railway.app/"
    private const val SERPAPI_BASE_URL = "https://googlemapsapi-serpapi-production.up.railway.app/"

    val detectionInstance: DetectionApiService by lazy {
        Retrofit.Builder()
            .baseUrl(YOLO_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(DetectionApiService::class.java)
    }

    val analysisInstance: AnalysisApiService by lazy {
        Retrofit.Builder()
            .baseUrl(ANALYSIS_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(AnalysisApiService::class.java)
    }

    val placesInstance: PlacesSerpApiService by lazy {
        Retrofit.Builder()
            .baseUrl(SERPAPI_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(PlacesSerpApiService::class.java)
    }
}