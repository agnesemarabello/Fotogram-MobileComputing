package com.example.fotogram

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import io.ktor.websocket.Frame.Text
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.foundation.lazy.items

@Composable
fun FeedScreen(
    modifier: Modifier = Modifier,
    onNavigate: (Screen) -> Unit,
    viewModel: FeedViewModel = viewModel(
        factory = FeedViewModelFactory(LocalContext.current)
    )
) {

    val posts by viewModel.posts.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()

    Scaffold(
        modifier = modifier
            .statusBarsPadding()
            .background(Color(0xFF1A95BB)),
        bottomBar = {
            NavigationBar(
                currentSelectedScreen = Screen.FEED,
                onFeedClick = { onNavigate(Screen.FEED) },
                onProfileClick = { onNavigate(Screen.PROFILE) }
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp)
        ) {
            if(isLoading && posts.isEmpty()) {
                item {
                    Box(modifier = Modifier.fillParentMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator()
                    }
                }

            }
            items(posts) { post ->
                PostCard(post = post)
            }

            if(!isLoading && posts.isEmpty()) {
                item {
                    Text("Nessun post da mostrare.")
                }
            }
        }
    }
}