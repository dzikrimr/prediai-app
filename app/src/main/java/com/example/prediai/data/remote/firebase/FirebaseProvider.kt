package com.example.prediai.data.remote.firebase

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

object FirebaseProvider {
    val database: FirebaseDatabase by lazy {
        FirebaseDatabase.getInstance("https://prediai-default-rtdb.asia-southeast1.firebasedatabase.app/")
    }

    val auth: FirebaseAuth by lazy {
        FirebaseAuth.getInstance()
    }
}
