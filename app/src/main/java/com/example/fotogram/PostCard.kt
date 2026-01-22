package com.example.fotogram

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
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.material3.TextButton
import androidx.compose.runtime.remember
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale

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
            Text(
                text = "  ${feedPostUI.authorUsername}",
                style = MaterialTheme.typography.titleMedium,
            )
        }
            Spacer(modifier = Modifier.weight(1f))

           if(!isMe) {
               TextButton(
                   onClick = onFollowToggle
               ) {
                   Text(
                       text = if(feedPostUI.isFollowingAuthor) "Segui già" else "Segui",
                       style = MaterialTheme.typography.labelLarge,
                       color = if(feedPostUI.isFollowingAuthor) Color.Gray else MaterialTheme.colorScheme.primary
                   )
               }
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
