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
    @JvmField val id: String = "",
    @JvmField val type: ScheduleType = ScheduleType.CEK_GULA,
    @JvmField val description: String = "",
    @JvmField val date: String = "",
    @JvmField val time: String = "",
    @JvmField val isDismissed: Boolean = false
)