package com.example.prediai.domain.model

enum class ScheduleType {
    CEK_GULA,
    KONSULTASI,
    OLAHRAGA,
    MINUM_OBAT,
    SKRINING_AI,
    JADWAL_MAKAN,
    CEK_TENSI
}

data class ScheduleItem(
    val id: String = "", // ID unik, bisa dibuat dari push() Firebase
    val type: ScheduleType = ScheduleType.CEK_GULA,
    val description: String = "",
    val date: String = "", // Simpan sebagai String (misal "yyyy-MM-dd")
    val time: String = "", // Simpan sebagai String (misal "HH:mm")
)