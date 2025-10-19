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
        @Query("key") apiKey: String = BuildConfig.YOUTUBE_API_KEY // Ambil dari BuildConfig
    ): YoutubeSearchResponse

    @GET("captions")
    suspend fun listCaptions(
        @Query("videoId") videoId: String,
        @Query("part") part: String = "snippet",
        @Query("key") apiKey: String = BuildConfig.YOUTUBE_API_KEY
    ): YoutubeCaptionsListResponse

    @GET("captions/{id}") // Path dinamis untuk ID caption
    @Streaming // Penting untuk download file
    suspend fun downloadCaption(
        @Path("id") captionId: String,
        @Query("tfmt") format: String = "srt", // Minta format SRT (atau vtt)
        @Query("key") apiKey: String = BuildConfig.YOUTUBE_API_KEY
    ): ResponseBody
}