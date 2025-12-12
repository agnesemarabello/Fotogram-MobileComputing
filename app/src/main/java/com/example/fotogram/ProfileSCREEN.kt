package com.example.fotogram

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import io.ktor.websocket.Frame.Text
import androidx.compose.ui.Modifier

@Composable
fun ProfileScreen(
    modifier: Modifier = Modifier,
    onNavigate: (Screen) -> Unit
    ) {
    Scaffold(
        modifier = modifier
            .statusBarsPadding()
            .background(Color(0xFF1A95BB)),
        bottomBar = { NavigationBar (
            currentSelectedScreen = Screen.FEED,
            onFeedClick = { onNavigate(Screen.FEED) },
            onProfileClick = { onNavigate(Screen.PROFILE) }
        ) }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp)
        ) {

        }
    }
}