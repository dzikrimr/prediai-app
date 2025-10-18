package com.example.prediai

import android.app.Application
import com.cloudinary.android.MediaManager
import com.example.prediai.config.CloudinaryConfig
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class App : Application() {
    override fun onCreate() {
        super.onCreate()

        // Konfigurasi untuk Cloudinary
        val config = mapOf(
            "cloud_name" to CloudinaryConfig.CLOUD_NAME,
            "api_key" to CloudinaryConfig.API_KEY,
            "api_secret" to CloudinaryConfig.API_SECRET
        )

        MediaManager.init(this, config)
    }
}