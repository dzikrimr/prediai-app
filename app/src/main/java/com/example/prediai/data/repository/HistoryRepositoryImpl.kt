package com.example.prediai.data.repository

import com.cloudinary.android.MediaManager
import com.cloudinary.android.callback.ErrorInfo
import com.cloudinary.android.callback.UploadCallback
import com.example.prediai.domain.model.LabAnalysisRecord
import com.example.prediai.domain.model.ScanHistoryRecord
import com.example.prediai.domain.repository.AuthRepository
import com.example.prediai.domain.repository.HistoryRepository
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.tasks.await
import java.util.UUID
import javax.inject.Inject
import kotlin.coroutines.resume

class HistoryRepositoryImpl @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val authRepository: AuthRepository
) : HistoryRepository {

    // --- FUNGSI UNTUK UPLOAD GAMBAR (Digunakan bersama) ---

    override suspend fun uploadImage(image: ByteArray): Result<String> {
        return suspendCancellableCoroutine { continuation ->
            val requestId = MediaManager.get().upload(image)
                .unsigned("predii")
                .callback(object : UploadCallback {
                    override fun onSuccess(requestId: String, resultData: Map<*, *>) {
                        val url = resultData["secure_url"] as? String
                        if (url != null && continuation.isActive) {
                            continuation.resume(Result.success(url))
                        } else if (continuation.isActive) {
                            continuation.resume(Result.failure(Exception("URL tidak ditemukan dari hasil upload.")))
                        }
                    }

                    override fun onError(requestId: String, error: ErrorInfo) {
                        if (continuation.isActive) {
                            continuation.resume(Result.failure(Exception("Cloudinary Error: ${error.description}")))
                        }
                    }

                    override fun onStart(requestId: String) {}
                    override fun onProgress(requestId: String, bytes: Long, totalBytes: Long) {}
                    override fun onReschedule(requestId: String, error: ErrorInfo) {}
                }).dispatch()

            continuation.invokeOnCancellation {
                MediaManager.get().cancelRequest(requestId)
            }
        }
    }

    // --- FUNGSI-FUNGSI UNTUK RIWAYAT SCAN (YANG SUDAH ADA) ---

    override suspend fun saveScanRecord(record: ScanHistoryRecord): Result<Unit> {
        return try {
            val uid = authRepository.getCurrentUser()?.uid ?: throw Exception("Pengguna tidak login")
            val documentId = UUID.randomUUID().toString()
            val recordToSave = record.copy(id = documentId, userId = uid)

            firestore.collection("scan_history").document(documentId).set(recordToSave).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override fun getScanHistory(): Flow<Result<List<ScanHistoryRecord>>> = callbackFlow {
        val uid = authRepository.getCurrentUser()?.uid
        if (uid == null) {
            trySend(Result.failure(Exception("Pengguna tidak login"))).isSuccess
            close()
            return@callbackFlow
        }
        val listener = firestore.collection("scan_history")
            .whereEqualTo("userId", uid)
            .orderBy("timestamp", Query.Direction.DESCENDING)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    trySend(Result.failure(error))
                    return@addSnapshotListener
                }
                if (snapshot != null) {
                    trySend(Result.success(snapshot.toObjects(ScanHistoryRecord::class.java)))
                }
            }
        awaitClose { listener.remove() }
    }

    override suspend fun getScanRecordById(id: String): Result<ScanHistoryRecord?> {
        return try {
            val snapshot = firestore.collection("scan_history").document(id).get().await()
            Result.success(snapshot.toObject(ScanHistoryRecord::class.java))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // --- FUNGSI-FUNGSI BARU UNTUK RIWAYAT ANALISIS LAB ---

    override suspend fun saveLabAnalysisRecord(record: LabAnalysisRecord): Result<Unit> {
        return try {
            val uid = authRepository.getCurrentUser()?.uid ?: throw Exception("Pengguna tidak login")
            val documentId = UUID.randomUUID().toString()
            val recordToSave = record.copy(id = documentId, userId = uid)

            firestore.collection("lab_analysis_history").document(documentId).set(recordToSave).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override fun getLabAnalysisHistory(): Flow<Result<List<LabAnalysisRecord>>> = callbackFlow {
        val uid = authRepository.getCurrentUser()?.uid
        if (uid == null) {
            trySend(Result.failure(Exception("Pengguna tidak login"))).isSuccess
            close()
            return@callbackFlow
        }
        val listener = firestore.collection("lab_analysis_history")
            .whereEqualTo("userId", uid)
            .orderBy("timestamp", Query.Direction.DESCENDING)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    trySend(Result.failure(error))
                    return@addSnapshotListener
                }
                if (snapshot != null) {
                    trySend(Result.success(snapshot.toObjects(LabAnalysisRecord::class.java)))
                }
            }
        awaitClose { listener.remove() }
    }
    override suspend fun getLabAnalysisRecordById(id: String): Result<LabAnalysisRecord?> {
        return try {
            val snapshot = firestore.collection("lab_analysis_history").document(id).get().await()
            Result.success(snapshot.toObject(LabAnalysisRecord::class.java))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}