package com.example.fotogram

import android.util.Log
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.delay

enum class Screen {
    SETUP,
    FEED,
  //  POST,
    PROFILE,
    USER_DETAIL
}
@Composable
fun AppNavigator(dataStoreManager: DataStoreManager) {
    var currentScreen by remember {mutableStateOf<Screen?>(null)}
    var isLoading by remember { mutableStateOf(true) }

    var targetUserId by remember { mutableStateOf<Int?>(null) }

//    val feedViewModel: FeedViewModel = viewModel(factory = FeedViewModelFactory(LocalContext.current))
    val feedListState = rememberLazyListState()

    val context = LocalContext.current
    val requestManager = remember { RequestManager(dataStoreManager) }
    val postRepository = remember { PostRepository(requestManager) }
    val feedViewModel: FeedViewModel = viewModel {
        FeedViewModel(requestManager, postRepository)
    }
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
            val setUpProfileViewModel: SetUpProfileViewModel = viewModel {
                SetUpProfileViewModel(requestManager, dataStoreManager)
            }
            SetUpProfileScreen(
                viewModel = setUpProfileViewModel,
                onRegistrationComplete = {
                    currentScreen = Screen.FEED
                }
            )
        }

        Screen.FEED -> {
            FeedScreen(
                listState = feedListState,
                viewModel = feedViewModel,
                onNavigate = { screen -> currentScreen = screen },
                onNavigateToUser = { userId ->
                    targetUserId = userId
                    currentScreen = Screen.USER_DETAIL
                }
            )

        }

        Screen.PROFILE -> {
            val profileViewModel: ProfileViewModel = viewModel {
                ProfileViewModel(requestManager, dataStoreManager, postRepository)
            }
            ProfileScreen(
                viewModel = profileViewModel,
                onNavigate = { screen -> currentScreen = screen }
            )
        }

        Screen.USER_DETAIL -> {
            if(targetUserId == null) {
                LoadingScreen()
            } else {
                val userDetailViewModel: UserDetailViewModel = viewModel {
                    UserDetailViewModel(requestManager)
                }
                UserDetailScreen(
                    userId = targetUserId!!,
                    viewModel = userDetailViewModel,
                    onNavigate = { nextScreen -> currentScreen = nextScreen },
                    onFollowChanged = { authorId, isFollowing ->
                        feedViewModel.updateFollowState(authorId, isFollowing)
                    }
                )
            }
        }
        else -> if(isLoading) LoadingScreen()
    }
}