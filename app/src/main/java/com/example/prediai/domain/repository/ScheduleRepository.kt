package com.example.prediai.domain.repository

import com.example.prediai.domain.model.ScheduleItem
import kotlinx.coroutines.flow.Flow

interface ScheduleRepository {

    // Mengambil semua jadwal untuk user yang sedang login
    fun getAllSchedules(): Flow<List<ScheduleItem>>

    // Menambah jadwal baru untuk user yang sedang login
    suspend fun addSchedule(scheduleItem: ScheduleItem)

    // (Opsional) Fungsi lain yang mungkin kamu butuh
    // suspend fun updateSchedule(scheduleItem: ScheduleItem)
    // suspend fun deleteSchedule(scheduleId: String)

    suspend fun deleteSchedule(scheduleId: String)

    suspend fun dismissScheduleNotification(scheduleId: String)
}