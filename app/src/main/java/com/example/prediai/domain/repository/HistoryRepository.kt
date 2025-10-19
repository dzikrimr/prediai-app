package com.example.prediai.domain.repository

import com.example.prediai.domain.model.ScanHistoryRecord
import kotlinx.coroutines.flow.Flow

interface HistoryRepository {
    // Ganti ByteArray dengan tipe file/path yang sesuai untuk Cloudinary SDK Anda
    suspend fun uploadImage(image: ByteArray): Result<String>

    suspend fun saveScanRecord(record: ScanHistoryRecord): Result<Unit>

    fun getScanHistory(): Flow<Result<List<ScanHistoryRecord>>>

    suspend fun getScanRecordById(id: String): Result<ScanHistoryRecord?>
}