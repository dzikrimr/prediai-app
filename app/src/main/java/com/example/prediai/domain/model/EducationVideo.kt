package com.example.prediai.domain.model

// Pindahkan object kategori ke sini agar bisa diakses global
object VideoCategories {
    const val SEMUA = "Semua"
    const val NUTRISI = "Nutrisi"
    const val OLAHRAGA = "Olahraga"
    const val PERAWATAN = "Perawatan"
    const val GEJALA = "Gejala"
}

// Ini akan menjadi SATU-SATUNYA model data video kita
data class EducationVideo(
    val id: String,
    val title: String,
    val source: String,
    val views: String,
    val imageUrl: String,
    val youtubeVideoId: String,
    val category: String,
    val description: String
)