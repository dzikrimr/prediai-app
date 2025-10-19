package com.example.prediai.domain.repository

import com.example.prediai.domain.model.LabAnalysisRecord // <-- Import baru
import com.example.prediai.domain.model.ScanHistoryRecord
import kotlinx.coroutines.flow.Flow

interface HistoryRepository {
    suspend fun uploadImage(image: ByteArray): Result<String>
    suspend fun saveScanRecord(record: ScanHistoryRecord): Result<Unit>
    fun getScanHistory(): Flow<Result<List<ScanHistoryRecord>>>
    suspend fun getScanRecordById(id: String): Result<ScanHistoryRecord?>

    suspend fun saveLabAnalysisRecord(record: LabAnalysisRecord): Result<Unit>
    fun getLabAnalysisHistory(): Flow<Result<List<LabAnalysisRecord>>>
    suspend fun getLabAnalysisRecordById(id: String): Result<LabAnalysisRecord?> // <--- ADDED
}