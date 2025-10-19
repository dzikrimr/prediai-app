package com.example.prediai.domain.model

import com.google.firebase.firestore.ServerTimestamp
import java.util.Date

data class LabAnalysisRecord(
    val id: String = "",
    val userId: String = "",
    @ServerTimestamp
    val timestamp: Date? = null,
    val fileName: String = "",
    val fileUrl: String = "",
    val analysisResult: LabAnalysisResult? = null
)