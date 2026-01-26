package com.example.fotogram

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserDetailScreen(
    userId: Int,
    viewModel: UserDetailViewModel,
    onNavigate: (Screen) -> Unit,
    onFollowChanged: (Int, Boolean) -> Unit
) {
    val data by viewModel.userData.collectAsState()
    val posts by viewModel.userPosts.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()

    var selectedPost by remember { mutableStateOf<Post?>(null) }

    LaunchedEffect(userId) {
        viewModel.loadUser(userId)
    }

    Scaffold(
        bottomBar = {
            NavigationBar (
                currentSelectedScreen = Screen.USER_DETAIL,
                onFeedClick = { onNavigate(Screen.FEED)},
                onProfileClick = { onNavigate(Screen.PROFILE)}
            )
        }
    ) { paddingValues ->
        if(isLoading && data == null) {
            Box(
                modifier = Modifier
                    .fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        } else {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(3),
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(bottom = 16.dp)
                ) {
                    item(span = { GridItemSpan(3) }) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(start = 4.dp)
                        ) {
                            IconButton(
                                onClick = { onNavigate(Screen.FEED) },
                                modifier = Modifier
                                    .padding(8.dp)
                                    .align(Alignment.TopStart)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.ArrowBack,
                                    contentDescription = "Torna indietro",
                                    tint = Color.Black
                                )
                            }
                        }
                    }
                    item(span = { GridItemSpan(3) }) {
                        data?.let { user ->
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                ProfileHeaderContent(user)
                                Button(
                                    onClick = {
                                        viewModel.toggleFollow(
                                            user.id,
                                            user.isYourFollowing,
                                            onResult = { id, isFollowing ->
                                                onFollowChanged(id, isFollowing)
                                            }
                                        )
                                    },
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(horizontal = 32.dp, vertical = 8.dp),
                                    shape = RoundedCornerShape(8.dp),
                                    colors = if (user.isYourFollowing)
                                        ButtonDefaults.outlinedButtonColors()
                                    else
                                        ButtonDefaults.buttonColors()
                                ) {
                                    Text(if (user.isYourFollowing) "Smetti di seguire" else "Segui")
                                }

                                Spacer(modifier = Modifier.height(16.dp))
                                HorizontalDivider(
                                    modifier = Modifier.padding(horizontal = 16.dp),
                                    thickness = 0.5.dp,
                                    color = Color.LightGray
                                )
                            }
                        }
                    }

                    items(posts) { post ->
                        val bitmap = remember(post.contentPicture) {
                            decodedBase64Image(post.contentPicture)
                        }
                        Box(
                            modifier = Modifier
                                .aspectRatio(1f)
                                .padding(1.dp)
                                .background(Color.LightGray, shape = RoundedCornerShape(4.dp))
                                .clickable { selectedPost = post }
                        ) {
                            if (bitmap != null) {
                                Image(
                                    bitmap = bitmap,
                                    contentDescription = null,
                                    modifier = Modifier.fillMaxSize(),
                                    contentScale = ContentScale.Crop
                                )
                            }
                        }
                    }

                }
            }
        }
    }
    selectedPost?.let { post ->
        Dialog(
            onDismissRequest = {selectedPost = null},
            properties = DialogProperties(usePlatformDefaultWidth = false)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .clickable { selectedPost = null},
                contentAlignment = Alignment.Center
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth(0.95f)
                        .verticalScroll(rememberScrollState())
                        .clickable (enabled = false) {}
                ) {
                    PostCard(
                        feedPostUI = FeedPostUI(
                            post = post,
                            authorUsername = data?.username,
                            authorProfilePicture = data?.profilePicture,
                            isFollowingAuthor = data?.isYourFollowing ?: false
                        ),
                        isFullScreen = true,
                        onAuthorClick = { selectedPost = null},
                        onFollowToggle = {
                            viewModel.toggleFollow(
                                post.authorId,
                                data?.isYourFollowing ?: false,
                                onResult = { id, isFollowing ->
                                onFollowChanged(id, isFollowing)
                                }
                            )
                        },
                        isMe = false,
                        onPostClick = {}
                    )
                }
            }
        }
    }
}