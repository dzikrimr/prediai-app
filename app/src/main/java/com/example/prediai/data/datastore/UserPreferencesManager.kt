package com.example.prediai.data.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.prediai.domain.model.UserProfile
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "prediai_prefs")

class UserPreferencesManager(private val context: Context) {

    companion object {
        private val KEY_UID = stringPreferencesKey("user_uid")
        private val KEY_NAME = stringPreferencesKey("user_name")
        private val KEY_BIRTH_DATE = stringPreferencesKey("user_birth_date")
        private val KEY_HEIGHT = stringPreferencesKey("user_height")
        private val KEY_WEIGHT = stringPreferencesKey("user_weight")
        private val KEY_CITY = stringPreferencesKey("user_city")
        private val KEY_ONBOARDING = booleanPreferencesKey("onboarding_completed")
    }

    suspend fun saveUserProfile(uid: String, profile: UserProfile) {
        context.dataStore.edit { prefs ->
            prefs[KEY_UID] = uid
            prefs[KEY_NAME] = profile.name
            prefs[KEY_BIRTH_DATE] = profile.birthDate
            prefs[KEY_HEIGHT] = profile.height
            prefs[KEY_WEIGHT] = profile.weight
            prefs[KEY_CITY] = profile.city
        }
    }

    val userUidFlow: Flow<String?> = context.dataStore.data.map { it[KEY_UID] }

    val userProfileFlow: Flow<UserProfile> = context.dataStore.data.map { prefs ->
        UserProfile(
            name = prefs[KEY_NAME] ?: "",
            birthDate = prefs[KEY_BIRTH_DATE] ?: "",
            height = prefs[KEY_HEIGHT] ?: "",
            weight = prefs[KEY_WEIGHT] ?: "",
            city = prefs[KEY_CITY] ?: ""
        )
    }

    suspend fun clear() {
        context.dataStore.edit { it.clear() }
    }

    suspend fun setOnboardingCompleted(isCompleted: Boolean) {
        context.dataStore.edit { it[KEY_ONBOARDING] = isCompleted }
    }

    val isOnboardingCompleted: Flow<Boolean> = context.dataStore.data.map { it[KEY_ONBOARDING] ?: false }
}