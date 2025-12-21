package com.example.fotogram

import androidx.activity.result.contract.ActivityResultContracts.PickVisualMedia.DefaultTab.PhotosTab.value
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun ProfileScreen(
    modifier: Modifier = Modifier,
    onNavigate: (Screen) -> Unit,
    viewModel: ProfileViewModel = viewModel(factory = ProfileViewModelFactory(LocalContext.current))
    ) {
    val profile by viewModel.profileData.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()

    var imgBase64 by remember { mutableStateOf<String>("") }
    Scaffold(
        modifier = modifier
            .statusBarsPadding()
            .background(Color(0xFF1A95BB)),
        bottomBar = { NavigationBar (
            currentSelectedScreen = Screen.FEED,
            onFeedClick = { onNavigate(Screen.FEED) },
            onProfileClick = { viewModel.loadUserProfile() }
        ) }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(Color(0xFF1A95BB)),
            contentAlignment = Alignment.TopCenter
        ) {
            if(isLoading && profile == null) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            } else {
                profile?.let { data ->
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        val bitmap = data.profilePicture?.let { decodedBase64Image(it) }
                        Box(
                            modifier = Modifier
                                .size(120.dp)
                                .clip(CircleShape)
                                .background(Color.Gray),

                        ) {
                            if(bitmap != null) {
                                Image(
                                    bitmap = bitmap,
                                    contentDescription = "Foto profilo",
                                    modifier = Modifier.fillMaxSize(),
                                    contentScale = ContentScale.Crop
                                )
                            }
                        }
                        Spacer(modifier = Modifier.height(16.dp))

                        Text(text = "${data.username}", style = MaterialTheme.typography.headlineSmall)
                        Text(text = data.bio ?: "Nessuna bio disponibile", style = MaterialTheme.typography.bodyMedium, color = Color.DarkGray)

                        Spacer(modifier = Modifier.height(24.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceEvenly
                        ) {
                            ProfileStatColumn("Post", data.postsCount.toString())
                            ProfileStatColumn("Followers", data.followersCount.toString())
                            ProfileStatColumn("Following", data.followingCount.toString())
                        }
                    }

                }
            }
/*
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp)
            ) {
                item {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth(0.9f)
                            .fillMaxHeight(0.4f)
                            .background(
                                Color(0xFFF5F5F5),
                                shape = RoundedCornerShape(16.dp)
                            )
                            .padding(20.dp),

                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = "Benvenuto in Fotogram!",
                            style = MaterialTheme.typography.headlineMedium
                        )
                        Spacer(modifier = Modifier.height(60.dp))

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 5.dp),
                            verticalAlignment = Alignment.CenterVertically,
                        ) {

                            Box(
                                modifier = Modifier
                                    .size(80.dp)
                                    .align(Alignment.CenterVertically),
                                contentAlignment = Alignment.TopStart
                            ) {
                                Button(
                                    onClick = {},
                                    shape = CircleShape,
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = MaterialTheme.colorScheme.primary,
                                        contentColor = White
                                    ),
                                    modifier = Modifier
                                        .size(70.dp)
                                        .align(Alignment.TopStart)
                                ) {
                                    Box(
                                        modifier = Modifier.fillMaxSize(),
                                        contentAlignment = Alignment.Center
                                    ) {

                                        Icon(
                                            imageVector = Icons.Filled.Person,
                                            contentDescription = "Seleziona immagine profilo",
                                            modifier = Modifier.fillMaxSize()
                                        )
                                        Box(
                                            modifier = Modifier
                                                .align(Alignment.BottomEnd)
                                                .offset(x = 20.dp, y = 10.dp)
                                                .size(24.dp)
                                                .clip(CircleShape)
                                                .background(Color(0xFF90CAF9)),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Icon(
                                                imageVector = if (imgBase64 == "") Icons.Filled.Add else Icons.Filled.Check,
                                                contentDescription = "Stato selezione immagine",
                                                modifier = Modifier.size(20.dp)
                                            )
                                        }
                                    }
                                }
                            }
                        }
                        Spacer(modifier = Modifier.height(16.dp))
                    }
                }
            }*/
        }
    }
}

@Composable
fun ProfileStatColumn(label: String, count: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(text = count, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
        Text(text = label, style = MaterialTheme.typography.bodySmall, color = Color.DarkGray)
    }
}