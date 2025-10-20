package com.example.prediai.data.repository

import android.util.Log
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
        val userId = getCurrentUserId()
        if (userId == null) {
            trySend(emptyList())
            close()
            return@callbackFlow
        }

        val ref = database.reference.child("users").child(userId).child("schedules")

        val listener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val schedules = snapshot.children.mapNotNull {
                    it.getValue(ScheduleItem::class.java)
                }
                trySend(schedules)
            }

            override fun onCancelled(error: DatabaseError) {
                // Log the error instead of throwing it
                Log.e("ScheduleRepository", "Failed to fetch schedules: ${error.message}")
                trySend(emptyList())
            }
        }
        ref.addValueEventListener(listener)

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
}