package com.example.prediai.data.repository

import com.example.prediai.domain.model.ScheduleItem
import com.example.prediai.domain.repository.AuthRepository // <-- Ini kuncinya
import com.example.prediai.domain.repository.ScheduleRepository
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class ScheduleRepositoryImpl @Inject constructor(
    private val database: FirebaseDatabase,
    private val authRepository: AuthRepository // <-- Kita inject AuthRepository
) : ScheduleRepository {

    /**
     * Helper untuk mendapatkan UID user.
     * Ini sekarang sesuai dengan AuthRepository-mu.
     */
    private fun getCurrentUserId(): String? {
        // Kita panggil fungsi dari AuthRepository
        // lalu ambil properti .uid dari FirebaseUser
        return authRepository.getCurrentUser()?.uid
    }

    override fun getAllSchedules(): Flow<List<ScheduleItem>> = callbackFlow {
        val userId = getCurrentUserId() // <-- Pemanggilan ini sekarang 100% benar
        if (userId == null) {
            close(IllegalStateException("User tidak login"))
            return@callbackFlow
        }

        // Struktur RTDB: users/{userId}/schedules
        val ref = database.reference.child("users").child(userId).child("schedules")

        val listener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val schedules = snapshot.children.mapNotNull {
                    // Pastikan model ScheduleItem di domain/model cocok
                    it.getValue(ScheduleItem::class.java)
                }
                trySend(schedules) // Kirim data ke Flow
            }

            override fun onCancelled(error: DatabaseError) {
                close(error.toException())
            }
        }
        ref.addValueEventListener(listener)

        // Hapus listener saat flow ditutup
        awaitClose { ref.removeEventListener(listener) }
    }

    override suspend fun addSchedule(scheduleItem: ScheduleItem) {
        val userId = getCurrentUserId() // <-- Pemanggilan ini juga benar
            ?: throw IllegalStateException("User tidak login")

        val ref = database.reference.child("users").child(userId).child("schedules")

        // Buat ID unik baru menggunakan push()
        val newScheduleRef = ref.push()
        val newId = newScheduleRef.key ?: throw IllegalStateException("Gagal membuat ID baru")

        // Simpan data dengan ID baru yang sudah di-copy
        newScheduleRef.setValue(scheduleItem.copy(id = newId)).await()
    }

    override suspend fun deleteSchedule(scheduleId: String) {
        val userId = getCurrentUserId()
            ?: throw IllegalStateException("User tidak login")

        // Cek jika ID tidak kosong sebelum mencoba menghapus
        if (scheduleId.isBlank()) {
            throw IllegalArgumentException("Schedule ID tidak boleh kosong")
        }

        // Path ke item spesifik: users/{userId}/schedules/{scheduleId}
        database.reference
            .child("users")
            .child(userId)
            .child("schedules")
            .child(scheduleId) // <-- Target ID yang akan dihapus
            .removeValue() // <-- Hapus data
            .await() // Tunggu sampai selesai
    }

    override suspend fun dismissScheduleNotification(scheduleId: String) {
        val userId = getCurrentUserId()
            ?: throw IllegalStateException("User tidak login")

        if (scheduleId.isBlank()) {
            throw IllegalArgumentException("Schedule ID tidak boleh kosong")
        }

        // Path ke field 'isDismissed' dari item spesifik
        database.reference
            .child("users")
            .child(userId)
            .child("schedules")
            .child(scheduleId)
            .child("isDismissed") // <-- Target field yang di-update
            .setValue(true)       // <-- Set nilainya jadi true
            .await()
    }
}