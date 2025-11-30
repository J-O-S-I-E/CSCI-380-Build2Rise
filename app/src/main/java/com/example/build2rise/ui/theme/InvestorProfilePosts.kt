package com.example.build2rise.ui.theme

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayCircleOutline
import androidx.compose.material.icons.filled.Description
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage

/**
 * Card component for displaying a single investor post in grid
 * Used in MainScreen.kt Profile tab
 */
@Composable
fun PostCard(post: com.example.build2rise.data.model.PostResponse) {
    Column(
        modifier = Modifier
            .background(Almond, RoundedCornerShape(8.dp))
            .padding(8.dp)
            .height(160.dp)
    ) {
        // Display actual media based on post type
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1f)
                .clip(RoundedCornerShape(8.dp))
                .background(Almond),
            contentAlignment = Alignment.Center
        ) {
            when (post.postType) {
                "image" -> {
                    if (post.mediaUrl != null) {
                        AsyncImage(
                            model = post.mediaUrl,
                            contentDescription = "Post image",
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop
                        )
                    } else {
                        Text(
                            text = "image",
                            color = RussianViolet,
                            fontSize = 14.sp
                        )
                    }
                }
                "video" -> {
                    if (post.mediaUrl != null) {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .background(RussianViolet.copy(alpha = 0.1f)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.PlayCircleOutline,
                                    contentDescription = "Video",
                                    tint = RussianViolet,
                                    modifier = Modifier.size(48.dp)
                                )
                            }
                        }
                    } else {
                        Text(
                            text = "video",
                            color = RussianViolet,
                            fontSize = 14.sp
                        )
                    }
                }
                "text" -> {
                    Icon(
                        imageVector = Icons.Default.Description,
                        contentDescription = "Text post",
                        tint = RussianViolet.copy(alpha = 0.6f),
                        modifier = Modifier.size(32.dp)
                    )
                }
                else -> {
                    Text(
                        text = post.postType,
                        color = RussianViolet,
                        fontSize = 14.sp
                    )
                }
            }
        }

        Spacer(Modifier.height(4.dp))

        Text(
            text = post.postDescription?.take(20) ?: "Post",
            fontWeight = FontWeight.Bold,
            color = RussianViolet,
            fontSize = 12.sp,
            maxLines = 1
        )

        Text(
            text = formatPostTime(post.createdAt),  // Uses function from FounderPostCard.kt
            color = Color.Gray,
            fontSize = 11.sp
        )
    }
}