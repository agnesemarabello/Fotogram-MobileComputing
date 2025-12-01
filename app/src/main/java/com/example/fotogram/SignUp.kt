package com.example.fotogram

import android.net.Uri
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ModifierLocalBeyondBoundsLayout
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp

@Composable
fun SignUp() {
    var username by remember { mutableStateOf("") }
    var profileImageUri by remember { mutableStateOf<Uri?>(null) }

    Column (
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding(),

        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Benvenuto in Fotogram!",
            style = MaterialTheme.typography.headlineMedium
        )


        Spacer(modifier = Modifier.height(150.dp))

        Row (
            modifier = Modifier
                .fillMaxWidth(),
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
                    .padding(horizontal = 20.dp)
                    .weight(1f)
                    .padding(start = 20.dp)
            )

            Button(
                onClick = {},
                shape = CircleShape,
                modifier = Modifier
                    .padding(end =  10.dp)
                    .size(68.dp)
                ) {

                if(profileImageUri == null) {
                    Icon(
                        imageVector = Icons.Filled.Person,
                        contentDescription = "Seleziona immagine profilo",
                        tint = MaterialTheme.colorScheme.onPrimary,
                        modifier = Modifier.fillMaxSize()
                    )
                }
            }

        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = {}) {
            Text(text = "Registrati")
        }
    }
}