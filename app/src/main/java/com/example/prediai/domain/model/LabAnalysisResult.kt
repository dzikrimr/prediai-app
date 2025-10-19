package com.example.prediai.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class LabAnalysisResult(
    val summary: String = "Analisis belum tersedia.",
    val key_findings: List<Finding> = emptyList(),
    val next_steps: String = "Silakan coba lagi atau unggah dokumen lain."
)

@Serializable
data class Finding(
    val parameter: String = "",
    val value: String = "",
    val normal_range: String = "",
    val status: String = "",
    val explanation: String = ""
)