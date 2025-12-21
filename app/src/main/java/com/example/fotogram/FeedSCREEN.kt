package com.example.fotogram

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FeedScreen(
    modifier: Modifier = Modifier,
    onNavigate: (Screen) -> Unit,
    viewModel: FeedViewModel = viewModel(
        factory = FeedViewModelFactory(LocalContext.current)
    )
) {

    val isRefreshing by viewModel.isLoading.collectAsState()
    val posts by viewModel.posts.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val scope = rememberCoroutineScope()

    val listState = rememberLazyListState()

    Scaffold(
        modifier = modifier
            .statusBarsPadding()
            .background(Color(0xFF1A95BB)),
        bottomBar = {
            NavigationBar(
                currentSelectedScreen = Screen.FEED,
                onFeedClick = {
                    viewModel.refreshFeed()
                    scope.launch {
                       try {
                           listState.animateScrollToItem(0)
                       } catch (e: Exception) {
                           Log.i("FeedScreen", "Errore durante lo scroll al primo elemento: ${e.localizedMessage}")
                       }
                    }

                },
                onProfileClick = { onNavigate(Screen.PROFILE) }
            )
        }
    ) { paddingValues ->

        PullToRefreshBox(
            isRefreshing = isLoading && posts.isNotEmpty(), //Mostra l'indicatore di refresh solo se non è il caricamento iniziale
            onRefresh = { viewModel.refreshFeed() },
            modifier = Modifier.padding(paddingValues = PaddingValues())
        ) {

        LazyColumn(
            state = listState,
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp)
        ) {
            if (isLoading && posts.isEmpty()) {
                item {
                    Box(
                        modifier = Modifier.fillParentMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }

            }
            items(posts) { feedPost ->
                PostCard(feedPostUI = feedPost)
            }

            if (posts.isNotEmpty()) {
                item {
                    LaunchedEffect(posts.size) {
                        viewModel.loadFeed()
                    }
                    if(isLoading) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator(modifier = Modifier.size(32.dp))
                        }
                    }
                }
            }
            if(!isLoading && posts.isEmpty()) {
                item {
                    Box(
                        modifier = Modifier.fillParentMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        androidx.compose.material3.Text(
                            text = "Nessun post disponibile. Segui altri utenti per vedere i loro post nel feed.",
                            color = Color.White
                        )
                    }
                }
            }
        }
        }
    }
}