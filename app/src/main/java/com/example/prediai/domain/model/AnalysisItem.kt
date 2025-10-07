package com.example.prediai.domain.model

data class AnalysisItem(
    val fileName: String,
    val date: String,
    val type: AnalysisType,
    val isStarred: Boolean
)

enum class AnalysisType {
    IMAGE, PDF
}