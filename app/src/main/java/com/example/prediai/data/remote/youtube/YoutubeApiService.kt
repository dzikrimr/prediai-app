package com.example.prediai.data.remote.youtube

import com.example.prediai.BuildConfig
import okhttp3.ResponseBody
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import retrofit2.http.Streaming

interface YoutubeApiService {

    @GET("search")
    suspend fun searchVideos(
        @Query("q") query: String,
        @Query("part") part: String = "snippet",
        @Query("type") type: String = "video",
        @Query("maxResults") maxResults: Int = 10,
        @Query("key") apiKey: String = BuildConfig.YOUTUBE_API_KEY
    ): YoutubeSearchResponse

    // --- TAMBAHKAN FUNGSI BARU DI BAWAH INI ---
    @GET("videos")
    suspend fun getVideoDetails(
        @Query("part") part: String = "snippet,statistics", // Minta snippet & statistik
        @Query("id") videoId: String,
        @Query("key") apiKey: String = BuildConfig.YOUTUBE_API_KEY
    ): YoutubeVideoDetailsResponse // <-- Kita akan buat data class ini di langkah 2
    // --- AKHIR BAGIAN BARU ---

    @GET("captions")
    suspend fun listCaptions(
        @Query("videoId") videoId: String,
        @Query("part") part: String = "snippet",
        @Query("key") apiKey: String = BuildConfig.YOUTUBE_API_KEY
    ): YoutubeCaptionsListResponse

    @GET("captions/{id}")
    @Streaming
    suspend fun downloadCaption(
        @Path("id") captionId: String,
        @Query("tfmt") format: String = "srt",
        @Query("key") apiKey: String = BuildConfig.YOUTUBE_API_KEY
    ): ResponseBody
}