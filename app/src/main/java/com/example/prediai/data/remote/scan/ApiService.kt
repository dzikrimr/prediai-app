package com.example.prediai.data.remote.scan

import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface ApiService {
    @Multipart
    @POST("/detect/kuku")
    suspend fun detectKuku(@Part image: MultipartBody.Part): Response<DetectionResponse>

    @Multipart
    @POST("/detect/lidah")
    suspend fun detectLidah(@Part image: MultipartBody.Part): Response<DetectionResponse>
}