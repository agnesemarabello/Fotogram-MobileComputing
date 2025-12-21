package com.example.fotogram

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
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
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale

@Composable
fun PostCard(feedPostUI: FeedPostUI) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {
        Column {
            PostHeader(feedPostUI = feedPostUI)
            PostContentImage(post = feedPostUI.post)
            PostCaption(post = feedPostUI.post)
        }
    }
}
@Composable
fun PostHeader(feedPostUI: FeedPostUI) {
    Row(
        modifier = Modifier
            .fillMaxSize()
            .padding(8.dp),
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
            if(profileImageBitmap != null) {
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

            Spacer(modifier = Modifier.weight(1f))

           /*if(feedPostUI.isFollowingAuthor) {
                Text(
                    text = "Segui già",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Green
                )
            } else {
                Text(
                    text = "Non segui",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Red
                )
            }*/
    }
}

@Composable
fun PostContentImage(post: Post) {
    val imageBitmap = decodedBase64Image(post.contentPicture)
    if (imageBitmap != null) {
        Image(
            bitmap = imageBitmap,
            contentDescription = post.contentText ?: "Immagine del post",
            modifier = Modifier
                .fillMaxWidth()
                .height(400.dp),
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
