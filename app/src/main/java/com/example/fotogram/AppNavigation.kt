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
import io.ktor.client.request.request
import kotlinx.coroutines.delay

enum class Screen {
    SETUP,
    FEED,
    PROFILE,
    USER_DETAIL
}
@Composable
fun AppNavigator(dataStoreManager: DataStoreManager) {
    var currentScreen by remember {mutableStateOf<Screen?>(null)}
    var isLoading by remember { mutableStateOf(true) }

    var targetUserId by remember { mutableStateOf<Int?>(null) }

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
    when(currentScreen) {

        Screen.SETUP -> {
            SetUpProfileScreen(
                onRegistrationComplete = {
                    currentScreen = Screen.FEED
                }
            )
        }

        Screen.FEED -> {
            FeedScreen(
                onNavigate = { screen -> currentScreen = screen },
                onNavigateToUser = { userId ->
                    targetUserId = userId
                    currentScreen = Screen.USER_DETAIL
                }
            )

        }

        Screen.PROFILE -> {
            ProfileScreen(
                onNavigate = { screen -> currentScreen = screen }
            )
        }

        Screen.USER_DETAIL -> {
            targetUserId?.let { id ->
                val userDetailViewModel: UserDetailViewModel = viewModel(
                    factory = UserDetailViewModelFactory(requestManager)
                )
                UserDetailScreen(
                    userId = id,
                    viewModel = userDetailViewModel,
                    onNavigate = { nextScreen -> currentScreen = nextScreen }
                )
            }
        }
        else -> if(isLoading) LoadingScreen()
    }
}