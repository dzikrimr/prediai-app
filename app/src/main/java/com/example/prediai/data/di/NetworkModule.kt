package com.example.prediai.data.di

import com.example.prediai.data.remote.youtube.YoutubeApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
// --- IMPORT BARU ---
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    private const val BASE_URL = "https://www.googleapis.com/youtube/v3/"

    // --- TAMBAHKAN PROVIDE UNTUK OKHTTPCLIENT ---
    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient {
        val logging = HttpLoggingInterceptor()
        // Level BODY akan mencetak URL, Header, dan Body request/response
        // (HATI-HATI JANGAN PAKAI DI RELEASE BUILD KARENA MENCETAK API KEY!)
        logging.setLevel(HttpLoggingInterceptor.Level.BODY)
        return OkHttpClient.Builder()
            .addInterceptor(logging) // Tambahkan interceptor
            .build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit { // Terima OkHttpClient
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient) // <-- Gunakan OkHttpClient yang sudah ada logging
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun provideYoutubeApiService(retrofit: Retrofit): YoutubeApiService {
        return retrofit.create(YoutubeApiService::class.java)
    }
}