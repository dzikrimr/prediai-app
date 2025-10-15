package com.example.prediai.data.remote.scan

import com.google.gson.annotations.SerializedName

data class DetectionResponse(
    @SerializedName("detection")
    val detection: DetectionResult?,
    @SerializedName("message")
    val message: String
)

data class DetectionResult(
    @SerializedName("class_name")
    val className: String,
    @SerializedName("confidence")
    val confidence: Float,
    @SerializedName("box_normalized")
    val boxNormalized: List<Float>
)