package com.example.fotogram

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map


private val Context.dataStore by preferencesDataStore(name = "settings")


class DataStoreManager(private val context: Context) {
    val dataStore = context.dataStore
    private val SID = stringPreferencesKey("sessionId")
    private val UID = intPreferencesKey("userId")

    suspend fun saveSession(sessionId: String, userId: Int) {
        dataStore.edit { preferences ->
            preferences[SID] = sessionId
            preferences[UID] = userId
        }
    }

    suspend fun getSID(): String? {
        return dataStore.data.map { preferences ->
            preferences[SID]
        }.firstOrNull()
    }

    suspend fun getUID(): Int? {
        return dataStore.data.map { preferences ->
            preferences[UID]
        }.firstOrNull()
    }

}

data class SessionData(
    val sessionId: String,
    val userId: String
)