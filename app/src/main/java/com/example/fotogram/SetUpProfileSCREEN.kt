package com.example.fotogram

import android.net.Uri
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext

@Composable
fun SetUpProfileScreen(
    onRegistrationComplete: () -> Unit,
    viewModel: SetUpProfileViewModel = viewModel()
) {
    var username by remember { mutableStateOf("") }
    var imageUri by remember { mutableStateOf<Uri?>(null) }
    var base64img by remember { mutableStateOf<String?>(null) }
    val context = LocalContext.current


}