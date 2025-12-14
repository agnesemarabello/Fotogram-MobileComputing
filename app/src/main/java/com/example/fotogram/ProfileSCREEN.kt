package com.example.fotogram

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import io.ktor.websocket.Frame.Text
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch

@Composable
fun ProfileScreen(
    modifier: Modifier = Modifier,
    onNavigate: (Screen) -> Unit
    ) {

    var imgBase64 by remember { mutableStateOf<String>("") }
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
        }

    }
        }
