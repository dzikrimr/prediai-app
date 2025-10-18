package com.example.prediai.data.repository

import com.cloudinary.android.MediaManager
import com.cloudinary.android.callback.ErrorInfo
import com.cloudinary.android.callback.UploadCallback
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

    override suspend fun uploadImage(image: ByteArray): Result<String> {
        return suspendCancellableCoroutine { continuation ->
            // Menggunakan "unsigned" upload dengan preset yang Anda berikan.
            // Ini lebih aman karena tidak memerlukan API Secret di sisi klien.
            val requestId = MediaManager.get().upload(image)
                .unsigned("predii") // <-- Menggunakan preset Anda
                .callback(object : UploadCallback {
                    override fun onStart(requestId: String) {
                        // Tidak perlu melakukan apa-apa di sini
                    }

                    override fun onProgress(requestId: String, bytes: Long, totalBytes: Long) {
                        // Bisa digunakan untuk menampilkan progress bar jika perlu
                    }

                    override fun onSuccess(requestId: String, resultData: Map<*, *>) {
                        val url = resultData["secure_url"] as? String
                        if (url != null) {
                            if (continuation.isActive) {
                                continuation.resume(Result.success(url))
                            }
                        } else {
                            if (continuation.isActive) {
                                continuation.resume(Result.failure(Exception("URL tidak ditemukan dari hasil upload.")))
                            }
                        }
                    }

                    override fun onError(requestId: String, error: ErrorInfo) {
                        if (continuation.isActive) {
                            continuation.resume(Result.failure(Exception("Cloudinary Error: ${error.description}")))
                        }
                    }

                    override fun onReschedule(requestId: String, error: ErrorInfo) {
                        // Tidak perlu melakukan apa-apa di sini
                    }
                }).dispatch()

            // Jika coroutine dibatalkan, batalkan juga request upload
            continuation.invokeOnCancellation {
                MediaManager.get().cancelRequest(requestId)
            }
        }
    }

    // Fungsi saveScanRecord dan getScanHistory tidak berubah
    override suspend fun saveScanRecord(record: ScanHistoryRecord): Result<Unit> {
        return try {
            val uid = authRepository.getCurrentUser()?.uid
                ?: throw Exception("Pengguna tidak login")

            val documentId = UUID.randomUUID().toString()
            val recordToSave = record.copy(id = documentId, userId = uid)

            firestore.collection("scan_history")
                .document(documentId)
                .set(recordToSave)
                .await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override fun getScanHistory(): Flow<Result<List<ScanHistoryRecord>>> = callbackFlow {
        val uid = authRepository.getCurrentUser()?.uid
        if (uid == null) {
            trySend(Result.failure(Exception("Pengguna tidak login")))
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
                    val records = snapshot.toObjects(ScanHistoryRecord::class.java)
                    trySend(Result.success(records))
                }
            }

        awaitClose { listener.remove() }
    }

    // Pastikan Anda juga menambahkan implementasi getScanRecordById dari langkah sebelumnya
    override suspend fun getScanRecordById(id: String): Result<ScanHistoryRecord?> {
        return try {
            val snapshot = firestore.collection("scan_history").document(id).get().await()
            val record = snapshot.toObject(ScanHistoryRecord::class.java)
            Result.success(record)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}