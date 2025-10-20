package com.example.prediai

import android.app.Application
import com.cloudinary.android.MediaManager
import com.example.prediai.config.CloudinaryConfig
import com.google.android.libraries.places.api.Places
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class App : Application() {
    override fun onCreate() {
        super.onCreate()

        // Langsung masukkan API key Anda di sini (untuk testing)
        val mapsApiKey = "AIzaSyCOLyWY92S56k5FVSWlXv0asoVwXKFKq4g"

        // Lakukan inisialisasi
        if (!Places.isInitialized()) {
            // --- GANTI METODE INITIALIZE DENGAN YANG BARU ---
            Places.initializeWithNewPlacesApiEnabled(applicationContext, mapsApiKey)
        }

        // Konfigurasi untuk Cloudinary (tidak berubah)
        val config = mapOf(
            "cloud_name" to CloudinaryConfig.CLOUD_NAME,
            "api_key" to CloudinaryConfig.API_KEY,
            "api_secret" to CloudinaryConfig.API_SECRET
        )

        MediaManager.init(this, config)
    }
}