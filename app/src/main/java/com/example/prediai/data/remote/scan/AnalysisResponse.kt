package com.example.prediai.data.remote.scan

import com.google.gson.annotations.SerializedName

data class AnalysisResponse(
    @SerializedName("risk_level")
    val riskLevel: String,

    @SerializedName("risk_percentage")
    val riskPercentage: Float,

    @SerializedName("risk_factors_identified")
    val riskFactors: List<String>,

    // Properti lain bisa ditambahkan jika ada di response
    // @SerializedName("lidah_result")
    // val lidahResult: PredictionResult?,
    //
    // @SerializedName("kuku_result")
    // val kukuResult: PredictionResult?
)