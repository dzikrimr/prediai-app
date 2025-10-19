package com.example.prediai.domain.model

data class AnalysisItem(
    val id: String,
    val fileName: String,
    val date: String,
    val type: AnalysisType,
    val isStarred: Boolean
)

enum class AnalysisType {
    IMAGE, PDF
}