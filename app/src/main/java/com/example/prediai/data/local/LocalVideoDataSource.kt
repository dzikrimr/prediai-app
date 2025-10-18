package com.example.prediai.data.local

import com.example.prediai.domain.model.EducationVideo
import com.example.prediai.domain.model.VideoCategories

// Pindahkan daftar video hardcoded ke sini
object LocalVideoDataSource {

    val allVideosList = listOf(
        EducationVideo("1", "JEC Podcast | Eps 010: Edukasi Lebih Jauh Tentang Diabetes", "JEC Eye Hospitals & Clinics", "85rb x ditonton", "https://img.youtube.com/vi/RVE9TBV_oDA/0.jpg", "RVE9TBV_oDA", VideoCategories.GEJALA),
        EducationVideo("2", "Kenali gejala diabetesmu melalui tangan", "Ilmu Dokter", "91rb x ditonton", "https://picsum.photos/id/20/200", "9-e_v14-a-Q", VideoCategories.GEJALA),
        EducationVideo("3", "Olahraga ringan 15 menit untuk diabetes", "Info Sehat", "120rb x ditonton", "https://picsum.photos/id/30/200", "S5-zKft4-nI", VideoCategories.OLAHRAGA),
        EducationVideo("4", "Cara merawat luka diabetes agar cepat kering", "Apotek Kita", "50rb x ditonton", "https://picsum.photos/id/40/200", "9-e_v14-a-Q", VideoCategories.PERAWATAN),
        EducationVideo("5", "Mitos dan Fakta Makanan Penderita Diabetes", "Gula Sehat", "200rb x ditonton", "https://picsum.photos/id/50/200", "S5-zKft4-nI", VideoCategories.NUTRISI)
        // Tambahkan video lainnya di sini jika perlu
    )
}