package com.example.fotogram

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.fotogram.ui.theme.FotogramTheme
import com.mapbox.common.MapboxOptions

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        MapboxOptions.accessToken = getString(R.string.mapbox_access_token)
        setContent {
            FotogramTheme {
                AppNavigator(dataStoreManager = DataStoreManager(applicationContext))
            }
        }
    }
}