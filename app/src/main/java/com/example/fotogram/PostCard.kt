package com.example.fotogram

import com.mapbox.geojson.Point
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.material3.Text
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.TextButton
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.window.Dialog
import com.mapbox.maps.extension.compose.MapboxMap
import com.mapbox.maps.extension.compose.animation.viewport.rememberMapViewportState
import com.mapbox.maps.extension.compose.annotation.generated.PointAnnotation
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale
//import com.mapbox.maps.extension.compose.annotation.rememberIconImage



@Composable
fun PostCard(
    feedPostUI: FeedPostUI,
    isFullScreen: Boolean = false,
    onAuthorClick: (Int) -> Unit,
    onFollowToggle: () -> Unit,
    isMe: Boolean,
    onPostClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {
        Column {
            PostHeader(
                feedPostUI = feedPostUI,
                onAuthorClick = onAuthorClick,
                onFollowToggle = onFollowToggle,
                isMe = isMe
            )
            PostContentImage(post = feedPostUI.post, isFullScreen = isFullScreen, onClick = onPostClick)
            PostCaption(post = feedPostUI.post)
        }
    }
}
@Composable
fun PostHeader(
    feedPostUI: FeedPostUI,
    onAuthorClick: (Int) -> Unit,
    onFollowToggle: () -> Unit,
    isMe: Boolean
) {

    var showMapDialog by remember { mutableStateOf(false) }
    val hasCoordinates = feedPostUI.post.location?.latitude != null && feedPostUI.post.location?.longitude != null

    LaunchedEffect(feedPostUI.post.id) {
        Log.i("CARD_DEBUG", "Post ID: ${feedPostUI.post.id}, Location: ${feedPostUI.location?.latitude}, ${feedPostUI.location?.longitude}")
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            modifier = Modifier.clickable { onAuthorClick(feedPostUI.post.authorId)},
            verticalAlignment = Alignment.CenterVertically
        ) {

            val profileImageBitmap = feedPostUI.authorProfilePicture?.let { base64 ->
                decodedBase64Image(base64)
            }
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(Color.Gray)
            ) {
                if (profileImageBitmap != null) {
                    Image(
                        bitmap = profileImageBitmap,
                        contentDescription = "Immagine di profilo di ${feedPostUI.authorUsername}",
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                }
            }

            Column(
                modifier = Modifier
                    .padding(start = 8.dp)
            ) {
                Text(
                    text = "  ${feedPostUI.authorUsername}",
                    style = MaterialTheme.typography.titleMedium,
                )


                if(hasCoordinates) {
                        Row (
                            modifier = Modifier.clickable { showMapDialog = true },
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.LocationOn,
                                contentDescription = null,
                                modifier = Modifier.size(14.dp),
                                tint = MaterialTheme.colorScheme.primary
                            )
                            Text(
                                text = "Visualizza posizione",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.primary
                            )
                        }
                }

            }
        }
            Spacer(modifier = Modifier.weight(1f))

           if(!isMe) {
               TextButton(
                   onClick = onFollowToggle
               ) {
                   Text(
                       text = if(feedPostUI.isFollowingAuthor) "Segui già" else "Segui",
                       style = MaterialTheme.typography.titleMedium,
                       color = if(feedPostUI.isFollowingAuthor) {
                           Color(0xFF4CAF50)
                       }else MaterialTheme.colorScheme.primary
                   )
               }
           }
            if(showMapDialog && hasCoordinates) {
                PostLocationDialog(
                    location = feedPostUI.post.location!!,
                    onDismiss = { showMapDialog = false }
                )
            }
    }
}

@Composable
fun PostContentImage(post: Post, isFullScreen: Boolean, onClick: () -> Unit) {
    val imageBitmap = remember(post.contentPicture) {
        decodedBase64Image(post.contentPicture)
    }

    val imageHeight = if (isFullScreen) 500.dp else 400.dp

    if (imageBitmap != null) {
        Image(
            bitmap = imageBitmap,
            contentDescription = post.contentText ?: "Immagine del post",
            modifier = Modifier
                .fillMaxWidth()
                .clickable { onClick() }
                .requiredHeight(imageHeight),
            contentScale = ContentScale.Crop
        )
    }
}

@Composable
fun PostCaption(post: Post) {

    val dateTimeString = try {
        val instant = Instant.parse(post.createdAt)
        val localDateTime = instant.atZone(ZoneId.systemDefault()).toLocalDateTime()
        val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm", Locale.getDefault())
        localDateTime.format(formatter)
    } catch (e: Exception) {
       post.createdAt
    }

    if (!post.contentText.isNullOrEmpty()) {
        Text(
            text = post.contentText,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
            style = MaterialTheme.typography.bodyMedium
        )
    }

    Text(
        text = "Data pubblicazione: $dateTimeString",
        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
        style = MaterialTheme.typography.bodySmall,
        color = Color.Gray
    )
}

@Composable
fun PostLocationDialog(
    location: PostLocation,
    onDismiss: () -> Unit
) {
    val lat = location.latitude ?: 0.0
    val lon = location.longitude ?: 0.0
    val point = Point.fromLngLat(lon, lat)

    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .requiredHeight(450.dp),
            shape = RoundedCornerShape(16.dp)
        ) {
            Column {
                val mapViewportState = rememberMapViewportState {
                    setCameraOptions {
                        center(point)
                        zoom(14.0)
                    }
                }
                Box(modifier = Modifier.weight(1f)) {
                    MapboxMap(
                        modifier = Modifier.fillMaxSize(),
                        mapViewportState = mapViewportState
                    ) {
                    /*    val marker = rememberIconImage(
                            key = "post-marker-${location.latitude}-${location.longitude}",
                            painter = painterResource(id = R.drawable.location_marker)
                        )

                        PointAnnotation(point = point) {
                            iconImage = marker
                            iconSize = 1.0
                        }*/
                    }
                }
                Button(
                    onClick = onDismiss,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                ) {
                    Text(text = "Chiudi")
                }
            }
        }
    }
}
