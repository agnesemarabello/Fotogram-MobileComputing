package com.example.fotogram

import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.runtime.*
import androidx.compose.ui.unit.dp
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.ui.draw.clip

@Composable
fun SignUp() {
    var username by remember { mutableStateOf("") }
    var profileImageUri by remember { mutableStateOf<Uri?>(null) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF90CAF9)),
        contentAlignment = Alignment.Center
    ) {
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
                OutlinedTextField(
                    value = username,
                    onValueChange = { newValue ->
                        username = newValue
                    },

                    supportingText = {
                        if (username.length > 15) {
                            Text(
                                "max 15 caratteri",
                                color = MaterialTheme.colorScheme.error
                            )
                        }
                    },

                    label = { Text("Inserisci il tuo username") },
                    singleLine = true,
                    modifier = Modifier
                        .padding(horizontal = 10.dp)
                        .weight(1f)
                )

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
                                    imageVector = if (profileImageUri == null) Icons.Filled.Add else Icons.Filled.Check,
                                    contentDescription = "Stato selezione immagine",
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {},
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 35.dp)
            ) {
                Text(text = "Registrati e inizia")
            }
        }
    }
}