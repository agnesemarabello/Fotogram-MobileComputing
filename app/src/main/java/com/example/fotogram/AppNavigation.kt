package com.example.fotogram

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import kotlinx.coroutines.delay

enum class Screen {
    FEED,
    PROFILE
}
@Composable
fun AppNavigator(dataStoreManager: DataStoreManager) {
    var currentScreen by remember {mutableStateOf(Screen.FEED)}
    var isLoading by remember { mutableStateOf(true) }

    val requestManager = remember { RequestManager(dataStoreManager) }
    val scope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        delay(3000)

        val sid = dataStoreManager.getSID()
        if (sid != null && sid.isNotEmpty()) {
            currentScreen = Screen.FEED
            Log.i("AppNavigator", "SID Esistente: $sid -> Mostra FEED")
        } else {
            Log.d("AppNavigator", "SID mancante -> Avvio registrazione implicita")

            val response = requestManager.registrationRequest()

            response?.let { regResponse ->
                val SID = regResponse.sessionId
                val UID = regResponse.userId

                dataStoreManager.saveSession(SID, UID)
                Log.i("AppNavigator", "Registrazione completata -> SID: $SID, UID: $UID")
                currentScreen = Screen.PROFILE
            } ?: run {
                Log.e("AppNavigator", "Registrazione fallita")
                currentScreen = Screen.PROFILE
            }
        }

        isLoading = false
    }
    when {
        isLoading -> {
            LoadingScreen()
        }

        currentScreen == Screen.FEED -> {
            FeedScreen(
                onNavigate = { screen -> currentScreen = screen }
            )
        }

        currentScreen == Screen.PROFILE -> {
            ProfileScreen(
                onNavigate = { screen -> currentScreen = screen }
            )
        }
        else -> {
            LoadingScreen()
        }
    }
}