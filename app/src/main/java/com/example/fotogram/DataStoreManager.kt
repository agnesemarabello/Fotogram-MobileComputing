package com.example.fotogram

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.first


private val Context.dataStore by preferencesDataStore(name = "settings")

val SID = stringPreferencesKey("sessionId")
val UID = intPreferencesKey("userId")

class DataStoreManager(private val context: Context) {
    val dataStore = context.dataStore

    suspend fun saveSession(sessionId: String, userId: Int) {
        dataStore.edit { preferences ->
            preferences[SID] = sessionId
            preferences[UID] = userId
        }
    }

    suspend fun getSID(): String? {
        val prefs = dataStore.data.first()
        return if(SID != null) {prefs[SID]} else null
    }

    suspend fun getUID(): Int? {
        val prefs = dataStore.data.first()
        return if(UID != null) {prefs[UID]} else null
    }
}

data class SessionData(
    val sessionId: String,
    val userId: String
)