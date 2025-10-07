package com.example.prediai.cache

import com.example.prediai.domain.model.UserProfile

object UserData {
    var uid: String = ""
    var userProfile: UserProfile? = null

    fun set(uid: String, profile: UserProfile) {
        this.uid = uid
        this.userProfile = profile
    }

    fun clear() {
        uid = ""
        userProfile = null
    }
}