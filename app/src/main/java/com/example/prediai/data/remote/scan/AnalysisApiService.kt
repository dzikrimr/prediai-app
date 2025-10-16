package com.example.prediai.data.remote.scan

import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface AnalysisApiService {
    @Multipart
    @POST("/predict")
    suspend fun analyzeImages(
        @Part lidahImage: MultipartBody.Part,
        @Part kukuImage: MultipartBody.Part
    ): Response<AnalysisResponse>
}