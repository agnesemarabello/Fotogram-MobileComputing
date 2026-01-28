package com.example.fotogram

import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.setValue
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.window.DialogProperties
import androidx.compose.ui.window.Dialog
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.tasks.CancellationTokenSource
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

/** SCHERMATA DI FEED **/
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FeedScreen(
    modifier: Modifier = Modifier,
    onNavigate: (Screen) -> Unit,
    onNavigateToUser: (Int) -> Unit,
    listState: LazyListState = rememberLazyListState(),
    viewModel: FeedViewModel
) {

    val posts by viewModel.posts.collectAsState()

    //  Caricamento iniziale del feed al primo avvio
    LaunchedEffect(Unit) {
        if(posts.isEmpty()) {
            viewModel.loadFeed(isRefresh = true)
        }
    }

    val isLoading by viewModel.isLoading.collectAsState()
    val scope = rememberCoroutineScope()
    val myUserId by viewModel.myUserId.collectAsState()

    var selectedPost by remember { mutableStateOf<FeedPostUI?>(null) }

    //  Configurazione della posizione corrente del dispositivo
    val context = LocalContext.current
    val fusedLocationClient = remember {
        LocationServices.getFusedLocationProviderClient(context)
    }
    var hasPermission by remember {mutableStateOf(false)}

    //  Gestione della richiesta dei permessi di posizione
    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        hasPermission = isGranted
        if (isGranted) {
            Log.d("Posizione", "Permessi concessi dall'utente")
        } else {
            Log.d("Posizione", "Permessi negati dall'utente")
        }
    }

    //  Controllo e richiesta dei permessi
    LaunchedEffect(Unit) {
        hasPermission = checkLocationPermission(context)

        if(!hasPermission) {
            Log.d("Posizione", "Richiesta permessi di posizione")
            permissionLauncher.launch(android.Manifest.permission.ACCESS_FINE_LOCATION)
        }
    }

    //  Recupero della posizione corrente se i permessi sono stati concessi
    LaunchedEffect(hasPermission) {
        if(hasPermission) {
            try {
                Log.d("Posizione", "Calcolo la posizione corrente...")
                val location = fusedLocationClient.getCurrentLocation(
                    Priority.PRIORITY_HIGH_ACCURACY,
                    CancellationTokenSource().token
                ).await()

                if (location != null) {
                    Log.d("Posizione", "Lat ${location.latitude}, Long ${location.longitude}")
                    viewModel.updateUserLocation(location)
                } else {
                    Log.e("Posizione", "Posizione nulla")
                }
            } catch (e: Exception) {
                Log.e("Posizione", "Errore nel recupero della posizione: ${e.message}")
            }
        }
    }

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

        //  Aggiornamento manuale del feed
        PullToRefreshBox(
            isRefreshing = isLoading && posts.isNotEmpty(),
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
                Box(
                    modifier = Modifier.clickable { selectedPost = feedPost }
                ) {
                    PostCard(
                        feedPostUI = feedPost,
                        onAuthorClick = { authorId ->
                            if (authorId == myUserId) {
                                onNavigate(Screen.PROFILE)
                            } else {
                                onNavigateToUser(authorId)
                            }
                        },
                        onFollowToggle = {
                            viewModel.toggleFollow(
                                feedPost.post.authorId,
                                feedPost.isFollowingAuthor
                            )
                        },
                        isMe = feedPost.post.authorId == myUserId,
                        onPostClick = { selectedPost = feedPost }

                    )
                }
            }

            //  Caricamento automatico del feed quando si arriva in fondo alla lista
            if (posts.isNotEmpty()) {
                item {
                    LaunchedEffect(Unit) { //--------------------
                        viewModel.loadFeed()
                    }
                    if (isLoading) {
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
            if (!isLoading && posts.isEmpty()) {
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
    /*
        ***    Dialog del post selezionato    ***:
        Mostra un dialog a schermo intero con i dettagli del post selezionato.
    */
    selectedPost?.let { feedPostUI ->
        Dialog(
            onDismissRequest = { selectedPost = null },
            properties = DialogProperties(
                usePlatformDefaultWidth = false,
                dismissOnBackPress = true,
                dismissOnClickOutside = true
            )
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .clickable { selectedPost = null},
                contentAlignment = Alignment.Center
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth(0.95f)
                        .clickable(enabled = false) {}
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .verticalScroll(rememberScrollState())
                    ) {
                        PostCard(
                            feedPostUI = feedPostUI,
                            isFullScreen = true,
                            onAuthorClick = { authorId ->
                                selectedPost = null
                                if(authorId == myUserId) onNavigate(Screen.PROFILE)
                                else onNavigateToUser(authorId)
                            },
                            onFollowToggle = {
                                viewModel.toggleFollow(feedPostUI.post.authorId, feedPostUI.isFollowingAuthor)
                            },
                            isMe = true,
                            onPostClick = { selectedPost = null }
                        )
                    }
                }
            }
        }
    }
}