package com.example.fotogram

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay

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
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Transparent),
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 450.dp, bottom = 30.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {

            Text( text = "Benvenuto in",
                fontSize = 24.sp
            )

            Text(
                text = "Fotogram",
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )

            Spacer(modifier = Modifier.weight(1.5f))

            CircularProgressIndicator(
                color = MaterialTheme.colorScheme.secondary
            )

            Spacer(modifier = Modifier.height(32.dp))

        }
    }
}