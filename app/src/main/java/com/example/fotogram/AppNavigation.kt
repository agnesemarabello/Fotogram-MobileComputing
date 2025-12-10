package com.example.fotogram

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

@Composable
fun AppNavigator(dataStoreManager: DataStoreManager) {
    var currentScreen by remember {mutableStateOf(Screen.SIGNUP)}
    var isLoading by remember { mutableStateOf(true) }

    LaunchedEffect(Unit) {
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
            FeedScreen()
        }
        else -> {
            LoadingScreen()
        }
    }
}

@Composable
fun LoadingScreen() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator()
    }
}