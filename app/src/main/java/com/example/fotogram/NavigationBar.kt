package com.example.fotogram

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
fun NavigationBar() {
        NavigationBar(
            modifier = Modifier.height(100.dp),
            containerColor = Color.LightGray,
            contentColor = Color.White,
        ) {
            NavigationBarItem(
                icon = { Icon(
                    Icons.Filled.Home,
                    contentDescription = "Feed",
                    modifier = Modifier.size(32.dp)) },
                label = {Text("Feed")},
                colors = NavigationBarItemDefaults.colors(
                  indicatorColor = Color.Transparent
                ),
                selected = true,
                onClick = { /* TODO: Handle navigation click */ }
            )
            NavigationBarItem(
                icon = { Icon(
                    Icons.Filled.Person,
                    contentDescription = "Profile",
                    modifier = Modifier.size(32.dp)) },
                label = { Text("Profile") },
                selected = false,
                onClick = { /* TODO: Handle navigation click */ }
            )
        }

}