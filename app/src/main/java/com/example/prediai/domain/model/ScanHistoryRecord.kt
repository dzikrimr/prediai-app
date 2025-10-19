package com.example.prediai.domain.model

import com.google.firebase.firestore.ServerTimestamp
import java.util.Date

data class ScanHistoryRecord(
    val id: String = "", // Document ID dari Firestore
    val userId: String = "",
    @ServerTimestamp
    val timestamp: Date? = null,
    val riskLevel: String = "",
    val riskPercentage: Float = 0f,
    val riskFactors: List<String> = emptyList(),
    val nailImageUrl: String = "",
    val tongueImageUrl: String = ""
)