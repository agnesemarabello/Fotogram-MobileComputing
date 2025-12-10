package com.example.fotogram

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import com.example.fotogram.ui.theme.FotogramTheme

enum class Screen {
    FEED,
    SIGNUP
}

val currentScreen = mutableStateOf(Screen.FEED)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            FotogramTheme {
                AppNavigator()
            }
        }
    }
}

@Composable
fun AppNavigator() {
    when (currentScreen.value) {
        Screen.SIGNUP -> SignUpScreen(
            onRegistrationComplete = {
                currentScreen.value = Screen.FEED
            }
        )
        Screen.FEED -> FeedScreen()
    }
}