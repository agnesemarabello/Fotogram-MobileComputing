package com.example.fotogram

import android.util.Log
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
import androidx.compose.ui.platform.LocalContext
import kotlinx.coroutines.launch


@Composable
fun SignUpScreen(onRegistrationComplete: () -> Unit){
    var username by remember { mutableStateOf("") }
    var imgBase64 by remember { mutableStateOf<String>("") }

    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    val dataStoreManager = remember {DataStoreManager(context)}
    val requestManager = remember {RequestManager(dataStoreManager)}

    val isUsernameValid = username.length <= 15
    val isReadyToRegister = isUsernameValid //&& imgBase64.isNotEmpty()

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

            Button(
                onClick = {
                    if(!isReadyToRegister) {
                        Log.d("SignUp", "Dati mancanti o non validi.")
                        return@Button
                    }
                    scope.launch {
                        val requestManager = RequestManager(dataStoreManager)
                        val response = requestManager.registrationRequest()

                        response?.let { regResponse ->
                            val SID = regResponse.sessionId
                            val UID = regResponse.userId

                            dataStoreManager.saveSession(SID, UID)
                            Log.i(
                                "SignUp",
                                "Dati di sessione salvati nel DataStore -> SID: $SID, UID: $UID"
                            )

                            val profileDetails = requestManager.updateProfileRequest(
                                newUsername = username,
                                newBio = "Ciao! Sono nuovo su Fotogram.",
                                newDateOfBirth = "2000-01-01"
                            )

                            if (profileDetails != null) {
                                Log.d(
                                    "SignUp",
                                    "Profilo salvato -> SID: ${response.sessionId}, UID: ${response.userId}"
                                )
                                onRegistrationComplete()
                            } else {
                                Log.d("SignUp", "Caricamento dati profilo fallito.")
                            }
                        }
                    }

                },
                enabled = isReadyToRegister,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 35.dp)
            ) {
                Text(text = "Registrati e inizia")
            }
        }
    }
}

