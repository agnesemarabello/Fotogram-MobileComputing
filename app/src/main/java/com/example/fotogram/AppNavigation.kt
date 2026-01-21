package com.example.fotogram

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.delay

enum class Screen {
    SETUP,
    FEED,
    PROFILE
}
@Composable
fun AppNavigator(dataStoreManager: DataStoreManager) {
    var currentScreen by remember {mutableStateOf<Screen?>(null)}
    var isLoading by remember { mutableStateOf(true) }

    val requestManager = remember { RequestManager(dataStoreManager) }
    val scope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        delay(3000)

        val sid = try {
            dataStoreManager.getSID()
        } catch (e: Exception) {
            Log.e("AppNavigator", "Errore nel recupero del SID: ${e.message}")
            null
        }
        if (sid != null && sid.isNotEmpty()) {
            currentScreen = Screen.FEED
            Log.i("AppNavigator", "SID Esistente: $sid -> Mostra FEED")
        } else {
            currentScreen = Screen.SETUP
            Log.d("AppNavigator", "SID mancante -> Avvio SETUP")
        }

        isLoading = false
    }
    when {
        isLoading || currentScreen == null -> {
            LoadingScreen()
        }

        currentScreen == Screen.SETUP -> {
            SetUpProfileScreen(
                onRegistrationComplete = {
                    currentScreen = Screen.FEED
                }
            )
        }

        currentScreen == Screen.FEED -> {
            FeedScreen(
                onNavigate = { screen ->
                        currentScreen = screen
                }
            )
        }

        currentScreen == Screen.PROFILE -> {
            ProfileScreen(
                onNavigate = { screen -> currentScreen = screen }
            )
        }
    }
}