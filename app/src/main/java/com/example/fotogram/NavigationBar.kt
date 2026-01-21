package com.example.fotogram

import android.util.Log
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.graphics.Color
import io.ktor.client.request.invoke

@Composable
fun NavigationBar(
    currentSelectedScreen: Screen,
    onFeedClick: () -> Unit = {},
    onProfileClick: () -> Unit = {}
) {
        NavigationBar(
            modifier = Modifier.height(100.dp),
            containerColor = Color.LightGray,
            contentColor = Color.White,
        ) {
            NavigationBarItem(
                selected = currentSelectedScreen == Screen.FEED,
                icon = { Icon(
                    imageVector = Icons.Filled.Home,
                    contentDescription = "Feed",
                    modifier = Modifier.size(32.dp)) },
                label = {Text("Feed")},
                colors = NavigationBarItemDefaults.colors(
                  indicatorColor = Color.Transparent
                ),
                onClick = {
                    onFeedClick()
                    Log.d("NavigationBar", "Caricato Feed Screen")
                }
            )
            NavigationBarItem(
                selected = currentSelectedScreen == Screen.PROFILE,
                icon = { Icon(
                    imageVector = Icons.Filled.Person,
                    contentDescription = "Profile",
                    modifier = Modifier.size(32.dp)) },
                label = { Text("Profile") },
                colors = NavigationBarItemDefaults.colors(
                    indicatorColor = Color.Transparent
                ),
                onClick = {
                    onProfileClick()
                    Log.d("NavigationBar", "Caricato Profile Screen")
                }
            )
        }

}