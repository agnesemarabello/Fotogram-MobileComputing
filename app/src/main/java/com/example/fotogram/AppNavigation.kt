package com.example.fotogram

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import kotlinx.coroutines.delay

enum class Screen {
    SIGNUP,
    FEED,
    PROFILE
}
@Composable
fun AppNavigator(dataStoreManager: DataStoreManager) {
    var currentScreen by remember {mutableStateOf(Screen.SIGNUP)}
    var isLoading by remember { mutableStateOf(true) }

    LaunchedEffect(Unit) {
        delay(3000)
        val sid = dataStoreManager.getSID()
        if (sid != null && sid.isNotEmpty()) {
            currentScreen = Screen.FEED
        } else {
            currentScreen = Screen.SIGNUP
        }

        isLoading = false
    }
    when {
        isLoading -> {
            LoadingScreen()
        }
        currentScreen == Screen.SIGNUP -> {
            SignUpScreen(
                onRegistrationComplete = {
                    currentScreen = Screen.FEED
                }
            )
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